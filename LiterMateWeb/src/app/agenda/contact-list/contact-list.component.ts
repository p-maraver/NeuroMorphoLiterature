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

import { Component, OnInit } from '@angular/core';
import {ContactPage} from '../model/contact-page';
import {MatTableDataSource} from '@angular/material/table';
import {Contact} from '../model/contact';
import {EmailService} from '../../services/email/email.service';
import {PageEvent} from '@angular/material/paginator';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.css']
})
export class ContactListComponent implements OnInit {

  displayedColumns: string[] = ['name', 'emailList', 'unsubscribed'];
  contactPage: ContactPage;
  contactList: MatTableDataSource<Contact>;
  pageIndex = 0;
  pageSize = 25;
  text = null;

  constructor(private contactService: EmailService,
              private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.getContactList();
  }

  getContactList(): void {
    this.contactService.findContactList(this.pageIndex, this.pageSize, this.text)
      .subscribe(contactList => {
        this.contactPage = contactList;
        this.contactList = new MatTableDataSource<Contact>(contactList.content);
      });

  }

  getContactListPage(event: PageEvent): void {
    this.pageSize = event.pageSize;
    this.pageIndex = event.pageIndex;
    this.getContactList();
  }

  applyFilter(event: String): void {
    this.text = event;
    this.getContactList();
  }

  update(contact: Contact, unsubscribed): void {
    contact.unsubscribed = unsubscribed;
    this.contactService.updateContact(contact.id, contact)
      .subscribe();
  }

  open(id: string) {
    const openedWindow = window.open(`agenda/${id}`, '_blank');
  }
}
