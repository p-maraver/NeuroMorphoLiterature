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

import {Component, ElementRef, Input, OnChanges, OnInit, ViewChild} from '@angular/core';
import {Article} from '../model/article';
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Usage} from '../model/usage';
import {
  MatAutocomplete,
  MatAutocompleteSelectedEvent,
  MatChipInputEvent,
  MatDialog,
  MatHorizontalStepper,
  MatSnackBar
} from '@angular/material';
import {filter, map, startWith} from 'rxjs/operators';
import {Observable, Subscription} from 'rxjs';
import {SharedDialogComponent} from './shared-dialog.component';
import {requiredIfDescribing} from '../validators/required-if-describing';
import {atLeastOne} from '../validators/at-least-one';
import {requiredIfSharing} from '../validators/required-if-sharing';
import {Reconstructions} from '../model/reconstructions';
import {ActivatedRoute} from '@angular/router';
import {ArticlesService} from '../services/articles.service';
import {COMMA, ENTER, SEMICOLON} from '@angular/cdk/keycodes';
import {Collection} from '../model/collection';
import {DataService} from '../../data.service';
import {MetadataService} from '../../../metadata-values/service/metadata.service';

@Component({
  selector: 'app-metadata',
  templateUrl: './metadata.component.html',
  styleUrls: ['./metadata.component.css', '../../article.component.css']
})
export class MetadataComponent implements OnInit, OnChanges {

  @Input() article: Article;
  @Input() stepper: MatHorizontalStepper;

  collection: Collection;
  formGroup: FormGroup;
  metadataFormGroup: FormGroup;
  sharedFormGroup: FormGroup;
  usageFormGroup: FormGroup;
  duplicateFormGroup: FormGroup;

  tracingCtrl = new FormControl();
  cellTypeCtrl = new FormControl();

  subscription: Subscription; // subscribe to read article updates

  filteredTracing: Observable<string[]>;
  filteredCellType: string[];

  tracingSystemValues: string[];
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA, SEMICOLON];

  @ViewChild('cellTypeInput', {static: false}) cellTypeInput: ElementRef<HTMLInputElement>;
  @ViewChild('tracingInput', {static: false}) tracingInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', {static: false}) matAutocomplete: MatAutocomplete;


  constructor(private route: ActivatedRoute,
              private articlesService: ArticlesService,
              private metadataService: MetadataService,
              private dataService: DataService,
              private _formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              public dialog: MatDialog) {

    this.initForm();

    this.subscription = this.dataService.article$.pipe( // subscribe add new article
      filter(articleObserver => articleObserver.emitter === 'data')
    ).subscribe(articleObserver => {
      this.article = articleObserver.article;
      this.collection = this.article.status;
    });
  }

  ngOnInit() {
    this.filteredCellType = ['glia', 'neuron'];
    this.metadataService.getReviewedValuesByKey('tracingSystem').subscribe(tracingSystemValues => {
      this.tracingSystemValues = tracingSystemValues;
      this.filteredTracing = this.tracingCtrl.valueChanges.pipe(
        startWith(null),
        map((value: string | null) => value ? this._filter(value) : this.tracingSystemValues.slice()));
    });
  }

  ngOnChanges(changes) {
    if (changes.article.currentValue) {
      this.fillForm();
    }
  }

  initForm() {
    const isNewArticle = this.route.snapshot.paramMap.get('id') === 'new';

    this.metadataFormGroup = this._formBuilder.group({
      nReconstructions: ['', [Validators.min(1), Validators.pattern(/^[0-9]*$/)]],
      species: [[]],
      cellType: [[]],
      brainRegion: [[]],
      tracingSystem: [[]],
    });

    this.usageFormGroup = this._formBuilder.group({
      describing: [false],
      using: [false],
      citing: [false],
      about: [false],
      sharing: [false],
      duplicate: [false]
    });

    this.sharedFormGroup = this._formBuilder.group({
      sharedList: this._formBuilder.array([])
    });

    this.duplicateFormGroup = this._formBuilder.group({
      id: [''],
      pmid: [''],
      title: ['']
    });

    this.formGroup = new FormGroup({
      metadata: this.metadataFormGroup,
      usage: this.usageFormGroup,
      shared: this.sharedFormGroup,
      duplicate: this.duplicateFormGroup
    }, {validators: [atLeastOne(), requiredIfDescribing(isNewArticle), requiredIfSharing()]});

  }

  fillForm() {
    this.metadataFormGroup.setValue({
      nReconstructions: this.article.metadata && this.article.metadata.nReconstructions
        ? this.article.metadata.nReconstructions : this.fillNReconstructions(),
      species: this.article.metadata && this.article.metadata.species
        ? this.article.metadata.species : [],
      cellType: this.article.metadata && this.article.metadata.cellType
        ? this.article.metadata.cellType : [],
      brainRegion: this.article.metadata && this.article.metadata.brainRegion
        ? this.article.metadata.brainRegion : [],
      tracingSystem: this.article.metadata && this.article.metadata.tracingSystem
        ? this.article.metadata.tracingSystem : []
    });
    this.usageFormGroup.setValue({
      describing: this.article.data.dataUsage.indexOf(Usage.Describing) !== -1 ? true : false,
      using: this.article.data.dataUsage.indexOf(Usage.Using) !== -1 ? true : false,
      citing: this.article.data.dataUsage.indexOf(Usage.Citing) !== -1 ? true : false,
      about: this.article.data.dataUsage.indexOf(Usage.About) !== -1 ? true : false,
      sharing: this.article.data.dataUsage.indexOf(Usage.Sharing) !== -1 ? true : false,
      duplicate: this.article.data.dataUsage.indexOf(Usage.Duplicate) !== -1 ? true : false,

    });
    this.fillSharedList(this.article.sharedList);
    this.fillDuplicate(this.article.duplicate);


  }


  add(event: MatChipInputEvent, type: string): void {
    const input = event.input;
    const value = event.value;

    if ((value || '').trim()) {
      this.metadataFormGroup.markAsDirty();
      this.metadataFormGroup.get(type).value.push(value.trim());
      this.formGroup.updateValueAndValidity();
      // Reset the input value
      if (input) {
        input.value = '';
      }
      this.tracingCtrl.setValue(null);
    }
  }

  remove(key: string, type: string): void {
    this.metadataFormGroup.markAsDirty();
    const index = this.metadataFormGroup.get(type).value.indexOf(key);
    if (index >= 0) {
      this.metadataFormGroup.get(type).value.splice(index, 1);
    }
    this.formGroup.updateValueAndValidity();
  }

  selectedTracingSystem(event: MatAutocompleteSelectedEvent): void {
    this.metadataFormGroup.markAsDirty();
    this.metadataFormGroup.get('tracingSystem').value.push(event.option.viewValue);
    this.tracingInput.nativeElement.value = '';
    this.tracingCtrl.setValue(null);
    this.formGroup.updateValueAndValidity();

  }

  selectedCellType(event: MatAutocompleteSelectedEvent): void {
    this.metadataFormGroup.markAsDirty();
    this.metadataFormGroup.get('cellType').value.push(event.option.viewValue);
    this.cellTypeInput.nativeElement.value = '';
    this.cellTypeCtrl.setValue(null);
    this.formGroup.updateValueAndValidity();

  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.tracingSystemValues.filter(tracingSystem => tracingSystem.toLowerCase().indexOf(filterValue) === 0);
  }

  fillSharedList(sharedListData: Article[]) {
    const sharedList = this.sharedFormGroup.get('sharedList') as FormArray;
    /*while (sharedList.length !== 0) {
      sharedList.removeAt(0);
    }*/
    if (sharedListData != null) {
      sharedListData.forEach(shared => {
        if (sharedList.value.some(e => e.id === shared.id)) {
          this.snackBar.open('The article you are trying to link is already linked', 'Error');
        }
        sharedList.push(this._formBuilder.group({
          id: shared.id,
          data: this._formBuilder.group({
            title: shared.data.title,
            pmid: shared.data.pmid
          }),
          metadata: this._formBuilder.group({
            species: shared.metadata.species == null ? null : shared.metadata.species[0],
            cellType: shared.metadata.cellType == null ? null : shared.metadata.cellType[0],
            brainRegion: shared.metadata.brainRegion == null ? null : shared.metadata.brainRegion[0],
            tracingSystem: shared.metadata.tracingSystem == null ? null : shared.metadata.tracingSystem[0]
          }),
          reconstructions: this._formBuilder.group({
            reconstructionsList: this._formBuilder.array(this.fillReconstructionsList(shared.id, shared.reconstructions))
          })
        }));
        if (shared.data.dataUsage.includes(Usage.Describing) &&
          (shared.status === Collection.Positive || shared.status === Collection.Evaluated)) {
          this.sharedFormGroup.markAsDirty();

        } else {
          this.snackBar.open('Only articles evaluated "Positive Describing Reconstructions" can be linked,' +
            ' the article you are trying to link is not. ' +
            'Please review it', 'Error');
        }
      });
    }
  }

  fillReconstructionsList(id: string, reconstructionsData: Reconstructions) {
    if (reconstructionsData && reconstructionsData.reconstructionsList) {
      return reconstructionsData.reconstructionsList.map(reconstructions => {
        return (this._formBuilder.group({
          nReconstructions: reconstructions.nReconstructions,
          statusDetails: reconstructions.statusDetails
        }));

      });
    } else {
      return [];
    }
  }

  deleteShared(i: number) {
    this.sharedFormGroup.markAsDirty();
    const sharedList = this.sharedFormGroup.get('sharedList') as FormArray;
    sharedList.removeAt(i);
  }

  openSharedDialog(): void {
    const dialogRef = this.dialog.open(SharedDialogComponent, {});

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        this.articlesService.findAllByText('all', 'text=' + result + '&page=0')
          .subscribe(articlePage => {
            if (articlePage.numberOfElements === 0) {
              this.snackBar.open('Shared article not found, search it first ', 'Error');
            } else if (articlePage.numberOfElements > 1) {
              this.snackBar.open('Shared article not unique, please use a unique identifier for linking ', 'Error');
            } else {
              this.fillSharedList(articlePage.content);
            }
          });
      }
    });
  }

  fillDuplicate(duplicateArticle: Article) {
    if (duplicateArticle != null) {
      this.duplicateFormGroup.setValue({
        id: duplicateArticle.id,
        title: duplicateArticle.data.title,
        pmid: duplicateArticle.data.pmid
      });
    }
  }

  openDuplicateDialog(): void {
    const dialogRef = this.dialog.open(SharedDialogComponent, {});

    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        this.articlesService.findAllByText('all', 'text=' + result + '&page=0')
          .subscribe(articlePage => {
            if (articlePage.numberOfElements === 0 || articlePage.numberOfElements > 1) {
              this.snackBar.open('Duplicate article not found ', 'Error');
            } else {
              this.duplicateFormGroup.markAsDirty();
              this.fillDuplicate(articlePage.content[0]);
            }
          });
      }
    });
  }


  save() {
    if (this.formGroup.valid) {
      if (this.usageFormGroup.dirty) {
        this.updateUsage();
      }
      if (this.metadataFormGroup.dirty) {
        this.updateMetadata();
      }
      if (this.sharedFormGroup.dirty) {
        this.updateShared();
      }
      if (this.duplicateFormGroup.dirty) {
        this.updateDuplicate();
      }
      this.stepper.next();
    }
  }

  updateMetadata() {
    const nReconstructions = +this.metadataFormGroup.get('nReconstructions').value; // number not string
    this.metadataFormGroup.get('nReconstructions').setValue(nReconstructions);
    if (this.article.metadata != null) {
      Object.assign(this.article.metadata, this.metadataFormGroup.value);
    } else {
      this.article.metadata = this.metadataFormGroup.value;
    }

    this.articlesService.update(this.article.id, 'metadata', this.article.metadata).subscribe(
      p => null,
      error => this.snackBar.open('Error updating metadata', 'Error')
    );
  }

  updateUsage() {
    const keys = Object.keys(this.usageFormGroup.value);
    const dataUsage: Usage[] = [];

    keys.forEach(key => {
      if (this.usageFormGroup.value[key]) {
        dataUsage.push(Usage[key[0].toUpperCase() + key.substring(1)]);
      }
    });
    this.articlesService.update(this.article.id, 'data.dataUsage', dataUsage)
      .subscribe(p => null,
        error => this.snackBar.open('Error updating data Usage ', 'Error')
      );
  }

  updateShared() {
    const sharedList = this.sharedFormGroup.get('sharedList') as FormArray;
    const data = sharedList.value.map(shared => {
      return {'id': shared.id};
    });
    this.articlesService.update(this.article.id, 'sharedList', data)
      .subscribe(p => null,
        error => this.snackBar.open('Error updating sharing reconstructions ', 'Error')
      );
  }

  updateDuplicate() {
    const data = {'duplicateId': this.duplicateFormGroup.value.id};
    this.articlesService.update(this.article.id, 'duplicate', data)
      .subscribe(p => null,
        error => this.snackBar.open('Error linking to duplicate article ', 'Error')
      );
  }

  showReconstructions2() {
    return (this.article.status !== Collection.Positive)
      || (this.article.status === Collection.Positive && this.article.metadata == null)
      || (this.article.status === Collection.Positive && this.article.metadata !== null &&
        this.article.metadata.nReconstructions == null || this.article.metadata.nReconstructions === 0);
  }

  fillNReconstructions() {
    if (this.article.status === Collection.Positive) {
      if (this.article.reconstructions == null) {
        return null;
      } else {
        return this.article.reconstructions.reconstructionsList.reduce((sum, current) => sum + current.nReconstructions, 0);
      }
    } else {
      return 1;
    }
  }

}

