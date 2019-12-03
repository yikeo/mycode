import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../shared/shared.module';

import { HomeComponent } from './home/home.component';
import { ConfigComponent } from './config/config.component';
import { PropertyComponent } from './property/property.component';
import { QueryComponent } from './query/query.component';
import { PageRoutingModule } from './page-routing.module';
import { AgGridModule } from '@ag-grid-community/angular';

import {A11yModule} from '@angular/cdk/a11y';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {PortalModule} from '@angular/cdk/portal';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {CdkStepperModule} from '@angular/cdk/stepper';
import {CdkTableModule} from '@angular/cdk/table';
import {CdkTreeModule} from '@angular/cdk/tree';


@NgModule({
  declarations: [HomeComponent, ConfigComponent, PropertyComponent, QueryComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SharedModule,
    PageRoutingModule,
    A11yModule,
    CdkStepperModule,
    CdkTableModule,
    CdkTreeModule,
    DragDropModule,
    PortalModule,
    ScrollingModule,
    AgGridModule.withComponents([])
  ],
  exports: [HomeComponent, ConfigComponent, PropertyComponent, QueryComponent]
})
export class PageModule { }
