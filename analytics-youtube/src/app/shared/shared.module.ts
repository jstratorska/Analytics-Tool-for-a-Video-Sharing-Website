import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GraphComponent} from './graph/graph.component';
import {HttpClientModule} from '@angular/common/http';
import {ChartComponent} from './chart/chart.component';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [GraphComponent, ChartComponent],
  imports: [
    CommonModule,
    HttpClientModule,
    FormsModule
  ]
})
export class SharedModule { }
