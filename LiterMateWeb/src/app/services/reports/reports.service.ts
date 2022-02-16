import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReportsService {
  private url = 'http://129.174.10.65:8189/articles';

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
