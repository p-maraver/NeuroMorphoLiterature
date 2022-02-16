import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {ArticleData} from '../model/article-data';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
   private url = 'http://129.174.10.65:8189/search';

  // private url = 'http://localhost:8187';
  constructor(private http: HttpClient) { }

  findByDOI(doi: string): Observable<ArticleData> {
    return this.http.get<ArticleData>(`${this.url}/crossref?doi=${doi}`);
  }

  findByPMID(pmid: string, db: string): Observable<ArticleData> {
    return this.http.get<ArticleData>(`${this.url}/pubmed?pmid=${pmid}&db=${db}`);
  }
}
