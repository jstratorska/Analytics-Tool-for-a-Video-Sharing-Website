import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {DashboardModule} from './dashboard/dashboard.module';
import {SharedModule} from './shared/shared.module';
import {CoreModule} from './core/core.module';
import {UserModule} from './user/user.module';
import {TrackingComponent} from './tracking/tracking.component';

import {AuthInterceptor} from './core/auth-interceptor';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import { TimeseriesComponent } from './timeseries/timeseries.component';

@NgModule({
  declarations: [
    AppComponent,
    TrackingComponent,
    TimeseriesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    DashboardModule,
    SharedModule,
    CoreModule,
    UserModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
