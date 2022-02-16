import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

@Component({
  selector: 'app-portal-dialog',
  templateUrl: './portal-dialog.component.html',
  styleUrls: ['./portal-dialog.component.css']
})
export class PortalDialogComponent {


  constructor( public dialogRef: MatDialogRef<PortalDialogComponent>,
               @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    data.startYear = new Date(data.startSearchDate).getFullYear();
    data.endYear = new Date(data.endSearchDate).getFullYear();

  }
  onNoClick(): void {
    this.dialogRef.close();
  }


}
