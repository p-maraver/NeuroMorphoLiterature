import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Article} from '../model/article';
import {ArticlePage} from '../model/article-page';
import {Contact} from '../../../agenda/model/contact';


@Injectable({
  providedIn: 'root'
})
export class ArticlesService {

  // private url = 'http://localhost:8188';
  private url = 'http://129.174.10.65:8189/articles';

  constructor(private http: HttpClient) {
  }

  findAllByText(collection: string, others: string): Observable<ArticlePage> {
    return this.http.get<ArticlePage>(
      `${this.url}/status/${collection}?${others}`);
  }

  async findAsyncAllByText(collection: string, others: string): Promise<ArticlePage> {
    return this.http.get<ArticlePage>(
      `${this.url}/status/${collection}?${others}`).toPromise();
  }

  findAllByEmail(collection: string, others: string): Observable<ArticlePage> {
    return this.http.get<ArticlePage>(
      `${this.url}/status/${collection}?${others}`);
  }

  findById(id: string): Observable<Article> {
    return this.http.get<Article>(`${this.url}/${id}`);
  }

  update(id: string, field: string, object: any): Observable<any> {
    return this.http.put(`${this.url}/${id}/${field}`, object);
  }

  updateContactsId(contact: Contact): Observable<any> {
    return this.http.put(`${this.url}/author`, contact);
  }

  add(id: string, article: Article): Observable<Article> {
    return this.http.post<Article>(`${this.url}/${id}/`, article);
  }

  delete(id: string): Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }

  getReconstructionsStatusValues(): Observable<string[]> {
    return this.http.get<string[]>(`${this.url}/reconstructions/specificDetails`);
  }

  updateCollection(id: string, newCollection: string, details: string): Observable<any> {
    let value = '';
    if (details != null) {
      value = `&specificDetails=${details}`;
    }
    return this.http.put<any>(`${this.url}/status/${id}?newArticleStatus=${newCollection}` + value, null);
  }

  update2NextStatus(id: string, reconstructionsStatus: string): Observable<any> {
    return this.http.put<any>(`${this.url}/reconstructions/${id}/${reconstructionsStatus}`, null);
  }

  saveArticle(article: Article): Observable<any> {
    return this.http.post<any>(`${this.url}`, article);
  }

  count(): Observable<any> {
    return this.http.get<any[]>(`${this.url}/count?`);
  }

  countStatusDetails(expired: boolean): Observable<any> {
    return this.http.get<any[]>(`${this.url}/reconstructions/count?expired=${expired}`);
  }

  countUsage(): Observable<any> {
    return this.http.get<any[]>(`${this.url}/count/usage`);
  }
}
