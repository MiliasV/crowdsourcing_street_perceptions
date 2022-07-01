import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class StreetService {

  private forwardActive = new BehaviorSubject<boolean>(true);
  private submitActive = new BehaviorSubject<boolean>(false);
  private finishActive = new BehaviorSubject<boolean>(false);

  constructor() { }

  setForwardActive(flag: boolean){
    this.forwardActive.next(flag);
  }

  getForwardStatus(): Observable<any> {
    return this.forwardActive.asObservable();
  }

  setSubmitActive(flag: boolean){
    this.submitActive.next(flag);
  }

  getSubmitStatus(): Observable<any> {
    return this.forwardActive.asObservable();
  }

  setFinishActive(flag: boolean){
    this.finishActive.next(flag);
  }

  getFinishStatus(): Observable<any> {
    return this.forwardActive.asObservable();
  }

}
