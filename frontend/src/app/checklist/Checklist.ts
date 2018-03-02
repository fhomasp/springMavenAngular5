import {CalendarItem} from './CalendarItem';

export class Checklist {

  creationDatestamp: number;
  title: string;
  calendarItem: CalendarItem;

  constructor(creationDatestamp: number, title: string, calendarItem: CalendarItem) {
    this.creationDatestamp = creationDatestamp;
    this.title = title;
    this.calendarItem = calendarItem;
  }
}
