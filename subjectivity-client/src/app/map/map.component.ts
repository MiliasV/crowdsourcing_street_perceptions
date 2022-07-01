import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  ElementRef,
  Input, Output, EventEmitter
} from '@angular/core';
import Map from 'ol/Map';
import OSM from 'ol/source/OSM';
import TileLayer from 'ol/layer/Tile';
import {Attribution, defaults as defaultControls} from 'ol/control';
import {DoubleClickZoom} from 'ol/interaction';
import {MapUtilService} from "./map-util.service";
import {Location} from "./location";
import {CommandService} from "../services/command.service";
import {ImageryService} from "../imagery/imagery.service";
import * as _ from 'lodash';
import {ActivatedRoute} from "@angular/router";
import {Log} from "../session/log";
import {SessionService} from "../session/session.service";
import {transform} from "ol/proj";
import {combineLatest, Subscription} from "rxjs";
import {Rate} from "../rate/rate";

@Component({
  selector: 'app-map',
  template: '',
  styles: [':host { width: 100%; height: 100%; display: block; }'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class MapComponent implements OnInit {

  map: Map;
  recordings: Array<any>;
  currentImageIndex: number;
  currentImage: any = null;
  lastImage: any = null;
  subscriptions: Subscription[] = [];

  @Input() pathId: number;
  @Output() complete = new EventEmitter();
//  @Output() rateReload = new EventEmitter();



  constructor(private elementRef: ElementRef, private route: ActivatedRoute, private mapService: MapUtilService,
              private imageryService: ImageryService, private command: CommandService,
              private sessionService: SessionService) {
    this.subscriptions.push(this.imageryService.getNavigator().subscribe(this.navigate.bind(this)));
  }

  ngOnInit() {
    this.subscriptions.push(combineLatest([
      this.imageryService.getApiInit(),
      this.sessionService.getSession(),
    ]).subscribe(([flag, sessionId]) => {
      if (flag && sessionId != null) {
        this.initMap();
        this.loadPath();
      }
    }));
  }

  initMap() {
    const attribution = new Attribution({
      collapsible: false,
    });
    this.map = new Map({
      layers: [
        new TileLayer({
          source: new OSM(),
        }),
      ],
      controls: defaultControls({attribution: false}).extend([attribution])
    });

    this.map.getInteractions().getArray().forEach((interaction) => {
      if (interaction instanceof DoubleClickZoom) {
        this.map.removeInteraction(interaction);
      }
    });

    this.map.setTarget(this.elementRef.nativeElement);
    this.mapService.setMap(this.map);
    this.mapService.addImageLocationLayer();
    this.map.on('singleclick', this.onClick.bind(this));
    this.map.on('postrender', this.mapLoaded.bind(this));
    this.map.on("pointermove", this.onPointerMove.bind(this));

  }


  loadPath() {
    let url = 'path/get';
    if (this.pathId != null)
      url = url + '/' + this.pathId;
    this.command.execute(url, 'GET', 'json', {}, true).subscribe((recordings) => {
      this.recordings = recordings;
      setTimeout(() => {
        this.mapService.drawImagePoints(recordings);
        this.getImageByIndex(1);
      }, 1000);
    }, (error => {
      console.error(error);
    }));
  }

  onClick(data) {
    let feature = this.map.forEachFeatureAtPixel(data.pixel, (feature, layer) => {
      return feature;
    });
    if (feature) {
      let index = feature.get('orderId');
      this.getImageByIndex(Number(index));
    }
  }

  onPointerMove(data) {
    let hit = this.map.forEachFeatureAtPixel(data.pixel, (feature, layer) => {
      return feature;
    });
    if (hit)
      this.elementRef.nativeElement.style.cursor = 'pointer';
    else
      this.elementRef.nativeElement.style.cursor = '';
  }

  drawCurrentLocation(x: number, y: number, direction:number, isFirstTime: boolean) {
    let point = transform([x, y], 'EPSG:3857', 'EPSG:4326');
    let orientation = {yaw: direction, hFov: 110.0};
    let location: Location = {
      lng: point[0],
      lat: point[1],
      orientation: orientation
    };
    this.mapService.drawCurrentLocation(location, isFirstTime);
  }

  mapLoaded(flag?: boolean) {
    this.mapService.setMapLoaded(flag);
  }

  imageApiInit(flag?: boolean){
    if(flag){

    }
  }

  getImageByIndex(orderId: number, direction?: string) {
    this.currentImageIndex = orderId;
    let image = _.find(this.recordings, record => {
      return record.orderId == orderId;
    });
    if(image && image.end) this.complete.next(true);
    let orientation = this.getDirection(image.orderId, direction);
    if (orientation) image['direction'] = orientation;
    this.currentImage = image;
    this.drawCurrentLocation(image.lng, image.lat, image.direction, image.start);
    this.mapService.setCurrentImage(image);
    this.mapService.setImagePointFeature(orderId, 'visited', true);
    let rate = this.mapService.getImagePointFeature(orderId,'rate');
    /*if (rate != null) {
      this.rateReload.next(rate);
    }*/
  }

  getDirection(orderId: number, direction?: string) {
    let image1 = _.find(this.recordings, record => {
      return record.orderId == orderId;
    });
    let previousOrderId;
    if (direction == null)
      previousOrderId = Number(orderId) + 1;
    else {
      previousOrderId = (direction === 'backward') ? (Number(orderId) - 1) : (Number(orderId) + 1);
    }
    let image2 = _.find(this.recordings, record => {
      return record.orderId == previousOrderId;
    });
    if (!image2) return null;
    let dx = image2.lng - image1.lng;
    let dy = image2.lat - image1.lat;
    let angle = Math.atan2(dx, dy);
    let degrees = angle * (180 / Math.PI);
    return degrees;
  }

  navigate(direction: string) {
    if (direction != null) {
      if (direction === 'backward') {
        if (!this.currentImage.start)
          this.getImageByIndex(this.currentImage.orderId - 1, direction);
      } else {
        if (!this.currentImage.end)
          this.getImageByIndex(this.currentImage.orderId + 1, direction);
      }
    }
  }


  ngOnDestroy() {
    this.mapService.destroyMap();
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }
}
