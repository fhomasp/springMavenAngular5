import { Component, OnInit } from '@angular/core';
import { Checklist } from '../Checklist';
import { ChecklistService } from '../checklist.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checklist-overview',
  templateUrl: './checklist-overview.component.html',
  styleUrls: ['./checklist-overview.component.css'],
  providers: [ChecklistService]
})
export class ChecklistOverviewComponent implements OnInit {

  checklists: Checklist[];

  constructor(private router: Router,
              private checklistService: ChecklistService) { }

  ngOnInit() {
    this.getAllChecklists();
  }

  getAllChecklists() {
    this.checklistService.findAll().subscribe(
        checklists => {
          this.checklists = checklists;
          // for (const checklist of checklists) {
          //
          // }
        },
      err => {
          console.log(err);
      }
    );
  }

  getCheckList(title: string) {
    this.checklistService.getCheckList(title).subscribe(
      checklists => {
        this.checklists.push(checklists);
    },
    err => {
        console.log(err);
    }
    );
  }

  redirectNewChecklistPage() {
    this.router.navigate(['/checklist/create']);
  }

  editChecklistPage(checklist: Checklist) {
    if (checklist) {
      this.router.navigate(['/checklist/edit', checklist.creationDatestamp]);
    }
  }

  deleteChecklist(checklist: Checklist) {
    console.log('Delete checklist '.concat(checklist.title));

    if (checklist) {
      this.checklistService.deleteCheckList(checklist.creationDatestamp).subscribe(
        res => {
          this.getAllChecklists();
          this.router.navigate(['/checklist']);
        }
      );
    }
  }

  convertTime(timestamp: number): Date {
    return new Date(timestamp);
  }
}
