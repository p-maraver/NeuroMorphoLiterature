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
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {ReleaseVersion} from './release-version';
import {catchError, map} from 'rxjs/operators';
import {environment} from '../../../environments/environment';

export enum TaskStatus {
  processing,
  error,
  success
}

export interface TaskProperties {
  status: TaskStatus;
  message: string;
  taskName: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReleaseService {
  // private url_release = 'http://129.174.10.65:8192';
  private url_release = environment.apiUrl + '/release';
  private url_api = environment.apiNeuromorpho + '/apiLiteratureReview/literature/reports?';

  constructor(private http: HttpClient) {
  }

  findVersion(): Observable<ReleaseVersion> {
    return this.http.get<ReleaseVersion>(`${this.url_release}/version/literature`);
  }

  getVersionList(): Observable<string[]> {
    return this.http.get<string[]>(`${this.url_release}/version`);
  }

  release(value: string, version?: string, parameter?: string): Observable<TaskProperties> {
    let uriParam = '';
    let versionParam = '';

    if (version != null) {
      versionParam = '?version=' + version;
      if (parameter != null) {
        uriParam = '&type=' + parameter;
      }
    }
    return this.http.get(`${this.url_release}/release/` + value + versionParam + uriParam, {observe: 'response'}).pipe(
      map(res => {
        return {
          status: TaskStatus.success,
          taskName: `release-${value}`,
          message: ''
        };
      }),
      catchError((err) => {

        console.log(err);

        if (err.status === 0) {

          return of({
            status: TaskStatus.error,
            taskName: `release-${value}`,
            message: 'Unable to connect to server'
          });

        }

        return of({
          status: TaskStatus.error,
          taskName: `release-${value}`,
          message: 'Server error'
        });
      })
    );

  }



  updateVersion(releaseObject): Observable<TaskProperties> {
    return this.http.put(`${this.url_release}/version/`, releaseObject,
      {observe: 'response'})
      .pipe(
        map(_res => {
          return {
            status: TaskStatus.success,
            taskName: 'update-version',
            message: ''
          };

        }),
        catchError((err) => {

          console.log(err);

          if (err.status === 0) {

            return of({
              status: TaskStatus.error,
              taskName: 'update-version',
              message: 'Unable to connect to server'
            });

          }

          return of({
            status: TaskStatus.error,
            taskName: 'update-version',
            message: 'An error occurred updating the version'
          });
        })
      );
  }

  generateReport(type: string): Observable<TaskProperties> {
    let params = new HttpParams();
    params = params.append('type', type);
    return this.http.get(this.url_api, {
      params: params,
      observe: 'response'
    })
      .pipe(
        map(_res => {
          return {
            status: TaskStatus.success,
            taskName: `generate-${type}`,
            message: ''
          };
        }),
        catchError((err) => {

          console.log(err);

          if (err.status === 0) {

            return of({
              status: TaskStatus.error,
              taskName: `generate-${type}`,
              message: 'Unable to connect to server'
            });

          }

          return of({
            status: TaskStatus.error,
            taskName: `generate-${type}`,
            message: 'An error occurred generating reports'
          });

        })
      );
  }

  startWebHook(): Observable<TaskProperties> {

    return this.http.get('http://ec2-100-28-253-215.compute-1.amazonaws.com:5050/users/webhook', {observe: 'response'}).pipe(
      map(_res => {
        return {
          status: TaskStatus.success,
          taskName: 'web hook',
          message: ''
        };
      }),
      catchError((err) => {

        console.log(err);

        if (err.status === 0) {

          return of({
            status: TaskStatus.error,
            taskName: 'web hook',
            message: 'Unable to connect to server'
          });

        }

        return of({
          status: TaskStatus.error,
          taskName: 'web hook',
          message: 'Server error'
        });
      })
    );
  }


}
