import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {firstValueFrom, Observable, of, Subscription, switchMap, take} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {CommandService} from "../../services/command.service";
import {DataService} from "../../services/data.service";
import {MatDialog} from "@angular/material/dialog";
import {Location} from "../../map/location";
import {MapUtilService} from "../../map/map-util.service";
import {ImageryService} from "../../imagery/imagery.service";
import {SessionService} from "../../session/session.service";
import {Log} from "../../session/log";
import {SimilarityDialogComponent} from "../../dialog/similarity-dialog/similarity-dialog.component";
import {MessageDialogComponent} from "../../dialog/message-dialog/message-dialog.component";
import {Rate} from "../../rate/rate";
import {ReasonDialogComponent} from "../../dialog/reason-dialog/reason-dialog.component";
import {AuthenticationService} from "../../authentication/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";


@Component({
  selector: 'app-street',
  templateUrl: './street.component.html',
  styleUrls: ['./street.component.scss']
})
export class StreetComponent implements OnInit, OnDestroy {

  id: number;
  width: number;
  imageId: number;
  pathId: any;
  rate: Rate = {};
  location: Location;
  breakpoint: number;
  initialized: boolean = false;
  appLoaded: boolean = false;
  mapLoaded: boolean = false;
  viewerLoaded: boolean = false;
  taskFinished: boolean = false;
  submitActive: boolean = false;

  private sessionId: string;
  private studyId: string;
  private workerId: string;

  private subscriptions: Subscription[] = [];

  @ViewChild('parentElement', {static: true}) imageContainerParent;


  constructor(private router: Router, private route: ActivatedRoute, public dataService: DataService,
              private command: CommandService, public dialog: MatDialog, private mapService: MapUtilService,
              private imageryService: ImageryService, private sessionService: SessionService,
              public authService: AuthenticationService, private snackBar: MatSnackBar) {

    this.subscriptions.push(this.route.queryParams.subscribe(params => {
      if (params['id'] && params['id'] != '')
        this.pathId = params['id'];

      if (params['sid'] && params['sid'] != '') {
        this.sessionId = params['sid'];
        if (this.sessionId != null) {

        } else {

        }
      }

      if (params['eid'] && params['eid'] != '') {
        this.studyId = params['eid'];

      }

      if (params['pid'] && params['pid'] != '') {
        this.workerId = params['pid'];
        if (this.workerId != null) {

        } else {

        }
      }
    }));

    this.subscriptions.push(this.sessionService.getSession().subscribe(this.initialize.bind(this)));

    this.subscriptions.push(this.mapService.getMapLoaded().subscribe((flag) => {
      this.mapLoaded = flag;
      if (this.viewerLoaded)
        this.appLoaded = true;
    }));

    this.subscriptions.push(this.imageryService.getViewerLoaded().subscribe((flag) => {
      this.viewerLoaded = flag;
      if (this.mapLoaded)
        this.appLoaded = true;
    }));

    this.subscriptions.push(this.mapService.getCurrentImage().subscribe(this.imageChange.bind(this)));
    this.subscriptions.push(this.mapService.getCurrentView().subscribe(this.viewChange.bind(this)));
  }

  ngOnInit() {
    if (this.workerId != null && this.sessionId != null) {
      let payload = {};
      payload['username'] = this.workerId;
      payload['password'] = 'prolific';
      this.authService.autoLogin(payload).subscribe(resp => {
        this.sessionService.initSession();
      }, err => {

      });
    } else {
      this.sessionService.initSession();
    }
  }

  initialize(flag) {
    if (flag != null && flag.length > 0)
      this.initialized = true;
  }


  imageChange(image: any) {
    if (image) {
      this.resetRate();
      let log: Log = {};
      log.imageId = image.id;
      log.yaw = image.direction;
      log.pathId = image.pathId;
      log.lng = image.lng;
      log.lat = image.lat;
      this.sessionService.saveLog(log);
    }
  }

  viewChange(view: any) {
    if (view) {
      let image = this.mapService.currentImage;
      if (image) {
        //this.resetRate();
        let log: Log = {};
        log.imageId = image.id;
        log.pathId = image.pathId;
        log.lng = image.lng;
        log.lat = image.lat;
        log.yaw = view.yaw;
        log.pitch = view.pitch;
        log.pov = view.hFov;
        this.sessionService.saveLog(log);
      }
    }
  }

  private getImageFile(): Observable<any>{
    return new Observable(observer => {
      let canvas = document.getElementsByTagName('canvas');
      canvas[2].toBlob(blob => {
        let file = new File([blob], "current.jpg", {type: "image/jpeg"})
        observer.next(file);
        observer.complete();
      }, "image/jpeg");
    });
  }

  async saveScores(rate: Rate){
    let currentImage;
    const result = await firstValueFrom(this.getImageFile().pipe(take(1),switchMap((file) => {
      let formData = new FormData();
      currentImage = file;
      formData.append('file', file, "test");

      rate.sessionId = this.sessionService.session;
      rate.logId = this.sessionService.getLogId();
      rate.imageId = this.mapService.currentImage['id'];
      formData.append('rate', JSON.stringify(rate));
      return this.command.execute('image/score', 'POST', 'json', formData, true);
    })));
    this.showSimilarImages(result['rateId'], result['similars'], currentImage, rate);
  }


  private showSimilarImages(rateId, images: any[], currentImage, rate: Rate) {
    if (images && images.length > 0) {
      const dialogRef = this.dialog.open(SimilarityDialogComponent, {
        autoFocus: false,
        maxWidth: '1230px',
        width: '90%',
        disableClose: true,
        data: {
          image: images[0].image,
          current: currentImage,
          rate: rate
        }
      });
      dialogRef.afterClosed().subscribe((rate: Rate) => {
        this.resetRate();
        this.snackBar.open('Your scores are saved!  Please continue...', 'Dismiss', {
          horizontalPosition: 'center',
          verticalPosition: 'top',
          duration: 5000
        });
        if(rate) {
          rate.imageRateId = rateId;
          rate.sessionId = this.sessionService.session;
          rate.logId = this.sessionService.getLogId();
          rate.imageId = this.mapService.currentImage['id'];
          rate.imageName = images[0].image;
          this.command.execute('similar/score', 'POST', 'json', rate, true).subscribe(() => {
            this.imageryService.forward();
            console.log('similar saved...');
          },(err)=>{
            this.imageryService.forward();
          });
        }
      });
    }
  }

  onRateChange(rate: Rate) {
    this.rate = Object.assign({}, rate);
    if (this.rate != null) {
      if (this.rate.attractiveness >= 1 && this.rate.business >= 1 && this.rate.safety >= 1) {
        this.submitActive = true;
      } else
        this.submitActive = false;
    } else
      this.submitActive = false;
  }

  rateReload(rate: Rate) {
    this.rate = Object.assign({}, rate);
  }

  resetRate(){
    this.rate = {};
    this.onRateChange({});
  }

  endTask(){
    this.taskFinished = true;
  }

  submit() {
    const rated = this.mapService.getRatedCount();
    let isEven = (rated == 1 || rated == 3) || (rated > 4 && rated % 2 > 0);
    let openDialog = isEven ||
      (this.rate.attractiveness == 1 || this.rate.attractiveness == 5) ||
      (this.rate.safety == 1 || this.rate.safety == 5);
    if (openDialog) {
      const dialogRef = this.dialog.open(ReasonDialogComponent, {
        autoFocus: false,
        disableClose: true,
        data: {
          rate: this.rate,
          isEven: isEven
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if(result != null) {
          this.openReasonImageDialog(result);
        }
      });
    } else {
      this.openReasonImageDialog(this.rate);
    }
  }

  openReasonImageDialog(rate: Rate){
    let image = this.mapService.currentImage;
    if (image) {
      let orderId = Number(this.mapService.currentImage['orderId']);
      this.mapService.setImagePointFeature(orderId, 'rated', true);
    //  this.mapService.setImagePointFeature(orderId, 'rate', rate);
      this.saveScores(rate);
    }
  }

  finish() {
    const rated = this.mapService.getRatedCount();
    const unvisited = this.mapService.getUnvisitedCount();
    let mode = (rated >= 4 && unvisited == 0) ? 'end' : 'warning';
    let message = '';
    if (mode === 'warning') {
      if (unvisited > 0)
        message = 'visit';
      if (rated < 4)
        message = 'rate';
    }

    if (mode == 'warning') {
      this.openWarningDialog(message);
    } else {
      let data = {};
      data['sessionId'] = this.sessionService.session;
      this.command.execute('session/complete', 'POST', 'json', data, true).subscribe((response) => {
        mode = (response.counter >= 3 && response.redirect) ? 'finish' : 'end';
        this.openFinishDialog(mode, response.counter, response.redirect);
      }, () => {

      });
    }
  }

  openWarningDialog(message: any){
    const dialogRef = this.dialog.open(MessageDialogComponent, {
      autoFocus: false,
      disableClose: true,
      data: {
        mode: 'warning',
        message: message
      }
    });

    dialogRef.afterClosed().subscribe(result => {

    });
  }

  openFinishDialog(mode: any, counter: any, redirect?: any){
    const currentUser = this.authService.currentUserValue;
    const dialogRef = this.dialog.open(MessageDialogComponent, {
      autoFocus: false,
      disableClose: true,
      data: {
        prolific: currentUser['isProlific'],
        mode: mode,
        counter: counter
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result && result == 'next') {
        window.location.reload();
      } else if (result && result == 'finish'){
        window.location.href = redirect;
      } else if (result && result == 'close'){
        this.authService.logout().subscribe(resp => {
          this.router.navigate(['/login']);
        }, err => {
          this.router.navigate(['/login']);
        });
      }
    });
  }


  reset() {
    this.location = null;
    this.rate = null;
    this.appLoaded = false;
    this.mapLoaded = false;
    this.viewerLoaded = false;
    this.taskFinished = false;
    this.ngOnInit();
  }


  ngOnDestroy() {
    this.subscriptions.forEach(subscription => {
      subscription.unsubscribe();
    });
  }

}
