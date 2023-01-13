/*
 * Copyright (c) 2015-2022, Patricia Maraver
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import { Injectable } from '@angular/core';
import {Observable} from 'rxjs';
import {ArticleData} from '../model/article-data';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private url = environment.apiUrl + '/search';

  constructor(private http: HttpClient) { }

  findByDOI(doi: string): Observable<ArticleData> {
    return this.http.get<ArticleData>(`${this.url}/crossref?doi=${doi}`);
  }

  // findByPMID(pmid: string, db: string): Observable<ArticleData> {
  //   return this.http.get<ArticleData>(`${this.url}/pubmed?pmid=${pmid}&db=${db}`);
  // }
}
