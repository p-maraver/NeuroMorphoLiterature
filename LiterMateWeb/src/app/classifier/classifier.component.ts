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
import {ClassifierService} from '../services/classifier/classifier.service';
import {Classifier, Status} from '../services/classifier/classifier';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-classifier',
  templateUrl: './classifier.component.html',
  styleUrls: ['./classifier.component.css']
})
export class ClassifierComponent implements OnInit {

  versionList = [];
  currentVersion = 'v2';
  file = 'assets/keywords/term';
  newVersion = '';
  keywordList = [];
  classifierList: Classifier[];
  training = false;
  updatedVersion = false;
  updatedThresholds = false;

  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true
  };
  public chartLabels = Array.from(Array(30).keys());
  public chartType = 'line';
  public chartLegend = true;

  constructor(private classifierService: ClassifierService,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.getClassifierList();
  }

  async getClassifierList() {
  }

  async train() {
    this.training = true;
    await this.classifierService.train(this.newVersion);
  }

  async update(id: string, field: string, object: Object) {
    await this.classifierService.update(id, field, object);
    this.updatedThresholds = true;
  }

  async updateVersion(version: string) {
    const versionNew = this.classifierList.find(element => element.version === version);
    await this.classifierService.update(versionNew.id, 'current', true);
    const versionOld = this.classifierList.find(element => element.current === true);
    await this.classifierService.update(versionOld.id, 'current', false);

    this.updatedVersion = true;

  }

  getStatus(status: Status) {
    return Status[status];
  }

  getValue(accuracy: number[]) {
    return accuracy == null ? null : accuracy[29];
  }
}
