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

import {Component, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder} from '@angular/forms';
import {ArticlesService} from './services/articles.service';
import {Collection} from './model/collection';
import {MatHorizontalStepper} from '@angular/material';
import {Article} from './model/article';
import {DataService} from '../data.service';
import {Subscription} from 'rxjs';
import {ArticleStatus} from './model/article-status';
import {filter} from 'rxjs/operators';
import {Usage} from './model/usage';
import {ExternalService} from '../../services/external/external.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {FullTextService} from '../../services/full-text/full-text.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {
  // forms
  isLinear = true;

  @ViewChild(MatHorizontalStepper, {static: true}) stepper: MatHorizontalStepper;

  collectionEvaluated: Collection[];
  article: Article;
  subscription: Subscription; // subscribe to read article updates
  collection: Collection;
  showMetadata = false;
  showReconstructions = false;

  constructor(private articlesService: ArticlesService,
              private externalService: ExternalService,
              private _formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private dataService: DataService,
              private fullTextService: FullTextService,
              private snackBar: MatSnackBar) {

  }

  ngOnInit() {
    this.article = this.dataService.getArticle();
    if (this.article != null) {
      this.evaluateArticleStatus(null);
    } else {
      this.subscription = this.dataService.article$.pipe(
        filter(articleObserver => articleObserver.emitter === 'server')
      ).subscribe(articleObserver => {
        this.article = articleObserver.article;
        this.evaluateArticleStatus(null);
      });
    }
    this.collectionEvaluated = [Collection.Positive, Collection.Negative, Collection.Inaccessible];
  }


  unlockArticle() {
    this.articlesService.update(this.article.id, 'locked', {'locked': this.article.locked})
      .subscribe(
      );
  }

  getArticleStatus(articleStatus: ArticleStatus) {
    this.evaluateArticleStatus(articleStatus);
  }

  evaluateArticleStatus(articleStatus: ArticleStatus) {
    if (articleStatus == null) {
      if (this.article.status != null &&
        (this.article.status === Collection.ToBeEvaluated ||
          this.article.status === Collection.Evaluated ||
          this.article.status === Collection.NeuroMorphoEvaluated)) {
        articleStatus = {
          'collection': this.article.metadata == null ? Collection.ToBeEvaluated : this.article.metadata.articleStatus,
          'internEvaluation': true
        };
      } else {
        articleStatus = {
          'collection': this.article.status,
          'internEvaluation': false
        };
      }
    }
    if (articleStatus.collection === Collection.Positive) {
      this.showMetadata = true;
      this.dataService.setStep('metadata');
    } else {
      this.showMetadata = false;
    }

    if (articleStatus.collection === Collection.Positive &&
      !articleStatus.internEvaluation
      && (this.article == null ||
        this.article != null && this.article.data.dataUsage.includes(Usage.Describing))) {
      this.showReconstructions = true;
      this.dataService.setStep('reconstructions');
    } else {
      this.showReconstructions = false;
    }
  }

  isValidForm(): boolean {
    const negative = this.article != null &&
      this.article.metadata != null &&
      this.article.metadata.articleStatus === Collection.Negative;

    const inaccessible = this.article != null &&
      this.article.metadata != null &&
      this.article.metadata.articleStatus === Collection.Inaccessible;

    const toBeEvaluated = this.article != null &&
      this.article.metadata != null &&
      this.article.metadata.articleStatus === Collection.ToBeEvaluated;

    const positive = this.article != null && this.article.metadata != null && this.article.metadata.articleStatus === Collection.Positive &&
      this.article.data.dataUsage.includes(Usage.Describing) &&
      (this.article.metadata.species != null ||
        this.article.metadata.cellType != null ||
        this.article.metadata.brainRegion != null ||
        this.article.metadata.nReconstructions != null ||
        this.article.metadata.tracingSystem != null) ||
      (this.article != null && this.article.data.dataUsage.includes(Usage.Sharing)) ||
      (this.article != null && this.article.data.dataUsage.includes(Usage.Citing)) ||
      (this.article != null && this.article.data.dataUsage.includes(Usage.About));
    return negative || positive || toBeEvaluated || inaccessible;
  }

  removeArticle(id: string): void {
    this.articlesService.delete(id).subscribe(result => {
      window.close();
    });
  }
}
