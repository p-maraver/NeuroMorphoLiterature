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
import {Observable} from 'rxjs';
import {Email} from './email';
import {Article} from '../../article/details/model/article';
import {ContactPage} from '../../agenda/model/contact-page';
import {Contact} from '../../agenda/model/contact';
import {Config} from '../../agenda/model/config';


@Injectable({
  providedIn: 'root'
})
export class EmailService {
  private url = 'http://129.174.10.65:8189/emails';
  private urlArticles = 'http://129.174.10.65:8189/articles';

  // private url = 'http://localhost:8183';
  // private urlArticles = 'http://localhost:8188';

  constructor(private http: HttpClient) {
  }

  findByArticleId(id: string): Observable<Email[]> {
    return this.http.get<Email[]>(`${this.url}/${id}`);
  }

  generateEmail(id: string, article: Article, type: string, ancient: boolean, available: boolean): Observable<Email> {
    let statusDetails = article.reconstructions.reconstructionsList[0].statusDetails;
    if (statusDetails === 'To be requested') {
      if (ancient) {
        statusDetails = statusDetails + ' ancient';
      }
      if (available) {
        statusDetails = statusDetails + ' available';
      }
    }
    return this.http.post<Email>(`${this.url}/${id}?statusDetails=${statusDetails}&type=${type}`, article);
  }

  sendEmail(email: Email): Observable<any> {
    return this.http.post(`${this.url}/send`, email);
  }

  findTemplate(type: string): Observable<Email> {
    return this.http.get<Email>(`${this.url}/templates?type=${type}`);
  }

  updateTemplate(template: Email): Observable<any> {
    return this.http.put<any>(`${this.url}/templates/${template.id}`, template);
  }

  findEmailList(id: string): Observable<Email[]> {
    return this.http.get<Email[]>(`${this.url}/${id}`);
  }

  generateAndSendEmail(article: Article, type: string, ancient: boolean, available: boolean): Observable<Email> {
    let statusDetails = article.reconstructions.reconstructionsList[0].statusDetails;
    if (statusDetails === 'To be requested') {
      if (ancient) {
        statusDetails = statusDetails + ' ancient';
      }
      if (available) {
        statusDetails = statusDetails + ' available';
      }
    }
    return this.http.post<Email>(`${this.url}/generateandsend?statusDetails=${statusDetails}&type=${type}`, article);
  }

  findContactList(pageIndex: number, pageSize: number, text: string): Observable<ContactPage> {
    let textUrl = '';
    if (text !== null) {
      textUrl = `&text=${text}`;
    }
    return this.http.get<ContactPage>(`${this.url}/contacts?pageIndex=${pageIndex}&pageSize=${pageSize}${textUrl}`);
  }

  async findContact(id: string): Promise<Contact> {
    return await this.http.get<Contact>(`${this.url}/contacts/${id}`).toPromise();
  }

  findContactSync(id: string): Observable<Contact> {
    return this.http.get<Contact>(`${this.url}/contacts/${id}`);
  }

  createContact(contact: Contact): Observable<Contact> {
    return this.http.post<Contact>(`${this.url}/contacts/`, contact);
  }

  updateContact(id: string, contact: Contact): Observable<Contact> {
    return this.http.put<any>(`${this.url}/contacts/${id}`, contact);
  }

  // updateAuthor(contact: Contact): Observable<any> {
  //   return this.http.put<any>(`${this.urlArticles}/authors`, contact);
  // }

  reloadContacts(): Observable<any> {
    return this.http.put<any>(`${this.url}/contacts/`, null);
  }

  exportContacts(): Observable<any> {
    return this.http.put<any>(`${this.url}/contacts/export`, null);
  }

  async findConfig(): Promise<Config> {
    return await this.http.get<Config>(`${this.url}/config`).toPromise();
  }

  async updateConfig(config: Config): Promise<any> {
    return await this.http.put<any>(`${this.url}/config`, config).toPromise();
  }


}
