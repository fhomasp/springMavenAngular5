import { Injectable } from '@angular/core';
import { Checklist } from './Checklist';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';

 // TODO: Http appears to be deprecated.  According to https://auth0.com/blog/whats-new-in-angular5/ we should use HttpClient

@Injectable()
export class ChecklistService {

  private apiUrl = 'http://localhost:8080/overview';

  constructor(private http: Http) { }


  findAll(): Observable<Checklist[]> {
    return this.http.get(this.apiUrl)
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'FindAll error' ));
  }

  getCheckList(title: string): Observable<Checklist> {
    return this.http.get(this.apiUrl.concat('/find/'.concat(title)))
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'Find Checklist error'));
  }

  saveChecklist(checklist: Checklist): Observable<Checklist> {
    return this.http.post(this.apiUrl, checklist)
      .catch((error: any) => Observable.throw(error.json().error || 'Save Checklist error'));

  }

  findByCreationDateStamp(creationDatestamp: number): Observable<Checklist> {
    return this.http.get(this.apiUrl.concat('/') + creationDatestamp)
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'FindByCDS Checklist error'));
  }

  updateCheckList(checklist: Checklist): Observable<Checklist> {
    return this.http.put(this.apiUrl, checklist)
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'update checklist error'));
  }

  deleteCheckList(creationDatestamp: number): Observable<number> {
      return this.http.delete(this.apiUrl + '/' + creationDatestamp)
        .map((res: Response) => res.json())
        .catch((error: any) => Observable.throw(error.json().error || 'delete checklist error'));
  }

}
