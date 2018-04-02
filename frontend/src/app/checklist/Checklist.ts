import {CalendarItem} from './CalendarItem';
import {ChecklistItem} from './ChecklistItem';

export class Checklist {

  creationDatestamp: number;
  title: string;
  calendarItem: CalendarItem;
  items: ChecklistItem[];

  constructor(creationDatestamp: number, title: string, calendarItem: CalendarItem) {
    this.creationDatestamp = creationDatestamp;
    this.title = title;
    this.calendarItem = calendarItem;
  }
}
