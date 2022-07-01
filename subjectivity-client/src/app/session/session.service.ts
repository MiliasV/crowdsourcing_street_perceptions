import { Injectable } from '@angular/core';
import {CommandService} from "../services/command.service";
import {BehaviorSubject, Observable} from "rxjs";
import {Log} from "./log";
import {AuthenticationService} from "../authentication/authentication.service";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  showInstruction = new BehaviorSubject<boolean>(false);
  sessionId = new BehaviorSubject<string>(null);
  logId: any = null;

  constructor(private commandService: CommandService, public authService: AuthenticationService,
              private router: Router, private snackBar: MatSnackBar) {
  }

  public initSession() {
    let data = {};
    data['width'] = window.innerWidth;
    data['height'] = window.innerHeight;
    if(this.authService.currentUserValue){
      data['pid'] = this.authService.currentUserValue.username;
    }
    this.commandService.execute('session/init', 'POST', 'json', data, true).subscribe((response) => {
      if(response.invalid){
        this.authService.logout().subscribe(resp => {
          this.router.navigate(['/login']);
        }, err => {
          this.router.navigate(['/login']);
        });
        this.snackBar.open('You have finished all of your available tasks', 'Dismiss', {
          horizontalPosition: 'center',
          verticalPosition: 'bottom',
          duration: 5000
        });
      }else {
        this.sessionId.next(response.sessionId);
        if (response.instruction) {
          this.showInstruction.next(true);
        }
      }
    },()=>{
      console.log('Here');
      this.router.navigate(['/login']);
    });
  }

  public saveLog(log: Log) {
    log.sessionId = this.session;
    this.commandService.execute('log/save', 'POST', 'json', log, true).subscribe((response) => {
      console.log(response);
      this.logId = response.logId;
    });
  }

  public getLogId(): number {
    return Number(this.logId);
  }

  public get session() {
    return this.sessionId.value;
  }

  public getSession(): Observable<string> {
    return this.sessionId.asObservable();
  }
}
