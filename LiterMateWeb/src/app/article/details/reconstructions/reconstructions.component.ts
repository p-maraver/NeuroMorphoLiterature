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

import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {FormArray, FormBuilder, FormGroup} from '@angular/forms';
import {MatHorizontalStepper, MatSnackBar} from '@angular/material';
import {ArticlesService} from '../services/articles.service';
import {Reconstructions} from '../model/reconstructions';
import {Subscription} from 'rxjs';
import {Article} from '../model/article';
import {DataService} from '../../data.service';
import {filter} from 'rxjs/operators';
import {FullTextService} from '../../../services/full-text/full-text.service';
import {ExternalService} from '../../../services/external/external.service';

@Component({
  selector: 'app-reconstructions',
  templateUrl: './reconstructions.component.html',
  styleUrls: ['./reconstructions.component.css']
})
export class ReconstructionsComponent implements OnInit, OnChanges {

  @Input() stepper: MatHorizontalStepper;

  @Input() article: Article;
  subscription: Subscription; // subscribe to read article updates

  formGroup: FormGroup;
  reconstructionsStatusValues: string[];

  constructor(private articlesService: ArticlesService,
              private dataService: DataService,
              private fullTextService: FullTextService,
              private externalService: ExternalService,
              private _formBuilder: FormBuilder,
              private snackBar: MatSnackBar) {
    this.initForm();
    this.subscription = this.dataService.article$.pipe( // subscribe add new article
      filter(articleObserver => articleObserver.emitter === 'data')
    ).subscribe(articleObserver => {
      this.article = articleObserver.article;
    });
  }

  ngOnInit() {
    this.articlesService.getReconstructionsStatusValues().subscribe(values => {
      this.reconstructionsStatusValues = values;
    });
  }

  ngOnChanges(changes) {
    if (changes.article.currentValue) {
      this.fillForm();
    }
  }

  initForm() {
    this.formGroup = this._formBuilder.group({
      reconstructionsList: this._formBuilder.array([])
    });
  }

  fillForm() {
    const reconstructionsFormList = this.formGroup.get('reconstructionsList') as FormArray;
    if (this.article.reconstructions != null && this.article.reconstructions.reconstructionsList !== null) {
      this.article.reconstructions.reconstructionsList.forEach(reconstructions => {
          reconstructionsFormList.push(this._formBuilder.group({
            id: reconstructions.id,
            statusDetails: reconstructions.statusDetails,
            expirationDate: reconstructions.expirationDate ? reconstructions.expirationDate : null,
            nReconstructions: reconstructions.nReconstructions,
            date: reconstructions.date
          }));
        }
      );
    }
  }

  add() {
    const reconstructionsList = this.formGroup.get('reconstructionsList') as FormArray;
    reconstructionsList.push(this._formBuilder.group({
      id: reconstructionsList.length + 1,
      statusDetails: '',
      expirationDate: '',
      nReconstructions: '',
      date: new Date()
    }));
  }

  delete(i: number) {
    this.formGroup.markAsDirty();
    const reconstructionsList = this.formGroup.get('reconstructionsList') as FormArray;
    reconstructionsList.removeAt(i);
  }

  convertDate(date): Date {
    return date === null ? null : new Date(date);
  }

  get reconstructionsList() {
    return this.formGroup.get('reconstructionsList') as FormArray;
  }

  save() {
    if (this.formGroup.valid && this.formGroup.dirty) {
      if (this.article.reconstructions == null) {
        this.article.reconstructions = new Reconstructions();
      }
      this.article.reconstructions.reconstructionsList = this.formGroup.get('reconstructionsList').value;
      this.articlesService.update(this.article.id, 'reconstructions', this.article.reconstructions)
        .subscribe(p => {
            if (this.article.reconstructions.reconstructionsList[0].statusDetails === 'In processing pipeline' ||
                this.article.reconstructions.reconstructionsList[0].statusDetails === 'Positive response') {
                this.metadataCall();
            }
          },
          error => this.snackBar.open('Error updating reconstructions', 'Error'));
      this.stepper.next();
    }
  }

  metadataCall() {
    this.fullTextService.findFullText(this.article.id)
      .subscribe(fullText => {
        this.externalService.sendData(this.article, fullText)
          .subscribe(
            res => {
              this.snackBar.open(`Metadata API correctly called`, 'Success');
            },
            error => {
              console.log(error);
              this.snackBar.open(`Error calling metadata webhook: ${error.statusText}`, 'Error');
            });
      });
  }
}
