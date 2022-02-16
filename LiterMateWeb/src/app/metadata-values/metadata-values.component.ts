import {Component, OnInit, ViewChild} from '@angular/core';
import {MetadataService} from './service/metadata.service';
import {MetadataValue} from './model/metadata-value';
import {MatDialog, MatSnackBar, MatTable} from '@angular/material';
import {MetadataDialogComponent} from './metadata-dialog.component';
import {MetadataPage} from './model/metadata-page';

@Component({
  selector: 'app-metadata-values',
  templateUrl: './metadata-values.component.html',
  styleUrls: ['./metadata-values.component.css']
})
export class MetadataValuesComponent implements OnInit {

  keyList: string[];
  currentKey: string;
  metadataPage: MetadataPage;
  page = 0;
  sortDirection = 'DESC';
  sortProperty = 'reviewed';

  displayedColumns: string[] = ['name', 'reviewed', 'remove'];
  @ViewChild('table', {static: false}) table: MatTable<any>;

  constructor(private metadataService: MetadataService,
              public dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.getMetadataKeys();
    // this.getMetadataValues();

  }

  getMetadataKeys(): void {
    this.metadataService.getKeys()
      .subscribe(keyList => {
          this.keyList = keyList;
          this.currentKey = keyList[0];
          this.metadataService.getValuesByKey(this.currentKey, this.page, this.sortProperty, this.sortDirection)
            .subscribe(metadataPage => {
                this.metadataPage = metadataPage;
              },
              error => {
                this.snackBar.open('Error receiving metadata values', 'Error');
              });
        },
        error => {
          this.snackBar.open('Error receiving metadata keys', 'Error');
        });
  }

  getMetadataValues(key): void {
    this.currentKey = key;
    this.metadataService.getValuesByKey(key, this.page, this.sortProperty, this.sortDirection)
      .subscribe(metadataPage => {
          this.metadataPage = metadataPage;
        },
        error => {
          this.snackBar.open('Error receiving metadata keys', 'Error');
        });
  }

  reloadValues(key): void {
    this.metadataService.getArticleMetadataDistinctValues(key)
      .subscribe(metadataList => {
          this.metadataService.updateValuesByKey(key, metadataList)
            .subscribe(p => {
                this.getMetadataKeys();
              },
              error => {
                this.snackBar.open('Error reloading new metadata values', 'Error');
              });
        },
        error => {
          this.snackBar.open('Error reloading new metadata values', 'Error');
        });
  }


  openMetadataDialog(element, index): void {
    const dialogRef = this.dialog.open(MetadataDialogComponent, {
      data: {
        metadata: element
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        this.metadataService.updateArticleMetadataValues(
          element.type, element.name, result.nameList).subscribe(p => {
            this.metadataService.remove(element.id).subscribe(q => {
                this.metadataPage.content.splice(index, 1);
                this.table.renderRows();
              },
              error => {
                this.snackBar.open('Error removing metadata name', 'Error');
              });
          },
          error => {
            this.snackBar.open('Error updating articles metadata value', 'Error');
          });
      }
    });
  }


  updatePage(page: number) {
    this.page = page;
    this.getMetadataValues(this.currentKey);
  }

  updateReviewed(element: MetadataValue) {
    this.metadataService.update(element.id, element).subscribe(p => null,
      error => {
        this.snackBar.open('Error updating metadata', 'Error');
      });
  }

  updateSort(active: string, direction: string) {
    this.sortDirection = direction.toUpperCase();
    this.sortProperty = active;
    this.getMetadataValues(this.currentKey);

  }

}
