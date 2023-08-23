import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ErrorComponent} from 'src/app/core/error.component';
import {DashboardModule} from 'src/app/dashboard/dashboard.module';
import {DashboardComponent} from 'src/app/dashboard/dashboard.component';
import {GraphComponent} from 'src/app/shared/graph/graph.component';
import {TimeseriesComponent} from 'src/app/timeseries/timeseries.component';
import {ChartComponent} from 'src/app/shared/chart/chart.component';
import {TrackingComponent} from './tracking/tracking.component';
import {LoginComponent} from 'src/app/user/login/login.component';
import {RegisterComponent} from 'src/app/user/register/register.component';
import {RouteGuard} from './core/route-guard.guard';
import {PlaylistResolver} from './shared/playlist.resolver';
import {VideoResolver} from './shared/video.resolver';
import {CategoryResolver} from './shared/category.resolver';
import {TrackingResolver} from './shared/tracking.resolver';
import {StatisticsResolver} from './dashboard/statistics.resolver';
import {VideoNumberResolver} from './dashboard/video-number.resolver';


const routes: Routes = [
  {
    path: 'dashboard', component: DashboardComponent, resolve:
    {
      stats: StatisticsResolver,
      videoNumber: VideoNumberResolver
    }
  },
  {
    path: 'graph', component: GraphComponent, canActivate: [RouteGuard], resolve:
    {
      playlists: PlaylistResolver,
      videos: VideoResolver,
      categories: CategoryResolver
    }
  },
  {
    path: 'timeseries', component: TimeseriesComponent, canActivate: [RouteGuard], resolve:
    {
      tracking: TrackingResolver
    }
  },
  {
    path: 'tracking', component: TrackingComponent, canActivate: [RouteGuard], resolve:
    {
      playlists: PlaylistResolver,
      videos: VideoResolver,
      tracking: TrackingResolver
    }
  },
  {
    path: 'chart', component: ChartComponent, canActivate: [RouteGuard], resolve:
    {
      playlists: PlaylistResolver,
      videos: VideoResolver
    }
  },
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {path: '**', component: ErrorComponent}
];

@NgModule({
  imports: [
    DashboardModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
