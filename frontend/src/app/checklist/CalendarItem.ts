export class CalendarItem {

  calendarId: number;
  title: string;
  description: string;
  startDate: Date;
  endDate: Date;


  constructor(calendarId: number, title: string, description: string, startDate: Date, endDate: Date) {
    this.calendarId = calendarId;
    this.title = title;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
