import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {SessionService} from "./session/session.service";
import {AuthenticationService} from "./authentication/authentication.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {InstructionDialogComponent} from "./dialog/instruction-dialog/instruction-dialog.component";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AppComponent implements OnInit {

  public subs: Subscription[] = [];
  public isLoggedIn: boolean;
  public isProlific: boolean;

  constructor(public dialog: MatDialog, public sessionService: SessionService, private router: Router,
              public authService: AuthenticationService) {
    this.subs.push(authService.authState.subscribe(this.onAuthStateChanged.bind(this)));
    this.subs.push(sessionService.showInstruction.subscribe(this.onShowInstruction.bind(this)));
  }

  ngOnInit(): void {
    //this.sessionService.initSession();
  }

  signOut(): void {
    this.authService.logout().subscribe(resp => {
      this.router.navigate(['/login']);
    }, err => {
      this.router.navigate(['/login']);
    });
  }

  onAuthStateChanged(user) {
    this.isLoggedIn = (user != null);
    if (this.authService.currentUserValue) {
      const currentUser = this.authService.currentUserValue;
      this.isProlific = currentUser['isProlific'];
    } else
      this.isProlific = false;
  }

  onShowInstruction(flag){
    if(flag)
      this.openInstructionWindow();
  }

  openInstructionWindow() {
  //  let innerWidth = 900;
    let innerHeight = 600;
    if (window && window.innerWidth && window.innerHeight) {
      //innerWidth = Math.round(window.innerWidth * 0.6);
      innerHeight = Math.round(window.innerHeight * 0.7);
    }

    const dialogRef = this.dialog.open(InstructionDialogComponent, {
      autoFocus: false,
      disableClose: true,
      backdropClass: 'backdrop',
      data: {
        innerHeight: innerHeight
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  ngOnDestroy() {
    this.subs.forEach(sub => {
      if (sub) {
        sub.unsubscribe();
      }
    });
  }
}

