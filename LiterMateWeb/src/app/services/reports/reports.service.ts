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
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {
  private url = environment.apiUrl + '/articles';

  constructor(private http: HttpClient) {
  }

  generate(status: string): Observable<any> {
    return this.http.get<any>(`${this.url}/csv?status=${status}&text=true`);
  }

  delete(status: string): Observable<any> {
    return this.http.delete<any>(`${this.url}/csv?status=${status}`);
  }

  exists(status: string): Observable<any> {
    return this.http.get<any>(`./assets/${status}.csv`);
  }

}
