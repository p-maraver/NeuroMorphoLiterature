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
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {DialogData} from '../../article/details/data/merge-dialog.component';
import {Collection} from '../../article/details/model/collection';
import {Keyword} from '../../services/portal-search/model/keyword';

@Component({
  selector: 'app-keyword-dialog',
  templateUrl: './keyword-dialog.component.html',
  styleUrls: ['./keyword-dialog.component.css']
})
export class KeywordDialogComponent {

  collectionOptions: Collection[] = [Collection.ToBeEvaluated, Collection.NeuroMorpho];

  constructor( public dialogRef: MatDialogRef<KeywordDialogComponent>,
               @Inject(MAT_DIALOG_DATA) public data: Keyword
  ) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
