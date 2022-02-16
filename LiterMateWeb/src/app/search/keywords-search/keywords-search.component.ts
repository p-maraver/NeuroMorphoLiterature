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
