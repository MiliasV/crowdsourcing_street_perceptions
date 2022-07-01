import {ChangeDetectionStrategy, Component, ElementRef, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import { transform } from 'ol/proj.js';
import * as StreetSmartApi from '../../assets/js/StreetSmartApi.js';
import {Fill, Stroke, Style} from "ol/style";
import {MapUtilService} from "../map/map-util.service";
import {Location} from "../map/location";
import {Element} from "@angular/compiler";
import {ImageryService} from "./imagery.service";
import {debounceTime, distinctUntilChanged, Subject, Subscription} from "rxjs";
import {SessionService} from "../session/session.service";
import {Log} from "../session/log";
declare var StreetSmartApi: any;

@Component({
  selector: 'app-imagery',
  templateUrl: './imagery.component.html',
  styleUrls: ['./imagery.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ImageryComponent implements OnInit {

  streetApi: StreetSmartApi;
  username = "kb@wigeogis.com"
  password = "Ghtj4$fgQ[s2[qN"
  viewChangedNotifier = new Subject<any>();
  subscriptions: Subscription[] = [];
  panoramaViewer: StreetSmartApi.panoramaViewer;
  @ViewChild('streetsmartApi', {static: true}) viewerElement: ElementRef;


  constructor(private mapService: MapUtilService, public imageryService: ImageryService, public sessionService: SessionService) {
    this.subscriptions.push(this.mapService.getCurrentImage().subscribe(this.changeImage.bind(this)));
    this.subscriptions.push(this.imageryService.openImageNotifier().subscribe(this.changeImageByNameAndInfo.bind(this)));
    this.subscriptions.push(this.viewChangedNotifier.pipe(debounceTime(500)).subscribe((view) => {
      if (view != null) {
        this.mapService.setCurrentView(view);
      }
    }));
  }


  ngOnInit(): void {
    StreetSmartApi.init({
      targetElement: this.viewerElement.nativeElement,
      username: this.username,
      password: this.password,
      apiKey: "uzsd2zgBGrQNq7MFOPMNyPkxeHc9-vsNWv8BuTo5yDU1mT8pavERiPo16j81WnEB",
      srs: "EPSG:3857",
      locale: 'en-us',
      addressSettings: {
        locale: "us",
        database: "Nokia"
      },
      configurationUrl: 'https://atlas.cyclomedia.com/configuration/'
    }).then(
      () => {
        console.log('Api: init: success!');
        this.imageryService.setApiInit();
      }, (err) => {
        console.error('Api: init: failed. Error: ', err);
      });

  }


  openImage(imageId: string, direction?: number, pitch?: number, pov?: number) {
    if (imageId && imageId != '') {
      let viewerType = (StreetSmartApi.ViewerType.PANORAMA);
      StreetSmartApi.open(imageId, {
        viewerType: viewerType,
        srs: 'EPSG:3857',
      }).then((result) => {
        if (result && result[0]) {
          if (result[0].getType() === StreetSmartApi.ViewerType.PANORAMA) {
            this.reset();
            let panoramaViewer = result[0];
            panoramaViewer.toggle3DCursor(true);
            panoramaViewer.toggleNavbarVisible(false);
            panoramaViewer.toggleAddressesVisible(false);
            panoramaViewer.toggleRecordingsVisible(false);
            panoramaViewer.setOrientation({yaw: direction, pitch: pitch, hFov: pov} );
            this.panoramaViewer = panoramaViewer;
            this.panoramaViewer.on(StreetSmartApi.Events.panoramaViewer.VIEW_CHANGE, this.viewChanged.bind(this));
            this.panoramaViewer.on(StreetSmartApi.Events.panoramaViewer.VIEW_LOAD_END, this.viewerLoaded.bind(this));
          }
        }
      });
    }
  }

  viewChanged() {
    let rl = this.panoramaViewer.getRecording();
    if (rl) {
      let point = transform([rl.xyz[0], rl.xyz[1]], 'EPSG:3857', 'EPSG:4326');
      let orientation = this.panoramaViewer.getOrientation();
      this.mapService.drawLocationCone([point[0], point[1]], orientation);
      this.viewChangedNotifier.next(orientation);
    }
  }

  changeImage(currentImage: any) {
    if (currentImage)
      this.openImage(currentImage.imageId, currentImage.direction);
  }

  changeImageByNameAndInfo(image: any){
    if (image)
      this.openImage(image.imageId, image.direction, image.pitch, image.pov);
  }

  featureClick(type, detail){
    console.log(type);
    console.log(detail);
    //event.stopPropagation();
  }

  viewerLoaded() {
    this.imageryService.setViewerLoaded();
  }

  reset() {
    if (this.panoramaViewer != null) {
      this.panoramaViewer.off(StreetSmartApi.Events.panoramaViewer.VIEW_CHANGE);
      this.panoramaViewer.off(StreetSmartApi.Events.panoramaViewer.VIEW_LOAD_END);
    }
  }

  ngOnDestroy() {
    this.imageryService.destroy();
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
    this.viewChangedNotifier.complete();
    StreetSmartApi.destroy({
      targetElement: this.viewerElement.nativeElement
    });
  }
}
