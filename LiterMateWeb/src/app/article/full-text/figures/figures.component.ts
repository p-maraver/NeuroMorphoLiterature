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

import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Figure} from '../model/figure';
import {ActivatedRoute} from '@angular/router';
import {MatAutocompleteSelectedEvent, MatAutocomplete} from '@angular/material/autocomplete';
import {COMMA, ENTER, SEMICOLON} from '@angular/cdk/keycodes';
import {MatChipInputEvent} from '@angular/material';
import {FormControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {FigureCategory} from '../model/figure-category';
import {map, startWith} from 'rxjs/operators';
import {FullTextService} from '../../../services/full-text/full-text.service';

@Component({
  selector: 'app-images',
  templateUrl: './figures.component.html',
  styleUrls: ['./figures.component.css']
})
export class FiguresComponent implements OnInit {

  @Input() figureList: Figure[];
  @Input() isSupplementary: boolean;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA, SEMICOLON];
  categoryCtrl = new FormControl();
  filteredCategoryLabel: Observable<string[]>;

  categoryOptions: string[] = ['Microscopy', 'Reconstruction', 'Morphology', 'Electrophysiology'];

  @ViewChild('labelInput', {static: false}) labelInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;

  constructor(private route: ActivatedRoute,
              private fullTextService: FullTextService) {
    this.filteredCategoryLabel = this.categoryCtrl.valueChanges.pipe(
      startWith(null),
      map((label: string | null) => label ? this._filter(label) : this.categoryOptions.slice()));
  }

  ngOnInit() {
  }

  showImage(base64: string) {
    return 'data:image/jpeg;base64,' + base64;
  }


  updateCategory(figureLabel: string, category: FigureCategory) {
    const id = this.route.snapshot.paramMap.get('id');
    const figure2Send = {
      'label': figureLabel,
      'category': category
    };
    let field;
    if (this.isSupplementary) {
      field = 'supplementaryMaterial.figureList';
    } else {
      field = 'figureList';
    }
    this.fullTextService.updateFigureCategory(id, field, figure2Send)
      .subscribe(
      );
  }

  add(event: MatChipInputEvent): void {
    // Add fruit only when MatAutocomplete is not open
    // To make sure this does not conflict with OptionSelected Event
    const input = event.input;
    const value = event.value;

    // Reset the input value
    if (input) {
      input.value = '';
    }
  }

  remove(key: string, figure: Figure): void {
    const index = figure.category.labelList.indexOf(key);
    if (index >= 0) {
      figure.category.labelList.splice(index, 1);
    }
    this.updateCategory(figure.label, figure.category);

  }

  selected(event: MatAutocompleteSelectedEvent, figure: Figure): void {
    if (figure.category == null) {
      figure.category = {'labelList': [], 'value': null};
    }
    figure.category.labelList.push(event.option.viewValue);
    this.labelInput.nativeElement.value = '';
    this.categoryCtrl.setValue(null);
    this.updateCategory(figure.label, figure.category);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.categoryOptions.filter(categoryLabel => categoryLabel.toLowerCase().indexOf(filterValue) === 0);
  }

}
