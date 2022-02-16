import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ReleaseVersion} from './release-version';


@Injectable({
  providedIn: 'root'
})
export class ReleaseService {
  private url = 'http://129.174.10.65:8189/release';

  constructor(private http: HttpClient) {
  }

  findVersion(): Observable<ReleaseVersion> {
    return this.http.get<ReleaseVersion>(`${this.url}/version/literature`);
  }
}
