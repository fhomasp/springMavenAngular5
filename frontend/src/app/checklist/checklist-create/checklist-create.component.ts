import {Component, Injectable, OnDestroy, OnInit} from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ChecklistService } from '../checklist.service';
import { Checklist } from '../Checklist';
import { ActivatedRoute, Router } from '@angular/router';
import {NgbDateParserFormatter, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {isNumber, padNumber, toInteger} from '@ng-bootstrap/ng-bootstrap/util/util';


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
  dateModel: NgbDateStruct;

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
      targetDateManual: new FormControl('', [
        Validators.required

      ])
    });
  // Validators.pattern('(0[1-9]|1[0-9]|2[0-9]|3[01])/(0[1-9]|1[012])/[0-9]{4}')

    if (this.creationDatestamp) {
      this.checklistService.findByCreationDateStamp(this.creationDatestamp).subscribe(
        checklist => {

          const targetDate: Date = new Date(checklist.targetDatestamp);
          const targetDateStruct: NgbDateStruct = {day: targetDate.getDate(), month: targetDate.getMonth() + 1,
            year: targetDate.getFullYear()};
          this.creationDatestamp = checklist.creationDatestamp;
          this.checklistForm.patchValue({
            title: checklist.title,
            targetDateManual: targetDateStruct
          });
        }, error => {
          console.log(error);
        }
      );
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
           new Date(this.dateModel.year, this.dateModel.month - 1, this.dateModel.day).getTime()
         );
         this.checklistService.updateCheckList(checklist).subscribe();
      }else {
        // this.creationDatestamp = Date.now();
        const checklist: Checklist = new Checklist(
          null,
          this.checklistForm.controls['title'].value,
          new Date(this.dateModel.year, this.dateModel.month - 1, this.dateModel.day).getTime()
        );
        this.checklistService.saveChecklist(checklist).subscribe();
      }

      this.checklistForm.reset();
      this.router.navigate(['/checklist']);
    }
  }

  redirectChecklistPage() {
    this.router.navigate(['/checklist']);
  }
}
