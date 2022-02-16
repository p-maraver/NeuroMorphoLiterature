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
