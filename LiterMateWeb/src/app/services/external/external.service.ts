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
