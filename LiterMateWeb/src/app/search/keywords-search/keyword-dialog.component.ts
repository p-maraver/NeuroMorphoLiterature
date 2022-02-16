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
