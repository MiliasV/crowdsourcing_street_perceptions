import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {StreetComponent} from "./pages/street/street.component";
import {AuthGuard} from "./authentication/auth.guard";
import {LoginComponent} from "./pages/login/login.component";
import {ViewerComponent} from "./pages/viewer/viewer.component";


const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'street', component: StreetComponent, canActivate: [AuthGuard] },
  { path: 'street/:id', component: StreetComponent, canActivate: [AuthGuard] },
  { path: 'view', component: ViewerComponent },
  { path: '', redirectTo: '/street', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
