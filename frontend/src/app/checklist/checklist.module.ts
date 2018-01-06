import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ChecklistRoutingModule } from './checklist-routing.module';
import { ChecklistOverviewComponent } from './checklist-overview/checklist-overview.component';
import { ChecklistCreateComponent } from './checklist-create/checklist-create.component';

@NgModule({
  imports: [
    CommonModule,
    ChecklistRoutingModule
  ],
  declarations: [ChecklistOverviewComponent, ChecklistCreateComponent]
})
export class ChecklistModule { }
