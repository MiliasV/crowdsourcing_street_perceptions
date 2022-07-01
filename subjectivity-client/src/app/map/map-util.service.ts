import { Injectable } from '@angular/core';
import Map from "ol/Map";
import View from 'ol/View';
import VectorLayer from 'ol/layer/Vector';
import TileLayer from 'ol/layer/Tile';
import OSM from 'ol/source/OSM';
import VectorSource from 'ol/source/Vector';
import Point from 'ol/geom/Point';
import Feature from 'ol/Feature';
import { fromLonLat } from 'ol/proj.js';
import {toContext} from 'ol/render';
import {Polygon} from "ol/geom";
import BaseLayer from "ol/layer/Base";
import {Location} from "./location";
import {Circle, Fill, Icon, Stroke, Style} from "ol/style";
import {BehaviorSubject, Observable} from "rxjs";
import {Extent} from "ol/interaction";
import {buffer} from 'ol/extent';

@Injectable({
  providedIn: 'root'
})
export class MapUtilService {

  map: Map;
  locLayer: VectorLayer<any>;
  coneLayer: VectorLayer<any>;
  imageLocLayer: VectorLayer<any>;
  mapLoaded = new BehaviorSubject<any>(false);
  mapReload = new BehaviorSubject<any>(false);
  _currentImage = new BehaviorSubject<any>(null);
  _currentView = new BehaviorSubject<any>(null);

  constructor() { }

  setMap(map: Map){
    this.map = map;
  }

  addImageLocationLayer(){
    this.imageLocLayer = new VectorLayer({
      source: new VectorSource(),
      style: this.getImagePointStyle.bind(this),
      visible: true
    });
    this.imageLocLayer.set('name', 'image_location');
    this.imageLocLayer.setZIndex(2);
    this.map.addLayer(this.imageLocLayer);
  }

  drawImagePoints(recordings: Array<any>) {
    if (recordings && recordings.length > 0) {
      let source = this.imageLocLayer.getSource();
      recordings.forEach((record)=>{
        let geom = new Point([record.lng, record.lat]);
        let elem = {geometry: geom};
        let feature = new Feature(elem);
        feature.setId(Number(record.orderId));
        feature.set('imageId', record.imageId);
        feature.set('direction', record.direction);
        feature.set('orderId', record.orderId);
        feature.set('pathId', record.pathId);
        feature.set('start', record.start);
        feature.set('end', record.end);
        feature.set('visited', false);
        feature.set('rated', false);
        source.addFeature(feature);
      });
    }
  }

  drawCurrentLocation(location : Location, isFirstTime: boolean) {
    this.drawLocationMarker(location.lng, location.lat);
    this.drawLocationCone([location.lng, location.lat], location.orientation);
    if (isFirstTime) {
      let ext = this.imageLocLayer.getSource().getExtent();
      this.map.getView().fit(ext);
      this.map.getView().setZoom(this.map.getView().getZoom() - 0.3);
    } else {
      this.map.getView().animate({
        center: fromLonLat([location.lng, location.lat]),
        duration: 1000,
        zoom: 18
      });
    }
    this.map.render();
  }

  calculateConeCoords(options) {
    var hFov = options.hFov;
    var scale = options.scale;
    // The viewing cone mirrors around the central viewing direction axis.
    // We'll split the cone into 2 virtual triangles, like this:
    //
    //                       a = sin(angle)
    //      _________           B ____ C                      ____
    //      \   |   /             \   |                      |   /
    //       \  |  /   =           \  |                   +  |  /
    //        \ | /           c = 1 \ | b =                  | /
    //         \|/                   \|  sqrt(c^2 - a^2)     |/
    //                                A
    //
    // Since both triangles now contain one 90 degree angle, we can use simple trigonometry to compute the points.
    // Note that below, angle and width are based on one of those sides, not the total of the two.
    // Get half the field of view
    var angle = hFov / 2.0;

    // Using law of sines:
    //      sin A = a / c
    //   =>     a = c * sin A
    // where a is the side opposite to A, and c is the hypotenuse (edge without right angle), which we set to 1.0.
    var width = Math.sin(angle);

    // Use famous Pythagoras formula to compute length, using a constant unit length for the hypotenuse
    var length = Math.sqrt(1.0 - width * width);

    // Total area of 2 triangles
    var area = width * length;

    // Scale such that it has a desired screen size, and make scaling dependent on actual area.
    // As a result of this, smaller view angles will result in an elongated cone, whereas greater angles will
    // produce a shorter cone.
    // NB: this will also happen to a smaller extent without this area-based scaling, since we are using a constant
    //     length for the hypotenuse.
    var size = scale / Math.sqrt(area);

    // Coordinates for the cone polygon. Must be closed by repeating first coordinate.
    // Make sure all coordinates fit within a canvas: range [0,0] to [2*width,height]
    return [
      [0, 0],
      [size * width * 2, 0],
      [size * width, size * length],
      [0, 0],
    ];
  }

  createCanvasContext2D(opt_width: any, opt_height: any){
    let canvas = document.createElement('CANVAS') as any;
    if (opt_width) {
      canvas.width = opt_width;
    }
    if (opt_height) {
      canvas.height = opt_height;
    }
    return canvas.getContext('2d');
  }

  drawLocationMarker(lng: number, lat: number) {
    this.map.removeLayer(this.locLayer);
    const feature = new Feature({
      geometry: new Point(fromLonLat([lng, lat]))
    });
    this.locLayer = new VectorLayer({
      source: new VectorSource({
        features: [feature]
      }),
      style: this.getMarkerStyle(),
      visible: true
    });
    this.locLayer.set('name', 'current_location');
    this.locLayer.setZIndex(3);
    this.map.addLayer(this.locLayer);
  }

  drawLocationCone(location: any, orientation: any) {
    if (this.map != null) {
      this.map.removeLayer(this.coneLayer);
      let viewerData: any = {};
      viewerData.yaw = orientation.yaw * Math.PI / 180;
      viewerData.hFov = (orientation.hFov) ? (orientation.hFov * Math.PI / 180.0) : 110.0;
      viewerData.xyz = location;
      viewerData.srs = 'EPSG:3857';
      viewerData.scale = 50;


      this.coneLayer = new VectorLayer({
        source: new VectorSource(),
        visible: true,
        updateWhileInteracting: true,
      });

      let coordinates = this.calculateConeCoords({hFov: viewerData.hFov, scale: viewerData.scale});
      // return length and width of cone to fit in. (+1 for margin on edges)
      let dimensions = [
        coordinates[1][0] + 0.5,
        coordinates[2][1] + 0.5,
      ];

      // Create canvas to draw the cone on.
      let context = this.createCanvasContext2D(dimensions[0], dimensions[1]);
      let vectorContext = toContext(context, {size: dimensions, pixelRatio: 1});

      let colorStyle = new Style({
        fill: new Fill({color: [252, 205, 58, 0.5]}),
        stroke: new Stroke({color: [252, 205, 58, 0.5], width: 0.2})
      });

      // Set the coloring style for the cone.
      vectorContext.setStyle(colorStyle);

      // Draw the cone on the canvas.
      vectorContext.drawGeometry(new Polygon([coordinates]));

      this.coneLayer.setStyle(new Style({
        image: new Icon({
          img: context.canvas,
          rotation: viewerData.yaw,
          anchor: [0.5, 1],
          imgSize: dimensions,
          rotateWithView: true,
        })
      }));
      this.coneLayer.setZIndex(1);
      this.coneLayer.set('name', 'cone_1');
      this.map.addLayer(this.coneLayer);

      let mapSource = this.coneLayer.getSource();
      let coneFeature = mapSource.getFeatureById('cone_1');

      if (!coneFeature) {
        let point_geom = new Point(fromLonLat([viewerData.xyz[0], viewerData.xyz[1]]));

        // Build feature from polygon
        coneFeature = new Feature(point_geom);


        // Set id, type, style and viewerData for the feature
        coneFeature.setId('cone_1');
        coneFeature.set('viewerData', viewerData);
        coneFeature.set('color', colorStyle);
        coneFeature.set('type', 'cone');

        // Add circleFeature and coneFeature to maplayer.
        this.coneLayer.getSource().addFeature(coneFeature);
      } else {
        // update viewerdata of the feature.
        coneFeature.set('viewerData', viewerData);
        // Needed to draw the cone on the correct location.
        let coneGeometry = coneFeature.getGeometry() as Point;
        coneGeometry.setCoordinates([viewerData.xyz[0], viewerData.xyz[1]]);
      }
    }
  }

  getMarkerStyle() {
    return new Style({
      image: new Icon({
        anchor: [16, 28],
        size: [32, 32],
        imgSize: [32, 32],
        anchorXUnits: 'pixels',
        anchorYUnits: 'pixels',
        opacity: 1.0,
        src: 'assets/street-view.png',
      }),
    });
  }

  getRatedStyle() {
    return new Style({
      image: new Icon({
        src: 'assets/rated.svg',
      }),
    });
  }

  getImagePointStyle(feature) {
    let fill = '#4060FF';
    let stroke = '#2030DD';
    let radius = 5;
    if (feature.get('start') || feature.get('visited')) {
      fill = '#06a431';
      stroke = '#03671d';
      if (feature.get('start')) radius = 7;
    } else if (feature.get('end')) {
      fill = '#dd2020';
      stroke = '#8c1616';
      radius = 7;
    }
    if(feature.get('rated')){
      return this.getRatedStyle();
    }else{
      return new Style({
        image: new Circle({
          radius: radius,
          fill: new Fill({color: fill}),
          stroke: new Stroke({color: stroke, width: 1})
        })
      });
    }
  }

  public getRatedCount() {
    let counter = 0;
    let features = this.imageLocLayer.getSource().getFeatures();
    features.forEach((feature) => {
      if (feature.get('rated'))
        counter = counter + 1;
    });
    return counter;
  }

  public getUnvisitedCount() {
    let counter = 0;
    let features = this.imageLocLayer.getSource().getFeatures();
    features.forEach((feature) => {
      if (!feature.get('visited'))
        counter = counter + 1;
    });
    return counter;
  }

  public get currentImage(){
    return this._currentImage.value;
  }

  setCurrentImage(currentImage: any){
    if(currentImage)
      this._currentImage.next(currentImage);
  }

  getCurrentImage(): Observable<any> {
    return this._currentImage.asObservable();
  }

  public get currentView(){
    return this._currentView.value;
  }

  setCurrentView(currentView: any){
    if(currentView)
      this._currentView.next(currentView);
  }

  getCurrentView(): Observable<any> {
    return this._currentView.asObservable();
  }

  setMapLoaded(flag?: boolean) {
    if (!this.mapLoaded.value)
      this.mapLoaded.next(flag || true);
  }

  getMapLoaded(): Observable<any> {
    return this.mapLoaded.asObservable();
  }

  setMapReload(flag?: boolean) {
    this.mapReload.next(flag || false);
  }

  getMapReload(): Observable<any> {
    return this.mapReload.asObservable();
  }

  setImagePointFeature(id: number, feature: string, value: any){
    let pointSource = this.imageLocLayer.getSource();
    if(pointSource && pointSource.getFeatureById(id)) {
      pointSource.getFeatureById(id).set(feature, value);
    }
  }

  getImagePointFeature(id: number, feature: string){
    let pointSource = this.imageLocLayer.getSource();
    if(pointSource && pointSource.getFeatureById(id)) {
      return pointSource.getFeatureById(id).get(feature);
    }
    return null;
  }

  destroyMap(){
    this.locLayer = null;
    this.coneLayer = null;
    this.imageLocLayer = null;
    this.mapLoaded = new BehaviorSubject<any>(false);
    this._currentImage = new BehaviorSubject<any>(null);
    this._currentView = new BehaviorSubject<any>(null);
    if(this.map != null)
      this.map.setTarget(null);
    this.map = null;
  }

}
