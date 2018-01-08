import { Component, OnInit } from '@angular/core';
import { Checklist } from '../Checklist';
import { ChecklistService } from '../checklist.service';

@Component({
  selector: 'app-checklist-overview',
  templateUrl: './checklist-overview.component.html',
  styleUrls: ['./checklist-overview.component.css'],
  providers: [ChecklistService]
})
export class ChecklistOverviewComponent implements OnInit {

  checklists: Checklist[];

  constructor(private checklistService: ChecklistService) { }

  ngOnInit() {
    this.getAllUsers();
  }

  getAllUsers() {
    this.checklistService.findAll().subscribe(
        checklists => {
          this.checklists = checklists;
        },
      err => {
          console.log(err);
      }
    );
  }
}
