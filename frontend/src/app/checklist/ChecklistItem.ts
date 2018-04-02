export class ChecklistItem {

  bulletName: string;
  taken: boolean;
  checklistId: number;


  constructor(bulletName: string, taken: boolean, checklistId: number) {
    this.bulletName = bulletName;
    this.taken = taken;
    this.checklistId = checklistId;
  }

}
