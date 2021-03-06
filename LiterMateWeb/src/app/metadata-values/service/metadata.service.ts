import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {MetadataPage} from '../model/metadata-page';
import {MetadataValue} from '../model/metadata-value';

@Injectable({
  providedIn: 'root'
})
export class MetadataService {
   private url_metadata = 'http://129.174.10.65:8189/metadata';
   private url_articles = 'http://129.174.10.65:8189/articles';

  // private url_articles = 'http://localhost:8188';
  // private url_metadata = 'http://localhost:8180';


  constructor(private http: HttpClient) {
  }

  getValuesByKey(key: string, page: number, sortProperty: string, sortDirection: string): Observable<MetadataPage> {
    return this.http.get<MetadataPage>(
      `${this.url_metadata}/values?type=${key}&page=${page}&sortDirection=${sortDirection}&sortProperty=${sortProperty}`);
  }

  getReviewedValuesByKey(key: string): Observable<string[]> {
    return this.http.get<string[]>( `${this.url_metadata}/reviewed?type=${key}`);
  }

  updateValuesByKey(key: string, valueList: string[]): Observable<any> {
    return this.http.put<any>(`${this.url_metadata}/names/${key}`, valueList);
  }

  update(id: string, metadata: MetadataValue): Observable<any> {
    return this.http.put<any>(`${this.url_metadata}/${id}`, metadata);
  }

  getKeys(): Observable<string[]> {
    return this.http.get<string[]>(`${this.url_metadata}/keys`);
  }

  remove(id: string): Observable<string[]> {
    return this.http.delete<any>(`${this.url_metadata}/${id}`);
  }

  getArticleMetadataDistinctValues(key: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.url_articles}/metadata?key=${key}`);
  }

  updateArticleMetadataValues(key: string, oldValue: string, newValue: string[]): Observable<any> {
    return this.http.put<any>(`${this.url_articles}/metadata?key=${key}&oldValue=${oldValue}`, newValue);
  }



}
