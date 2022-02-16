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

import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {FullText} from '../../article/full-text/model/full-text';

@Injectable({
  providedIn: 'root'
})
export class FullTextService {
     private url = 'http://129.174.10.65:8189/fulltext';
   //  private url = 'http://localhost:8182';

  constructor(private http: HttpClient) {
  }

  findFullText(id: string): Observable<FullText> {
    return this.http.get<FullText>(`${this.url}/${id}`);
  }

  saveFullText(id: string, rawText: string | ArrayBuffer): Observable<any> {
    return this.http.put<FullText>(`${this.url}/${id}`, {'rawText': rawText});
  }

  existsFullText(id: string): Observable<string> {
    return this.http.get<string>(`${this.url}/${id}/exists`);
  }

  updateFigureCategory(id: string,
                 field: string,
                 figure: Object): Observable<any> {
    return this.http.put<any>(`${this.url}/${id}/${field}`, figure);
  }

}
