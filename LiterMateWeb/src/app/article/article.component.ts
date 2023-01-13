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

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';

import {Article} from './details/model/article';
import {ArticlesService} from './details/services/articles.service';
import {DataService} from './data.service';
import {Subscription} from 'rxjs';
import {FullTextService} from '../services/full-text/full-text.service';


@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.css']
})
export class ArticleComponent implements OnInit {

  article: Article;
  exists = 'EMPTY';
  userSubscription: Subscription;


  constructor(private articlesService: ArticlesService,
              private fullTextService: FullTextService,
              private dataService: DataService,
              private route: ActivatedRoute) {

  }

  /*onNotify(reconstructionsFormGroup: FormGroup): void {
    this.reconstructionsFormGroup = reconstructionsFormGroup;
  }*/

  ngOnInit() {
    this.userSubscription = this.route.params.subscribe(
      (params: ParamMap) => {
        const id = params['id'];
        if (id !== 'new') {
          this.getArticle(id);
          this.existsFullText(id);
        }
      });
  }

  getArticle(id): void {
    this.articlesService.findById(id)
      .subscribe(article => {
        this.article = article;
        this.convertDates();
        this.dataService.sendArticle({'emitter': 'server', 'article': article});
      });

  }

  private convertDates() {
    this.article.data.ocDate = this.convertDate(this.article.data.ocDate);
    this.article.data.evaluatedDate = this.convertDate(this.article.data.evaluatedDate);
    this.article.data.publishedDate = this.convertDate(this.article.data.publishedDate);
    this.article.data.approvedDate = this.convertDate(this.article.data.approvedDate);
    if (this.article.reconstructions && this.article.reconstructions.reconstructionsList) {
      this.article.reconstructions.reconstructionsList.forEach(reconstructions => {
        reconstructions.date = this.convertDate(reconstructions.date);
        reconstructions.expirationDate = this.convertDate(reconstructions.expirationDate);
      });
    }
  }

  private convertDate(date: Date) {
    return date !== null ? new Date(date) : null;
  }

  private existsFullText(id) {
    this.fullTextService.existsFullText(id)
      .subscribe(exists => {
        this.exists = exists;
      });
  }

  private openFullText() {
    const id = this.route.snapshot.paramMap.get('id');
    window.open(`article/${id}/fulltext`, 'newwindow', 'width=800,height=700,left=450');
  }
}

