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
import {HttpClient} from '@angular/common/http';
import {Classifier} from './classifier';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ClassifierService {

  private url = environment.apiUrl + '/evaluate';

  constructor(private http: HttpClient) {
  }

  async findAll(): Promise<Classifier[]> {
    return await this.http.get<Classifier[]>(`${this.url}/classifier`).toPromise();
  }

  async train(version: string): Promise<void> {
    return await this.http.post<void>(`${this.url}/classifier/train?version=${version}`, null).toPromise();
  }

  async update(id: string, field: string, object: any): Promise<void> {
    return await this.http.put<void>(`${this.url}/classifier/${id}`, {[field]: object}).toPromise();
  }

  async readFile(name: string): Promise<any> {
    return await this.http.get<string>(`${this.url}/classifier/file?name=${name}`).toPromise();
  }

  async updateFile(name: string, content: string): Promise<void> {
    return await this.http.put<void>(`${this.url}/classifier/file?name=${name}`, content).toPromise();
  }

}
