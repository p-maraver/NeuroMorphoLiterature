import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Article} from '../../article/details/model/article';
import {Literature} from '../reports/literature';
import {FullText} from '../../article/full-text/model/full-text';

@Injectable({
  providedIn: 'root'
})
export class ExternalService {

  private url = 'http://cng-nmo-dev6.orc.gmu.edu/litermate-collect';

  constructor(private http: HttpClient) {
  }

  sendData(article: Article, fulltext: FullText): Observable<any> {
    const literature = new Literature(article, fulltext);
    console.log(literature);
    return this.http.post<any>(`${this.url}`, literature);
  }



}
