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

import {Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, ViewChild} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, ValidationErrors, Validators} from '@angular/forms';


import {MatDialog, MatHorizontalStepper, MatSnackBar} from '@angular/material';
import {Subject} from 'rxjs';
import {Article} from '../model/article';
import {Collection} from '../model/collection';
import {SearchService} from '../services/search.service';
import {ArticlesService} from '../services/articles.service';
import {existEmail} from '../validators/exists-email.validator';
import {validEmail} from '../validators/correct-email.validator';
import {ActivatedRoute, Router} from '@angular/router';
import {ArticleStatus} from '../model/article-status';
import {DataService} from '../../data.service';
import {requiredIf} from '../validators/required-if';
import {MergeDialogComponent} from './merge-dialog.component';
import {requiredIfNot} from '../validators/required-if-not';
import {AuthorDialogComponent} from './author-dialog/author-dialog.component';
import {Author} from '../model/author';
import {EmailService} from '../../../services/email/email.service';
import {FullTextService} from '../../../services/full-text/full-text.service';
import {PdfService} from '../../../services/pdf/pdf.service';
import {fullNameEmail} from '../validators/fullname-email.validator';
import {PubmedService} from '../../../services/pubmed/pubmed.service';
import {environment} from '../../../../environments/environment';


@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css',
    '../../article.component.css']
})
export class DataComponent implements OnInit, OnChanges {

  @Input() stepper: MatHorizontalStepper;
  @Input() article: Article;

  @Output() articleStatus = new EventEmitter<ArticleStatus>();

  formGroup: FormGroup;
  dataFormGroup: FormGroup;
  statusFormGroup: FormGroup;
  collectionOptions: Collection[] = [];
  currentCollection = Collection.ToBeEvaluated;
  validForm: boolean;
  disabled = true;

  private updatingCollectionSender = new Subject<boolean>();
  updatingCollectionReceiver = this.updatingCollectionSender.asObservable();

  percentCompleted = 0;
  percentUploaded = [0];
  isSingleUploaded = false;
  urlAfterUpload = '';
  sent = 0;
  // @ts-ignore
  @ViewChild('divClick') divClick: ElementRef;

  constructor(private searchService: SearchService,
              private articlesService: ArticlesService,
              private dataService: DataService,
              private pubmedService: PubmedService,
              private fullTextService: FullTextService,
              private pdfService: PdfService,
              private agendaService: EmailService,
              private _formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private snackBar: MatSnackBar,
              private router: Router,
              public dialog: MatDialog) {
    this.initForm();


    Object.values(Collection).forEach(value => {
      if (Collection.isSelectableCollection(value)) {
        this.collectionOptions.push(value);
      }
    });
    this.updatingCollectionReceiver.subscribe(updating => {
      if (!updating && this.dataFormGroup.dirty) {
        this.updateData();
      }
      if (!updating && this.statusFormGroup.dirty) {
        this.updateMetadata();
      }
    });
  }

  initForm() {
    this.statusFormGroup = this._formBuilder.group({
      internEvaluation: [false],
      articleStatus: ['', Validators.required],
      currentCollection: [''],
      comment: [''],
      negativeIfNoAnswer: [''],
    }, {validators: requiredIfNot()});

    this.dataFormGroup = this._formBuilder.group({
      title: ['', [Validators.pattern('^((?!…).)*$')]],
      pmid: ['', [Validators.pattern('^[0-9]+')]],
      pmcid: ['', [Validators.pattern('^[0-9]+')]],
      doi: ['', [Validators.pattern('^((?!doi.org).)*$')]],


      notDoi: [''],
      link: [''],
      pdfLink: [''],
      journal: ['', [Validators.pattern('^((?!…).)*$')]],
      publishedDate: [''],
      ocDate: [''],
      evaluatedDate: [''],
      approvedDate: [''],
      authorList: this._formBuilder.array([], [Validators.required])
    });

    this.formGroup = new FormGroup({
      data: this.dataFormGroup,
      status: this.statusFormGroup,
    }, [existEmail(), validEmail(), requiredIf(), fullNameEmail()]);

  }

  ngOnInit() {
  }

  ngOnChanges(changes) {
    if (changes.article.currentValue) {
      this.fillForm();
    }
  }

  fillForm() {
    let articleStatus = this.article.status;
    if ((articleStatus === Collection.Evaluated
        || articleStatus === Collection.NeuroMorphoEvaluated
        || articleStatus === Collection.ToBeEvaluated)
      && this.article.metadata && this.article.metadata.articleStatus) {
      articleStatus = this.article.metadata.articleStatus;
    }
    if (this.currentCollection === Collection.ToBeEvaluated && articleStatus === Collection.Inaccessible) {
      this.disabled = false;

    }
    this.statusFormGroup.setValue({
      internEvaluation: this.article.status === Collection.Evaluated ||
        this.article.status === Collection.ToBeEvaluated ||
        this.article.status === Collection.NeuroMorphoEvaluated ||
        this.article.status === Collection.NeuroMorpho,
      articleStatus: articleStatus,
      currentCollection: this.article.status,
      comment: this.article.metadata && this.article.metadata.comment
        ? this.article.metadata.comment : '',
      negativeIfNoAnswer: this.article.metadata && this.article.metadata.negativeIfNoAnswer
        ? this.article.metadata.negativeIfNoAnswer : false,
    });

    this.dataFormGroup.setValue({
      title: this.article.data.title,
      pmid: this.article.data.pmid,
      pmcid: this.article.data.pmcid,
      doi: this.article.data.doi,
      notDoi: this.article.status === Collection.ToBeEvaluated ? false : this.article.data.doi === null,
      link: this.article.data.link,
      pdfLink: this.article.data.pdfLink,
      journal: this.article.data.journal,
      publishedDate: this.article.data.publishedDate,
      ocDate: this.article.data.ocDate,
      evaluatedDate: this.article.data.evaluatedDate,
      approvedDate: this.article.data.approvedDate,
      authorList: []

    });
    this.fillAuthorList(this.article.data.authorList);
  }


  async fillAuthorList(dataAuthorList): Promise<void> {
    const authorFormList = this.dataFormGroup.get('authorList') as FormArray;

    while (authorFormList.length !== 0) {
      authorFormList.removeAt(0);
    }
    dataAuthorList.forEach(author => {
      const formAuthor = this._formBuilder.group({
        name: author.name,
        emailList: [[]],
        contactId: author.contactId,
        contact: null
      });
      authorFormList.push(formAuthor);
    });
    for (const element of authorFormList.controls) {
      if (element.value.contactId !== null) {
        const contact = await this.agendaService.findContact(element.value.contactId);
        const emailList = contact.emailList.map(email => email.email);
        element.patchValue({
          contact: contact,
          emailList: emailList,
        });
      }
    }
  }

  addAuthor() {
    this.dataFormGroup.markAsDirty();
    const authorList = this.dataFormGroup.get('authorList') as FormArray;
    authorList.push(this._formBuilder.group({
      name: '',
      emailList: [[]],
      contactId: null,
      contact: null
    }));
  }

  editAuthor(i: number) {
    const authorList = this.dataFormGroup.get('authorList') as FormArray;
    this.openAuthorDialog(authorList.at(i).value, i);
  }

  deleteAuthor(i: number) {
    const authorList = this.dataFormGroup.get('authorList') as FormArray;
    authorList.removeAt(i);
    this.dataFormGroup.markAsDirty();
  }

  deleteEmail(i: number) {
    const authorList = this.dataFormGroup.get('authorList') as FormArray;
    authorList.at(i).patchValue({
      contactId: null});
    this.dataFormGroup.markAsDirty();
  }

  openAuthorDialog(author: Author, i: number): void {
    const dialogRef = this.dialog.open(AuthorDialogComponent, {data: author});
    dialogRef.afterClosed().subscribe(result => {
      if (result != null) {
        if (result.id == null) {
          this.agendaService.createContact(result).subscribe(
            p => {
              const authorList = this.dataFormGroup.get('authorList') as FormArray;
              authorList.at(i).setValue({
                emailList: p.emailList.map(email => email.email),
                name: p.firstName + ' ' + p.lastName,
                contactId: p.id,
                contact: p
              });
            },
            error => this.snackBar.open('Error adding contact', 'Error'));
        } else {
          const authorList = this.dataFormGroup.get('authorList') as FormArray;
          authorList.at(i).setValue({
            emailList: result.emailList.map(email => email.email),
            name: result.firstName + ' ' + result.lastName,
            contactId: result.id,
            contact: result
          });
        }
        this.dataFormGroup.markAsDirty();
      }
    });
  }

  get portalList() {
    return this.dataFormGroup.get('portalList') as FormArray;
  }

  get authorList() {
    return this.dataFormGroup.get('authorList') as FormArray;
  }

  navigate(where) {
    let url = '';
    switch (where) {
      case 'pubmed': {
        url = 'https://www.ncbi.nlm.nih.gov/pubmed/' + this.dataFormGroup.get('pmid').value;
        break;
      }
      case 'pubmedcentral': {
        url = 'https://www.ncbi.nlm.nih.gov/pmc/articles/PMC' + this.dataFormGroup.get('pmcid').value;
        break;
      }
      case 'doi': {
        url = 'http://dx.doi.org/' + this.dataFormGroup.get('doi').value;
        break;
      }
      case 'link': {
        url = this.dataFormGroup.get('link').value;
        break;
      }
      case 'pdfLink': {
        url = this.dataFormGroup.get('pdfLink').value;
        break;
      }
    }
    window.open(url, '_blank');
  }

  retrieveArticleDataFromPubMed(db) {

    let pmid = this.dataFormGroup.get('pmid').value;
    if (db === 'pmc') {
      pmid = this.dataFormGroup.get('pmcid').value;
    }
    this.pubmedService.findByPMID(pmid, db)
      .subscribe(articleData => {

        this.dataFormGroup.markAsDirty();
        if (this.article === undefined) {
          this.article = new Article;
          this.article.data = articleData;
        } else {
          if (articleData.authorList.length === this.article.data.authorList.length) {
            for (let i = 0; i < articleData.authorList.length; i++) {
              this.article.data.authorList[i].name = articleData.authorList[i].firstName +
                ' ' + articleData.authorList[i].lastName;
              this.article.data.authorList[i].emailList = [];
              this.article.data.authorList[i].emailList.push(articleData.authorList[i].email);
            }
          } else {
            this.article.data.authorList = articleData.authorList;
            for (let i = 0; i < articleData.authorList.length; i++) {
              this.article.data.authorList[i].name = articleData.authorList[i].firstName +
                ' ' + articleData.authorList[i].lastName;
              this.article.data.authorList[i].emailList = [];
              this.article.data.authorList[i].emailList.push(articleData.authorList[i].email);
            }
          }
          this.article.data.title = articleData.title;
          this.article.data.pmid = articleData.pmid;
          this.article.data.doi = articleData.doi;
          this.article.data.pmcid = articleData.pmcid;
          this.article.data.journal = articleData.journal;
          this.article.data.publishedDate = articleData.publishedDate;
        }
        this.dataFormGroup.get('title').setValue(this.article.data.title);
        this.dataFormGroup.get('pmid').setValue(this.article.data.pmid);
        this.dataFormGroup.get('pmcid').setValue(this.article.data.pmcid);
        this.dataFormGroup.get('doi').setValue(this.article.data.doi);
        this.dataFormGroup.get('journal').setValue(this.article.data.journal);
        this.dataFormGroup.get('publishedDate').setValue(new Date(this.article.data.publishedDate));
        if (this.dataFormGroup.get('pmid').value === undefined) {
          this.dataFormGroup.get('portalList').setValue([{'manual': 'Article added manually'}]);
        }
        this.fillAuthorList(this.article.data.authorList);
      });

  }

  retrieveArticleDataFromCrossRef() {
    this.searchService.findByDOI(this.dataFormGroup.get('doi').value)
      .subscribe(articleData => {
        this.dataFormGroup.markAsDirty();
        if (this.article === undefined) {
          this.article = new Article;
          this.article.data = articleData;
        } else {
          if (articleData.authorList.length === this.article.data.authorList.length) {
            for (let i = 0; i < articleData.authorList.length; i++) {
              this.article.data.authorList[i].name = articleData.authorList[i].name;
            }
          } else {
            this.article.data.authorList = articleData.authorList;
          }
          this.article.data.title = articleData.title;
          this.article.data.doi = articleData.doi;
          this.article.data.journal = articleData.journal;
          this.article.data.publishedDate = articleData.publishedDate;
        }
        this.dataFormGroup.get('title').setValue(this.article.data.title);
        this.dataFormGroup.get('pmid').setValue(this.article.data.pmid);
        this.dataFormGroup.get('pmcid').setValue(this.article.data.pmcid);
        this.dataFormGroup.get('doi').setValue(this.article.data.doi);
        this.dataFormGroup.get('journal').setValue(this.article.data.journal);
        this.dataFormGroup.get('publishedDate').setValue(new Date(this.article.data.publishedDate));
        if (this.dataFormGroup.get('pmid').value === undefined) {
          this.dataFormGroup.get('portalList').setValue([{'manual': 'Article added manually'}]);
        }
        this.fillAuthorList(this.article.data.authorList);
      });
  }

  save() {
    if (this.formGroup.valid) {
      if (this.statusFormGroup.dirty) {
        let newCollection: Collection;
        if (this.statusFormGroup.get('internEvaluation').value) {

          if (this.article != null &&
            (this.article.status === Collection.NeuroMorpho
              || this.article.status === Collection.NeuroMorphoEvaluated)) {
            newCollection = Collection.NeuroMorphoEvaluated;
          } else if (this.statusFormGroup.get('articleStatus').value === Collection.ToBeEvaluated &&
            this.currentCollection === Collection.ToBeEvaluated) {
            newCollection = Collection.ToBeEvaluated;
          } else {
            newCollection = Collection.Evaluated;

          }
        } else {
          newCollection = this.statusFormGroup.get('articleStatus').value;
        }
        const id = this.route.snapshot.paramMap.get('id');
        if (id === 'new') {
          this.saveNewArticle(newCollection);
        } else if (newCollection !== this.article.status) {
          this.updatingCollectionSender.next(true);
          this.dataFormGroup.get('evaluatedDate').setValue(new Date());
          this.articlesService.updateCollection(this.article.id, newCollection, null).subscribe(
            p => this.updatingCollectionSender.next(false),
            error => this.snackBar.open('Error updating collection', 'Error'));
        } else {
          this.updatingCollectionSender.next(false);
        }
      } else {
        this.updatingCollectionSender.next(false);
      }
      this.stepper.next();
    }
  }

  saveNewArticle(collection: Collection) {
    this.article = new Article();
    this.article.searchPortal = {'Manual': ['article added manually']};
    this.article.data = this.dataFormGroup.value;
    this.article.metadata = this.statusFormGroup.value;
    this.article.status = collection;
    this.articlesService.saveArticle(this.article)
      .subscribe(
        article => {
          this.article = article, this.dataService.sendArticle({'article': this.article, 'emitter': 'data'});
        },
        error => {
          const values = error.error.errorMessage.split(';');
          const id = values[1].split('id: ')[1];
          const link = environment.apiUrl + `/article/${id}/details`;
          this.snackBar.open('Error saving new article - Duplicate resourced found in ' + error.error.errorMessage, 'View Article')
            .onAction()
            .subscribe(() =>
              window.open(link, '_blank'));
            // this.router.(`http://localhost:4200/article/60f599ca2b8cb7052425e9da//article/${id}`));
        });
  }

  updateData() {
    Object.assign(this.article.data, this.dataFormGroup.value);
    this.article.data.authorList.map(author => {
      delete author.contact;
      delete author.emailList;
    });

    this.articlesService.update(this.article.id, 'data', this.article.data)
      .subscribe(
        res => null,
        error => {
          if (error.status === 409) {
            this.articlesService.update(this.article.id, 'data.authorList', this.article.data.authorList).subscribe();
            this.openMergeDialog(error);
          } else {
            this.snackBar.open('Error updating article data', 'Error');
          }
        }
      );
  }


  updateMetadata() {
    if (this.article.metadata != null) {
      Object.assign(this.article.metadata, this.statusFormGroup.value);
    } else {
      this.article.metadata = this.statusFormGroup.value;
    }
    this.articlesService.update(this.article.id, 'metadata', this.article.metadata)
      .subscribe(
        res => null,
        error => this.snackBar.open('Error updating article metadata', 'Error')
      );
  }

  emitArticleStatus() {
    const articleStatus = {
      'collection': this.statusFormGroup.get('articleStatus').value,
      'internEvaluation': this.statusFormGroup.get('internEvaluation').value
    };
    this.articleStatus.emit(articleStatus);
  }

  lockArticle() {
    this.articlesService.update(this.article.id, 'locked', {'locked': this.article.locked})
      .subscribe(res => null,
        error => this.snackBar.open('Error locking article', 'Error')
      );
  }

  openMergeDialog(error): void {
    const dialogRef = this.dialog.open(MergeDialogComponent, {data: {error: error.error.errorMessage, article: this.article}});
    dialogRef.afterClosed().subscribe(result => {
      this.stepper.next();
    });
  }

  isValidForm(): boolean {
    return (this.article != null && !this.article.locked); // not previously locked
  }


  extractText($event) {
    this.sent = 1;
    const formData = new FormData();
    formData.append('file', $event.target.files[0]);

    const id = this.route.snapshot.paramMap.get('id');
    this.pdfService.uploadWithProgress(id, formData)
      .subscribe(text => {
          this.fullTextService.saveFullText(id, text.text).subscribe(saved => {
              this.sent = 2;
              this.article.data.fulltext = 'RAWTEXT';
            },
            error => {
              this.sent = 3;
              this.snackBar.open('Error extracting text for the article', 'Error');
            });
          /*console.log(event.type === HttpEventType.UploadProgress)
          if (event.type === HttpEventType.UploadProgress) {
            this.percentCompleted = Math.round(100 * event.loaded / event.total);
          } else if (event instanceof HttpResponse) {
            this.isSingleUploaded = true;
            this.urlAfterUpload = event.body.link;
          }*/
        },
        error => {
          this.sent = 3;
          this.snackBar.open('Error extracting text for the article', 'Error');
        }
      );
  }

  hasFullText() {
    return (this.article != null && (this.article.data.fulltext === 'RAWTEXT' || this.article.data.fulltext === ' FULLTEXT'));
  }

  openContact(author: Author, position: number) {

    const openedWindow = window.open(`agenda/contacts/${author.contactId}`, '_blank');
    const _that = this;

    openedWindow.onbeforeunload = function (e) {
      console.log('Executes after close');
      _that.agendaService.findContactSync(author.contactId)
        .subscribe(contact => {
          const authorList = _that.dataFormGroup.get('authorList') as FormArray;
          let i = 0;
          for (const element of authorList.controls) {
            if (i === position) {
              const emailList = contact.emailList.map(email => email.email);
              element.patchValue({
                contact: contact,
                emailList: emailList,
                name: contact.firstName + ' ' + contact.lastName,

              });
              _that.formGroup.updateValueAndValidity();
              const el = _that.divClick.nativeElement;
              el.click();
            }
            i++;
          }
        });
    };
  }

}
