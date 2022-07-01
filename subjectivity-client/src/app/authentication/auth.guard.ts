import { Injectable } from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, ActivatedRoute} from '@angular/router';
import {AuthenticationService} from "./authentication.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private route: ActivatedRoute, private authenticationService: AuthenticationService) {

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const currentUser = this.authenticationService.currentUserValue;
    if (this.isUserValid(currentUser, route.queryParams)) {
      return true;
    } else {
      this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
      return false;
    }
  }

  isUserValid(currentUser: any, params: any) {
    if (currentUser) {
      if (currentUser['isProlific']) {
        if ((params['sid'] && params['sid'] != '') && (params['pid'] && params['pid'] != '') && currentUser.username == params['pid'])
          return true;
        else
          return false;
      } else {
        return true;
      }
    } else {
      if ((params['sid'] && params['sid'] != '') && (params['pid'] && params['pid'] != ''))
        return true;
      else
        return false;
    }
  }

}
