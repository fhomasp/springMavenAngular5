export class Checklist {

  creationDatestamp: number;
  title: string;
  targetDatestamp: number;


  constructor(creationDatestamp: number, title: string, targetDatestamp: number) {
    this.creationDatestamp = creationDatestamp;
    this.title = title;
    this.targetDatestamp = targetDatestamp;
  }
}
