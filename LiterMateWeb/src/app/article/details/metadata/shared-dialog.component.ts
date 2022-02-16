import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';


@Component({
  selector: 'app-shared-dialog',
  templateUrl: './shared-dialog.component.html',
  styleUrls: ['./shared-dialog.component.css']
})

export class SharedDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<SharedDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

}
