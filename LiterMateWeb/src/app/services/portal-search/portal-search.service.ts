import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Portal} from './model/portal';
import {KeywordPage} from './model/keyword-page';
import {Keyword} from './model/keyword';


@Injectable({
  providedIn: 'root'
})
export class PortalSearchService {

   private url = 'http://129.174.10.65:8189/search';
   // private url = 'http://localhost:8187';

  constructor(private http: HttpClient) {
  }

  launchSearch(): Observable<any> {
    return this.http.get<any>(`${this.url}/start`);
  }

  findPortalList(): Observable<Portal[]> {
    return this.http.get<Portal[]>(`${this.url}/portals`);
  }

  updatePortal(portal: Portal): Observable<any> {
    return this.http.put<any>(`${this.url}/portals`, portal);
  }

  findKeywordList(page: number): Observable<KeywordPage> {
    return this.http.get<KeywordPage>(`${this.url}/keywords?page=${page}`);
  }

  deleteKeyword(id: string): Observable<any> {
    return this.http.delete<any>(`${this.url}/keywords/${id}`);
  }

  addKeyword(keyword: Keyword): Observable<any> {
    return this.http.post<any>(`${this.url}/keywords`, keyword);
  }
}
