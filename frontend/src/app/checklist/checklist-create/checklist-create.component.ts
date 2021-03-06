import {Component, Injectable, OnDestroy, OnInit} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ChecklistService } from '../checklist.service';
import { Checklist } from '../Checklist';
import { ActivatedRoute, Router } from '@angular/router';
import {NgbDateParserFormatter, NgbDateStruct, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import {isNumber, padNumber, toInteger} from '@ng-bootstrap/ng-bootstrap/util/util';
import {ChecklistItem} from '../ChecklistItem';


@Injectable()
export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  parse(value: string): NgbDateStruct {
    if (value) {
      const dateParts = value.trim().split('/');
      if (dateParts.length === 3 && isNumber(dateParts[0]) && isNumber(dateParts[1]) && isNumber(dateParts[2])
        && toInteger(dateParts[2]) >= 2010) {
        return {day: toInteger(dateParts[0]), month: toInteger(dateParts[1]), year: toInteger(dateParts[2])};
      }
    }
    return null;
  }

  format(date: NgbDateStruct): string {
    return date ?
      `${isNumber(date.day) ? padNumber(date.day) : ''}/${isNumber(date.month) ? padNumber(date.month) : ''}/${date.year}` :
      '';
  }
}


@Component({
  selector: 'app-checklist-create',
  templateUrl: './checklist-create.component.html',
  styleUrls: ['./checklist-create.component.css'],
  providers: [{provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter}, ChecklistService]
})
export class ChecklistCreateComponent implements OnInit, OnDestroy {

  creationDatestamp: number;
  checklist: Checklist;

  checklistForm: FormGroup;
  dateModelStartDate: NgbDateStruct;
  targetStartTime: NgbTimeStruct;
  dateModelEndDate: NgbDateStruct;
  targetEndTime: NgbTimeStruct;

  private sub: any;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private checklistService: ChecklistService) { }

  ngOnInit() {

    this.sub = this.route.params.subscribe(params => {
       this.creationDatestamp = params['creationdatestamp'];
    });

    this.checklistForm = new FormGroup({
      title: new FormControl('', Validators.required),
      targetStartDateManual: new FormControl('', [
        Validators.required

      ]),
      targetEndDateManual: new FormControl('', [
        Validators.required

      ])
    });

    if (this.creationDatestamp) {
      this.checklistService.findByCreationDateStamp(this.creationDatestamp).subscribe(
        checklist => {

          const targetStartDate: Date = new Date(checklist.calendarItem.startDate);
          const targetStartDateStruct: NgbDateStruct = {day: targetStartDate.getDate(), month: targetStartDate.getMonth() + 1,
            year: targetStartDate.getFullYear()};
          this.targetStartTime = {hour: targetStartDate.getHours(), minute: targetStartDate.getMinutes(), second: null};

          const targetEndDate: Date = new Date(checklist.calendarItem.endDate);
          const targetEndDateStruct: NgbDateStruct = {day: targetEndDate.getDate(), month: targetEndDate.getMonth() + 1,
            year: targetEndDate.getFullYear()};
          this.targetEndTime = {hour: targetEndDate.getHours(), minute: targetEndDate.getMinutes(), second: null};

          this.creationDatestamp = checklist.creationDatestamp;
          this.checklistForm.patchValue({
            title: checklist.title,
            targetStartDateManual: targetStartDateStruct,
            targetEndDateManual: targetEndDateStruct
          });
          this.checklist = checklist;
        }, error => {
          console.log(error);
        }
      );
    } else {
      this.targetStartTime = {hour: 0, minute: 0, second: null};
      this.targetEndTime = {hour: 0, minute: 0, second: null};
      this.checklist = new Checklist(null, null, null);
      this.checklist.items = [];
    }

  }


  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  onSubmit() {
    if (this.checklistForm.valid) {

      if (this.creationDatestamp) {
         const checklist: Checklist = new Checklist(this.creationDatestamp,
           this.checklistForm.controls['title'].value,
           { calendarId: null, title: null, description: null,
             startDate: new Date(this.dateModelStartDate.year, this.dateModelStartDate.month - 1, this.dateModelStartDate.day,
               this.targetStartTime.hour, this.targetStartTime.minute),
             endDate: new Date(this.dateModelEndDate.year, this.dateModelEndDate.month - 1, this.dateModelEndDate.day,
               this.targetEndTime.hour, this.targetEndTime.minute)
           }
         );
         checklist.items = this.checklist.items;
         this.checklistService.updateCheckList(checklist).subscribe(
           updatedChecklist => this.checklist = checklist,
           observedErr => { throw observedErr; },
           () => this.router.navigate(['/checklist'])
         );
      }else {
        // this.creationDatestamp = Date.now();
        const checklist: Checklist = new Checklist(
          null,
          this.checklistForm.controls['title'].value,
          { calendarId: null, title: null, description: null,
            startDate: new Date(this.dateModelStartDate.year, this.dateModelStartDate.month - 1, this.dateModelStartDate.day,
              this.targetStartTime.hour, this.targetStartTime.minute),
            endDate: new Date(this.dateModelEndDate.year, this.dateModelEndDate.month - 1, this.dateModelEndDate.day,
              this.targetEndTime.hour, this.targetEndTime.minute)
          }
        );
        checklist.items = this.checklist.items;
        this.checklistService.saveChecklist(checklist).subscribe(
          savedChecklist => this.checklist = checklist,
          observedErr => { throw observedErr; },
          () => this.router.navigate(['/checklist'])
        );
      }

      this.checklistForm.reset();
      // this.router.navigate(['/checklist']);
    }
  }

  addItem() {
      this.checklist.items.push( { bulletName: '', taken: false, checklistId: null } );
  }

  deleteItem(item: ChecklistItem) {
    const index: number = this.checklist.items.indexOf(item);
    if (index > -1) {
      this.checklist.items.splice(index, 1);
    }
  }

  redirectChecklistPage() {
    this.router.navigate(['/checklist']);
  }
}
