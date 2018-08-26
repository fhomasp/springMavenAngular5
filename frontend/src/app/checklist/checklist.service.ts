import { Injectable } from '@angular/core';
import { Checklist } from './Checklist';
import { Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/throw';
import {KeycloakHttp} from '../keycloak/keycloak.http';

 // TODO: Http appears to be deprecated.  According to https://auth0.com/blog/whats-new-in-angular5/ we should use HttpClient

@Injectable()
export class ChecklistService {

  private apiUrl = 'http://localhost:8080/overview';


  constructor(private keycloakHttp: KeycloakHttp ) { }

  findAll(): Observable<Checklist[]> {
    return this.keycloakHttp.get(this.apiUrl)
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'FindAll error' ));
  }

  getCheckList(title: string): Observable<Checklist> {
    return this.keycloakHttp.get(this.apiUrl.concat('/find/'.concat(title)))
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'Find Checklist error'));
  }

  saveChecklist(checklist: Checklist): Observable<Checklist> {
    return this.keycloakHttp.post(this.apiUrl.concat('/write'), checklist)
      .catch((error: any) => Observable.throw(error.json().error || 'Save Checklist error'));
  }

  findByCreationDateStamp(creationDatestamp: number): Observable<Checklist> {
    return this.keycloakHttp.get(this.apiUrl.concat('/') + creationDatestamp)
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'FindByCDS Checklist error'));
  }

  updateCheckList(checklist: Checklist): Observable<Checklist> {
    return this.keycloakHttp.put(this.apiUrl.concat('/write'), checklist)
      .map((res: Response) => res.json())
      .catch((error: any) => Observable.throw(error.json().error || 'update checklist error'));
  }

  deleteCheckList(creationDatestamp: number): Observable<number> {
      return this.keycloakHttp.delete(this.apiUrl.concat('/write') + '/' + creationDatestamp)
        .map((res: Response) => res.json())
        .catch((error: any) => Observable.throw(error.json().error || 'delete checklist error'));
  }

}
