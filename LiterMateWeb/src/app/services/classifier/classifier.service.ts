import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Classifier} from './classifier';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClassifierService {

   private url = 'http://129.174.10.65:8189/evaluate';

   // private url = 'http://localhost:8184';

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
