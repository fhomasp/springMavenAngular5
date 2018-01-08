import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChecklistOverviewComponent} from './checklist-overview/checklist-overview.component';
import { ChecklistCreateComponent} from './checklist-create/checklist-create.component';

const routes: Routes = [
  {path: 'checklist', component: ChecklistOverviewComponent},
  {path: 'checklist/create', component: ChecklistCreateComponent},
  {path: 'checklist/edit/:creationdatestamp', component: ChecklistCreateComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ChecklistRoutingModule { }
