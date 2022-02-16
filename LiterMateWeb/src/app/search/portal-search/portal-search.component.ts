import {Component, OnInit} from '@angular/core';
import {MatDialog, MatSnackBar} from '@angular/material';
import {PortalSearchService} from '../../services/portal-search/portal-search.service';
import {Portal} from '../../services/portal-search/model/portal';
import {KeywordDialogComponent} from '../keywords-search/keyword-dialog.component';
import {Keyword} from '../../services/portal-search/model/keyword';
import {PortalDialogComponent} from './portal-dialog.component';

@Component({
  selector: 'app-portal-search',
  templateUrl: './portal-search.component.html',
  styleUrls: ['./portal-search.component.css']
})
export class PortalSearchComponent implements OnInit {

  displayedColumns: string[] = ['name', 'startYear', 'endYear',
    'token', 'active', 'success', 'status', 'edit'];
  portalList = [];
  searching = false;

  constructor(private portalSearchService: PortalSearchService,
              public dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.getPortalList();
  }

  getPortalList(): void {

    this.portalSearchService.findPortalList()
      .subscribe(portalList => {
          this.portalList = portalList;
        },
        error => {
          this.snackBar.open('Error receiving portal values', 'Error');
        });
  }

  startSearch(): void {
    this.searching = true;
    this.portalSearchService.launchSearch()
      .subscribe(p => {
          this.searching = false;
        },
        error => {
          this.searching = false;
          this.snackBar.open('Error launching portal search', 'Error');
        });
  }

  getYear(date): number {
    return new Date(date).getFullYear();
  }

  openDialog(portal: Portal): void {
    const dialogRef = this.dialog.open(PortalDialogComponent, {
      width: '250px',
      data: portal
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {

        result.startSearchDate = new Date(result.startYear.toString());
        result.endSearchDate = new Date(result.endYear.toString());

        this.portalSearchService.updatePortal(result)
          .subscribe(p => {
            },
            error => {
              this.snackBar.open('Error updating portal', 'Error');
            });
      }
    });
  }


}
