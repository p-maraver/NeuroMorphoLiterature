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

import {Component, Input, OnInit} from '@angular/core';
import {MatChipInputEvent, MatSnackBar, MatSnackBarConfig} from '@angular/material';
import {FormBuilder, FormGroup} from '@angular/forms';
import {COMMA, ENTER, SEMICOLON} from '@angular/cdk/keycodes';
import {AngularEditorConfig} from '@kolkov/angular-editor';
import {ActivatedRoute} from '@angular/router';
import {Article} from '../../details/model/article';
import {ArticlesService} from '../../details/services/articles.service';
import {DataService} from '../../data.service';
import {filter} from 'rxjs/operators';
import {Email} from '../../../services/email/email';
import {EmailService} from '../../../services/email/email.service';

@Component({
  selector: 'app-new',
  templateUrl: './new-email.component.html',
  styleUrls: ['./new-email.component.css', '../email.component.css']
})
export class NewEmailComponent implements OnInit {

  private configSucces: MatSnackBarConfig = {
    panelClass: ['style-success'],
  };
  article: Article;
  form: FormGroup;
  newEmail: Email;
  sentEmailList: Email[];
  sent = 0;
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA, SEMICOLON];
  emailTypes: String[] = ['neuronal', 'glia'];
  emailType = 'neuronal';
  available = false;

  editorConfig: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    height: '25rem',
    minHeight: '5rem',
    placeholder: 'Enter text here...',
    translate: 'no',
    uploadUrl: 'v1/images', // if needed
    customClasses: [ // optional
      {
        name: 'quote',
        class: 'quote',
      },
      {
        name: 'redText',
        class: 'redText'
      },
      {
        name: 'titleText',
        class: 'titleText',
        tag: 'h1',
      },
    ]
  };

  constructor(private _formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private emailService: EmailService,
              private articleService: ArticlesService,
              private dataService: DataService,
              private snackBar: MatSnackBar) {
    this.form = this._formBuilder.group({
      to: [''],
      cc: [''],
      subject: [''],
      content: [''],
      type: ['']
    });
  }

  ngOnInit() {
    this.article = this.dataService.getArticle();
    if (this.article !== undefined) {
      this.extractType();
      this.generateNewEmail();
    } else {
      this.dataService.article$.pipe(
        filter(articleObserver => articleObserver.emitter !== 'email')
      ).subscribe(articleObserver => {
        this.article = articleObserver.article;
        this.extractType();
        this.generateNewEmail();
      });
    }
  }

  extractType(): void {
    const cellType = this.article.metadata.cellType != null ?
      this.article.metadata.cellType.toString().toLowerCase() : 'neuron';
    if (cellType.indexOf('glia') !== -1 || cellType.indexOf('rocyte') !== -1) {
      this.emailType = 'glia';
    }
  }

  generateNewEmail(): void {
    const id = this.route.snapshot.paramMap.get('id');
    const available = JSON.parse(this.route.snapshot.queryParamMap.get('available')) || this.available;
    const ancient = this.article.data.publishedDate.getFullYear() <= 2015;
    if (this.article.reconstructions != null &&
      this.article.reconstructions.reconstructionsList != null &&
      this.article.reconstructions.reconstructionsList[0].statusDetails != null) {
      this.emailService.generateEmail(id, this.article, this.emailType, ancient, available)
        .subscribe(newEmail => {
          this.newEmail = newEmail;
          this.form.setValue({
            to: newEmail.to,
            cc: newEmail.cc,
            subject: newEmail.subject,
            content: newEmail.content,
            type: newEmail.type
          });
        });
    }

  }

  send(): void {
    const email = Object.assign(this.newEmail, this.form.value);
    this.sent = 1;
    this.emailService.sendEmail(email)
      .subscribe(
        data => this.sentSuccess(),
        error => {
          this.snackBar.open('Error sending email', 'Error');
          this.sent = 0;
        });
  }

  sentSuccess(): void {
    const id = this.route.snapshot.paramMap.get('id');
    const statusDetails = this.article.reconstructions.reconstructionsList[0].statusDetails;
    this.articleService.update2NextStatus(id, statusDetails)
      .subscribe(
        data => this.sent = 2,
        error => this.snackBar.open('Error transitioning to next status', 'Error'));
  }

  add(event: MatChipInputEvent, type: string): void {
    const input = event.input;
    const value = event.value;
    if ((value || '').trim()) {
      this.form.get(type).value.push(value.trim());
    }
    if (input) {
      input.value = '';
    }
  }

  remove(key: string, type: string): void {
    const index = this.form.get(type).value.indexOf(key);
    if (index >= 0) {
      this.form.get(type).value.splice(index, 1);
    }
  }

}
