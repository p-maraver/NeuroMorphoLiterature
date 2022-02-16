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

import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatChipInputEvent, MatDialogRef, MatSnackBar} from '@angular/material';
import {MatAutocompleteSelectedEvent, MatAutocomplete} from '@angular/material/autocomplete';
import {MetadataService} from './service/metadata.service';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-metadata-dialog',
  templateUrl: './metadata-dialog.component.html',
  styleUrls: ['./metadata-dialog.component.css']
})
export class MetadataDialogComponent implements OnInit {

  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  nameCtrl = new FormControl();
  filteredNameList: Observable<string[]>;
  nameListValues: string[];

  @ViewChild('nameInput', {static: false}) nameInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;

  constructor(
    public dialogRef: MatDialogRef<MetadataDialogComponent>,
    private metadataService: MetadataService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private snackBar: MatSnackBar) {
    data.nameList = [];
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit() {
    this.getMetadataValues();
  }


  getMetadataValues(): void {

    this.metadataService.getReviewedValuesByKey(this.data.metadata.type)
      .subscribe(nameList => {
          this.nameListValues = nameList;
          this.filteredNameList = this.nameCtrl.valueChanges.pipe(
            map((value: string | null) => value ? this._filter(value) : this.nameListValues.slice()));
        },
        error => {
          this.snackBar.open('Error receiving metadata values', 'Error');
        });
  }

  add(event: MatChipInputEvent): void {
    // Add fruit only when MatAutocomplete is not open
    // To make sure this does not conflict with OptionSelected Event
    if (!this.matAutocomplete.isOpen) {
      const input = event.input;
      const value = event.value;

      // Add our fruit
      if ((value || '').trim()) {
        this.data.nameList.push(value.trim());
      }

      // Reset the input value
      if (input) {
        input.value = '';
      }

      this.nameCtrl.setValue(null);
    }
  }

  remove(name: string): void {
    const index = this.data.nameList.indexOf(name);

    if (index >= 0) {
      this.data.nameList.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    this.data.nameList.push(event.option.viewValue);
    this.nameInput.nativeElement.value = '';
    this.nameCtrl.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.nameListValues.filter(name => name.toLowerCase().indexOf(filterValue) === 0);
  }

}
