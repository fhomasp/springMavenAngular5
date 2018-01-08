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
      .catch((error: any) => Observable.throw(error.json().error || 'Server error' ));
  }

}
