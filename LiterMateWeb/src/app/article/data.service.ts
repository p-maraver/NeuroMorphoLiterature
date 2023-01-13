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

import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';
import {Article} from './details/model/article';
import {ArticleObserver} from './details/model/article-observer';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  // Observable string sources
  private articleSource = new Subject<ArticleObserver>();
  private stepSource = new Subject<string>();

  article$ = this.articleSource.asObservable();
  step$ = this.stepSource.asObservable();
  article: Article;

  constructor() { }

  getArticle() {
    return this.article;
  }

  sendArticle(articleObserver: ArticleObserver) {
    this.article = articleObserver.article;
    this.articleSource.next(articleObserver);
  }

  setStep(step: string) {
    this.stepSource.next(step);
  }

}
