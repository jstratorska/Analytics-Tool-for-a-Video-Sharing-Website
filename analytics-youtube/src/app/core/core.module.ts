import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ErrorComponent} from './error.component';
import {NavbarComponent} from './navbar/navbar.component';
import {RouterModule} from '@angular/router';


@NgModule({
  declarations: [ErrorComponent, NavbarComponent],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [NavbarComponent]
})
export class CoreModule { }
