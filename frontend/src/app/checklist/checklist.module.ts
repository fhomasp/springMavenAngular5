import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ChecklistRoutingModule } from './checklist-routing.module';
import { ChecklistOverviewComponent } from './checklist-overview/checklist-overview.component';
import { ChecklistCreateComponent } from './checklist-create/checklist-create.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  imports: [
    CommonModule,
    ChecklistRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgbModule
  ],
  declarations: [ChecklistOverviewComponent, ChecklistCreateComponent]
})
export class ChecklistModule { }
