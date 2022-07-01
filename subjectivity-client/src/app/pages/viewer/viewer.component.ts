import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {combineLatest, delay, firstValueFrom, Observable, of, Subscription, switchMap, take} from "rxjs";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {CommandService} from "../../services/command.service";
import {DataService} from "../../services/data.service";
import {MatDialog} from "@angular/material/dialog";
import {Location} from "../../map/location";
import {MapUtilService} from "../../map/map-util.service";
import {ImageryService} from "../../imagery/imagery.service";
import {SessionService} from "../../session/session.service";
import {Log} from "../../session/log";
import {Rate} from "../../rate/rate";
import {AuthenticationService} from "../../authentication/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-viewer',
  templateUrl: './viewer.component.html',
  styleUrls: ['./viewer.component.scss']
})
export class ViewerComponent implements OnInit, OnDestroy {


  id: any;
  pathId: any;
  logId: any;
  imageId: any;
  direction: number;
  pitch: number;
  pov: number;
  initialized: boolean = false;
  appLoaded: boolean = false;
  viewerLoaded: boolean = false;
  isEventRegistered: boolean = false;


  private subscriptions: Subscription[] = [];

  @ViewChild('parentElement', {static: true}) imageContainerParent;


  constructor(private router: Router, private route: ActivatedRoute, public dataService: DataService,
              private command: CommandService, public dialog: MatDialog, private mapService: MapUtilService,
              private imageryService: ImageryService, private sessionService: SessionService,
              public authService: AuthenticationService, private snackBar: MatSnackBar) {
    document.onreadystatechange =  () => {
      console.log(document.readyState);
    }
    this.subscriptions.push(this.route.queryParams.subscribe(params => {
      if (params['id'] && params['id'] != '')
        this.imageId = params['id'];
      if (params['yaw'] && params['yaw'] != '')
        this.direction = params['yaw'];
      if (params['pitch'] && params['pitch'] != '')
        this.pitch = params['pitch'];
      if (params['pov'] && params['pov'] != '')
        this.pov = params['pov'];
      if (params['image_id'] && params['image_id'] != '')
        this.id = params['image_id'];
      if (params['path_id'] && params['path_id'] != '')
        this.pathId = params['path_id'];
      if (params['log_id'] && params['log_id'] != '')
        this.logId = params['log_id'];
      if (!this.isEventRegistered)
        this.registerEvents();
    }));
  }

  ngOnInit() {

  }

  registerEvents() {
    this.subscriptions.push(this.imageryService.getApiInit().subscribe((flag) => {
      if (flag) {
        this.imageryService.openImage({
          imageId: this.imageId,
          direction: Number(this.direction),
          pitch: Number(this.pitch),
          pov: Number(this.pov)
        });
      }
      this.initialized = true;
    }));
    this.subscriptions.push(this.imageryService.getViewerLoaded().subscribe((flag) => {
      this.viewerLoaded = flag;
      this.appLoaded = true;
      this.viewChange();
    }));
    this.isEventRegistered = true;
  }

  initialize(flag) {
    if (flag != null && flag.length > 0) {
      this.initialized = true;
    }
  }

  async viewChange() {
    let currentImage;
    let name = this.pathId + "_" + this.id + "_" + this.logId;
    const result = await firstValueFrom(this.getImageFile(name).pipe(delay(1500), take(1), switchMap((file) => {
      const queryParams: Params = { status: 'done' };
      this.router.navigate(
        [],
        {
          relativeTo: this.route.parent,
          queryParams: queryParams,
          queryParamsHandling: 'merge', // remove to replace all query params by provided
        });

      let formData = new FormData();
      currentImage = file;
      formData.append('file', file, name);
      return this.command.execute('image/upload', 'POST', 'json', formData, true);
    })));
  }

  private getImageFile(id: string): Observable<any>{
    return new Observable(observer => {
      let canvas = document.getElementsByTagName('canvas');
      if(canvas != null && canvas.length > 0) {
        canvas[2].toBlob(blob => {
          let file = new File([blob], id + ".png", {type: "image/png"})
          observer.next(file);
          observer.complete();
        }, "image/png");
      }
    });
  }


  ngOnDestroy() {
    this.subscriptions.forEach(subscription => {
      subscription.unsubscribe();
    });
  }

}
