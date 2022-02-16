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

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AngularEditorConfig} from '@kolkov/angular-editor';
import {MatSnackBar} from '@angular/material';
import {Email} from '../../services/email/email';
import {EmailService} from '../../services/email/email.service';
import {Template} from '../model/template';


@Component({
  selector: 'app-templates',
  templateUrl: './templates.component.html',
  styleUrls: ['./templates.component.css']
})
export class TemplatesComponent implements OnInit {

  updated = false;
  template: Email;
  form: FormGroup;
  templateTypes: string[];
  templateType: string;

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
              private snackBar: MatSnackBar) {
    this.form = this._formBuilder.group({
      subject: [''],
      content: [''],
      type: ['']
    });
    this.templateTypes = Object.values(Template);
    this.templateType = Template.inviteAuthor;
    this.getTemplate();

  }

  ngOnInit() {
    this.getTemplate();
  }

  getTemplate(): void {
    let type;

    const keys = Object.keys(Template);
    keys.forEach(key => {
      if (Template[key] === this.templateType) {
        type = key;
      }
    });
    this.emailService.findTemplate(type)
      .subscribe(template => {
        this.template = template;
        this.form.setValue({
          subject: template.subject,
          content: template.content,
          type: template.type
        });
      });

  }

  update(): void {
    const template = Object.assign(this.template, this.form.value);
    this.emailService.updateTemplate(template)
      .subscribe(
        data => this.updated = true,
        error => {
          this.snackBar.open('Error updating template', 'Error');
        });
  }

}
