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
