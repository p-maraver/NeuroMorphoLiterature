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
