import { Injectable } from '@angular/core';
import {BehaviorSubject, EMPTY, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {CommandService} from "../services/command.service";
import {User} from "./user";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  public currentUserSubject: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(private command: CommandService) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public autoLogin(params) {
    return this.command.execute('auto_login', 'POST', 'json', params, true)
      .pipe(map(user => {
        user['isProlific'] = true;
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  public login(params) {
    return this.command.execute('login', 'POST', 'json', params, true)
      .pipe(map(user => {
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
        return user;
      }));
  }

  public logout() {
    if(this.currentUserValue) {
      let username = this.currentUserValue.username;
      return this.command.execute('logout', 'POST', 'json', username, true)
        .pipe(map(resp => {
          localStorage.removeItem('currentUser');
          this.currentUserSubject.next(null);
          return EMPTY;
        }));
    }else
      return EMPTY;
  }

  public removeUserData(): any{
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    return EMPTY;
  }

  public get authState(): Observable<User> {
    return this.currentUser;
  }

  public get currentUserValue(): User {
    return this.currentUserSubject.value;
  }

  public updateValue(user) {
    this.currentUserSubject.next(user);
  }

}
