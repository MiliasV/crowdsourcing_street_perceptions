import { BrowserModule } from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MapComponent} from "./map/map.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {FlexLayoutModule} from "@angular/flex-layout";
import {MaterialModule} from "./material.module";
import {StreetComponent} from "./pages/street/street.component";
import {ImageryComponent} from "./imagery/imagery.component";
import {JwtInterceptor} from "./authentication/jwt.interceptor";
import {ErrorInterceptor} from "./authentication/error.interceptor";
import {LoginComponent} from "./pages/login/login.component";
import {MessageDialogComponent} from "./dialog/message-dialog/message-dialog.component";
import {SimilarityDialogComponent} from "./dialog/similarity-dialog/similarity-dialog.component";
import {LinkPipe} from "./utils/link.pipe";
import {RateComponent} from "./rate/rate.component";
import {ReasonDialogComponent} from "./dialog/reason-dialog/reason-dialog.component";
import {InstructionDialogComponent} from "./dialog/instruction-dialog/instruction-dialog.component";
import {ViewerComponent} from "./pages/viewer/viewer.component";



@NgModule({
    declarations: [
        AppComponent,
        StreetComponent,
        ViewerComponent,
        MapComponent,
        ImageryComponent,
        LoginComponent,
        MessageDialogComponent,
        ReasonDialogComponent,
        SimilarityDialogComponent,
        InstructionDialogComponent,
        LinkPipe,
        RateComponent
    ],
  imports: [
    FormsModule,
    HttpClientModule,
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    MaterialModule,
    FlexLayoutModule
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  providers: [
    LinkPipe,
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
  ],
  entryComponents: [MessageDialogComponent, ReasonDialogComponent, SimilarityDialogComponent, InstructionDialogComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
