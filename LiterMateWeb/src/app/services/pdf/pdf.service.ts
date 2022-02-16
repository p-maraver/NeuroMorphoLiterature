import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PdfService {
  private url = 'http://129.174.10.65:8186';
  // private url = 'http://localhost:8186';

  constructor(private http: HttpClient) {
  }

  uploadWithProgress(id: string, formData: FormData): Observable<any> {
    return this.http.post<any>(`${this.url}/file`, formData); // , { observe: 'events',  reportProgress: true });
  }
}
