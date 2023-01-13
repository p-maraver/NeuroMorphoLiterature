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

import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from '@angular/material';
import {ArticlesService} from '../services/articles.service';
import {Article} from '../model/article';

export interface DialogData {
  article: Article;
  error: string;
}

@Component({
  selector: 'app-merge-dialog',
  templateUrl: './merge-dialog.component.html',
  styleUrls: ['./merge-dialog.component.css']
})


export class MergeDialogComponent {

  duplicateId: string;
  article: Article;
  collection: string;

  constructor(
    public dialogRef: MatDialogRef<MergeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private articlesService: ArticlesService,
    private snackBar: MatSnackBar) {

    this.article = data.article;
    const resultList = data.error.split('; ');
    resultList.forEach(result => {
      const value = result.split(': ');
      if (value[0] === 'id') {
        this.duplicateId = value[1];
      }
      if (value[0] === 'collection') {
        this.collection = value[1];
      }
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onLinkClick(): void {
    // update usage
    this.articlesService.update(this.article.id, 'data.dataUsage', ['DUPLICATE'])
      .subscribe(p => this.updateCollection(this.collection),
        error => this.snackBar.open('Error updating usage', 'Error'));
    // update collection
  }

  onLinkClickPending(): void {
    // remove non-evaluated article
    this.articlesService.delete(this.duplicateId)
      .subscribe(p => {},
        error => this.snackBar.open('Error removing article', 'Error'));
  }

  onLinkClickNotMatch(): void {
    this.articlesService.update(this.article.id, 'data.dataUsage', ['DUPLICATE'])
      .subscribe(p => this.updateCollection('Evaluated'),
        error => this.snackBar.open('Error updating usage', 'Error'));
    // update collection
  }

  updateCollection(collection) {
    const data = {'duplicateId': this.duplicateId};
    this.articlesService.updateCollection(this.article.id, collection, null).subscribe(
      p => this.articlesService.update(this.article.id, 'duplicate', data)
        .subscribe(p2 => this.dialogRef.close(),
          error => this.snackBar.open('Error updating duplicate list ', 'Error')
        ),
      error => this.snackBar.open('Error updating collection', 'Error'));
  }

  isPending() {
    return this.collection === 'Pending evaluation';
  }

  isSameEvaluation() {
    return this.collection !== 'Pending evaluation'
    && this.article.metadata.articleStatus === this.collection;
  }

  isDistinctEvaluation() {
    return this.collection !== 'Pending evaluation'
      && this.article.metadata.articleStatus !== this.collection;
  }
}

