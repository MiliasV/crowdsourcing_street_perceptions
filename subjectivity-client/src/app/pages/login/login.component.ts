import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, Validators} from "@angular/forms";

import {Subscription} from "rxjs";
import {AuthenticationService} from "../../authentication/authentication.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm;
  user: String;
  returnUrl: string;
  error: string;
  loginSubscription: Subscription;

  constructor(private formBuilder: FormBuilder, private router: Router,
              private route: ActivatedRoute, private internalAuthService: AuthenticationService) {
    if (this.internalAuthService.currentUserValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required ]]
    });
  };

  onUserLoggedIn(user) {
    this.user = user;
    if(user != null) {
      let payload = {};
      payload['authProvider'] = user.provider.toLowerCase();
      payload['token'] = user.authToken;
      this.internalAuthService.login(payload).subscribe(resp => {
        this.router.navigate([this.returnUrl]);
      }, err => {

      });
    }
  };

  signIn(): void {
    this.error = null;
    if (this.loginForm.valid) {
      let payload = {};
      payload['username'] = this.loginForm.controls.username.value;
      payload['password'] = this.loginForm.controls.password.value;
      this.internalAuthService.login(payload).subscribe(resp => {
        this.router.navigate([this.returnUrl]);
      }, err => {
        this.error = "Wrong username or password!!!";
      });
    } else {
      this.error = "Please enter username and password.";
      return;
    }
  };


  ngOnDestroy() {

  };

}
