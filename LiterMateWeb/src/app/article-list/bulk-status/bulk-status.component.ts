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
import {ArticlesService} from '../../article/details/services/articles.service';
import {Collection} from '../../article/details/model/collection';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-bulk-status',
  templateUrl: './bulk-status.component.html',
  styleUrls: ['./bulk-status.component.css']
})
export class BulkStatusComponent implements OnInit {

  pmidCommaList: string;
  statusListUpdated = true;
  statusDetailsFrom: string;
  statusDetailsTo: string;
  error = 'Articles not found in DB: ';
  statusDetailsList: string[] = [];

  constructor(private articlesService: ArticlesService,
              private route: ActivatedRoute,
              private snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    this.getStatusDetails();
  }

  getStatusDetails(): void {
    this.articlesService.getReconstructionsStatusValues()
      .subscribe(result => {
        this.statusDetailsList = result;
      });
  }

  submitStatusList(): void {
    const pmidList = this.pmidCommaList.trim().replace(/(\r\n|\n|\r)/g, '').split(',');

    pmidList.forEach(pmid => {
      const collection = Collection[this.route.snapshot.paramMap.get('collection')];

      this.articlesService.findAllByText(collection, 'text=' + pmid).subscribe(result => {
        if (result.totalElements === 0) {
          this.error = this.error + pmid + ',';
        }
        const article = result.content[0];
        article.reconstructions.reconstructionsList.forEach(function (status) {
          if (status.statusDetails === this.statusDetailsFrom) {
            status.statusDetails = this.statusDetailsTo;
          }
        });
        this.articlesService.update(article.id, 'reconstructions', article.reconstructions).subscribe(result => {
          this.statusListUpdated = true;
        },
          error => this.snackBar.open('Error updating article status details', 'Error'));
      });
    });
  }

}
