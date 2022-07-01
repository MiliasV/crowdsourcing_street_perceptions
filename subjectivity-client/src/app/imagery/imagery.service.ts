import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {CommandService} from "../services/command.service";

@Injectable({
  providedIn: 'root'
})
export class ImageryService {

  private apiInitialized = new BehaviorSubject<boolean>(false);
  private viewerLoaded = new BehaviorSubject<any>(false);
  private imageSetter = new BehaviorSubject<any>(null);
  private navigator = new BehaviorSubject<string>(null);
  private imageUploaded = new BehaviorSubject<any>(null);

  constructor() { }

  public openImage(image: any) {
    this.imageSetter.next(image);
  }

  public openImageNotifier(): Observable<any> {
    return this.imageSetter.asObservable();
  }

  public setViewerLoaded() {
    this.viewerLoaded.next(true);
  }

  public getViewerLoaded(): Observable<any> {
    return this.viewerLoaded.asObservable();
  }

 public getImageUploaded(): Observable<any> {
    return this.imageUploaded.asObservable();
  }

  public forward(){
    this.navigator.next('forward');
  }

  public backward(){
    this.navigator.next('backward');
  }

  public getNavigator(): Observable<any> {
    return this.navigator.asObservable();
  }

  public setApiInit() {
    this.apiInitialized.next(true);
  }

  public getApiInit(): Observable<boolean> {
    return this.apiInitialized.asObservable();
  }

  public uploadImage(data){

  }

  destroy() {
    this.apiInitialized = new BehaviorSubject<boolean>(false);
    this.viewerLoaded = new BehaviorSubject<any>(false);
    this.navigator = new BehaviorSubject<string>(null);
    this.imageUploaded = new BehaviorSubject<any>(null);
  }
}
