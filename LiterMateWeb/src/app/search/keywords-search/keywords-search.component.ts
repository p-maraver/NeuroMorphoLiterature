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

import { Component, OnInit } from '@angular/core';
import {MatDialog, MatSnackBar} from '@angular/material';
import {PortalSearchService} from '../../services/portal-search/portal-search.service';
import {KeywordPage} from '../../services/portal-search/model/keyword-page';
import {KeywordDialogComponent} from './keyword-dialog.component';
import {Keyword} from '../../services/portal-search/model/keyword';

@Component({
  selector: 'app-keywords-search',
  templateUrl: './keywords-search.component.html',
  styleUrls: ['./keywords-search.component.css']
})
export class KeywordsSearchComponent implements OnInit {

  constructor(private portalSearchService: PortalSearchService,
              public dialog: MatDialog,
              private snackBar: MatSnackBar) { }

  keywordPage: KeywordPage;
  page = 0;
  displayedColumns = ['name', 'collection',
    'pubmed', 'pubmedcentral', 'sciendirect', 'wiley',
  'springernature', 'googlescholar', 'remove'];
  ngOnInit() {
    this.getKeywordList();
  }

  getKeywordList(): void {
    this.portalSearchService.findKeywordList(this.page)
      .subscribe(keywordPage => {
          this.keywordPage = keywordPage;
        },
        error => {
          this.snackBar.open('Error receiving keyword list', 'Error');
        });
  }

  delete(id: string): void {
    this.portalSearchService.deleteKeyword(id)
      .subscribe(result => {
          this.getKeywordList();
        },
        error => {
          this.snackBar.open('Error removing keyword ', 'Error');
        });
  }

  updatePage(page: number) {
    this.page = page;
    this.getKeywordList();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(KeywordDialogComponent, {
      width: '250px',
      data: new Keyword('')
    });


    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        this.portalSearchService.addKeyword(result).subscribe(p => {
          this.getKeywordList();
          },
          error => {
            this.snackBar.open('Error adding new keyword', 'Error');
          });
      }
    });
  }
}
