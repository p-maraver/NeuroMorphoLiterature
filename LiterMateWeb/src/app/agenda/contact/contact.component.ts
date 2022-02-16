import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {validEmail} from '../validators/correct-email.validator';
import {MatSnackBar} from '@angular/material';
import {ArticlesService} from '../../article/details/services/articles.service';
import {EmailService} from '../../services/email/email.service';


@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  formGroup: FormGroup;
  visible = true;
  selectable = true;
  removable = true;
  addOnBlur = true;

  constructor(private contactService: EmailService,
              private articleService: ArticlesService,
              private route: ActivatedRoute,
              private _formBuilder: FormBuilder,
              private snackBar: MatSnackBar,
              private router: Router) {
    this.formGroup = this._formBuilder.group({
      firstName: [''],
      lastName: [''],
      emailList: this._formBuilder.array([], [Validators.required, validEmail()]),
      unsubscribed: ['']
    });
  }


  ngOnInit() {
    this.getContact();
  }

  async getContact(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id');

    const contact = await this.contactService.findContact(id);
    this.formGroup.setValue({
      firstName: contact.firstName,
      lastName: contact.lastName,
      emailList: [],
      unsubscribed: contact.unsubscribed
    });
    this.fillEmailList(contact.emailList);
  }

  update(): void {
    const id = this.route.snapshot.paramMap.get('id');

    this.contactService.updateContact(id, this.formGroup.value)
      .subscribe(
        contact => {
          this.articleService.updateContactsId(contact).subscribe();
          window.close();
        },
        error => this.snackBar.open('Error updating author details', 'Error')
      );
  }

  close(): void {
    window.close();
  }

  fillEmailList(emailList) {
    const emailFormList = this.formGroup.get('emailList') as FormArray;

    while (emailFormList.length !== 0) {
      emailFormList.removeAt(0);
    }
    emailList.forEach(email => {
        const formEmail = this._formBuilder.group({
          email: email.email,
          bounced: email.bounced
        });
        emailFormList.push(formEmail);
      }
    );
  }

  get emailList() {
    return this.formGroup.get('emailList') as FormArray;
  }

  deleteEmail(i: number) {
    this.formGroup.markAsDirty();
    const emailList = this.formGroup.get('emailList') as FormArray;
    emailList.removeAt(i);
  }

  addEmail() {
    this.formGroup.markAsDirty();
    const emailList = this.formGroup.get('emailList') as FormArray;
    emailList.push(this._formBuilder.group({
      email: '',
      bounced: false,
    }));

  }

}
