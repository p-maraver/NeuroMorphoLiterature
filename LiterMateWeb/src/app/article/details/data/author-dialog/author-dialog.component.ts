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

import {Component, Inject, OnInit, ViewChild} from '@angular/core';
import {MAT_DIALOG_DATA, MatChipInputEvent, MatDialogRef, MatSnackBar} from '@angular/material';
import {Author} from '../../model/author';
import {COMMA, ENTER, SEMICOLON, SPACE} from '@angular/cdk/keycodes';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {existEmailContact, validEmailContact} from '../../../../agenda/validators/correct-email.validator';
import {EmailService} from '../../../../services/email/email.service';

@Component({
  selector: 'app-author-dialog',
  templateUrl: './author-dialog.component.html',
  styleUrls: ['./author-dialog.component.css']
})
export class AuthorDialogComponent implements OnInit {

  formGroup: FormGroup;
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;
  readonly separatorKeysCodes: number[] = [ENTER, COMMA, SEMICOLON, SPACE];
  author: Author;
  comment = '';
  link = false;


  constructor(
    public dialogRef: MatDialogRef<AuthorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Author,
    private _formBuilder: FormBuilder,
    private agendaService: EmailService,
    private snackBar: MatSnackBar) {

    const name = data.name.trim();

    this.formGroup = this._formBuilder.group({
      firstName: name.substring(0, name.lastIndexOf(' ') + 1).trim(),
      lastName: name.substring(name.lastIndexOf(' ') + 1, name.length).trim(),
      emailList: this._formBuilder.array([], []),
      id: null
    }, []);
    this.author = data;
    this.formGroup.setValidators([existEmailContact(), validEmailContact()]);

  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close(this.formGroup.value);
  }

  onCancelClick() {
    this.dialogRef.close();
  }

  isInvalid() {
    return this.formGroup.errors != null &&
      (!this.formGroup.errors.validMail
        || !this.formGroup.errors.existMail);
  }

  onLinkClick(): void {
    // Calling agenda
    // this.articlesService.update(this.article.id, 'data.dataUsage', ['DUPLICATE'])
    //   .subscribe(p => this.updateCollection(),
    //     error => this.snackBar.open('Error updating usage', 'Error'));
    // update collection
  }

  remove(key: string): void {
    const index = this.formGroup.value.emailList.indexOf(key);
    if (index >= 0) {
      this.formGroup.value.emailList.splice(index, 1);
      this.formGroup.updateValueAndValidity();
    }
  }

  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;
    if ((value || '').trim()) {
      this.agendaService.findContactList(0, 2, value.trim().toLowerCase()).subscribe(
        contactPage => {
          if (contactPage.totalElements > 0) {
            const contact = contactPage.content[0];
            const firstName = this.formGroup.get('firstName').value;
            const lastName = this.formGroup.get('lastName').value;
            // if same author
            if (contact.firstName.length < firstName.length) {
              // Update contact firstName
            } else {
              this.formGroup.patchValue({
                firstName: contact.firstName,
                lastName: contact.lastName,
              });
            }
            this.formGroup.patchValue({
              id: contact.id
            });
            if (contact.firstName.charAt(0) === firstName.charAt(0)
              && contact.lastName === lastName) {
              this.comment = 'The email is not new, found in the agenda';
              this.link = false;
            } else {
              this.comment = 'The author firstName and lastName don`t match with the agenda ones, ' +
                'please double check. Contact can be found ';
              this.link = true;
            }
            console.log(this.formGroup);
            this.fillEmailList(contact.emailList);
          } else {
            this.formGroup.value.emailList.push({email: value.trim().toLowerCase(), bounced: false});
            this.comment = 'A new contact will be added, the email is new, not found in the agenda';
            // this.formGroup.controls['emailList'].updateValueAndValidity();
          }
          this.formGroup.updateValueAndValidity();

        },
        error => this.snackBar.open('Error retrieving emails from agenda', 'Error'));
      // Reset the input value
    }
    if (input) {
      input.value = '';
    }
  }

  fillEmailList(emailList) {
    const emailFormList = this.formGroup.get('emailList') as FormArray;
    emailList.forEach(email => {
        const formEmail = this._formBuilder.group({
          email: email.email,
          bounced: email.bounced
        });
        emailFormList.push(formEmail);
      }
    );
  }

// this.agendaService.createContact(newContact).subscribe(
//   res => {
//     author.contactId = res.id;
//     if (res.firstName + ' ' + res.lastName) {
//       author.name = res.firstName + ' ' + res.lastName;
//     }
//     author.emailList =
//       res.emailList.filter(email => !email.bounced).map(email => email.email);
//     this.dataFormGroup.patchValue({'authorList' : authorList});
//
//     if (res.replacedListId != null && res.replacedListId.length > 0) {
//       this.articlesService.updateContactsId(res).subscribe(
//         res2 => {
//         },
//         error => {
//           this.snackBar.open(`Error updating merged authors in articles`, 'Error');
//         }
//       );
//     }
//   },
//   error => {
//     this.snackBar.open(`Error updating contact agenda: ${error.error.errorMessage}`, 'Error');
//   }

}
