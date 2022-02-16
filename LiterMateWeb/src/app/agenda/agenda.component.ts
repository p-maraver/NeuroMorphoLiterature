import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from '@angular/material';
import {EmailService} from '../services/email/email.service';

@Component({
  selector: 'app-agenda',
  templateUrl: './agenda.component.html',
  styleUrls: ['./agenda.component.css']
})
export class AgendaComponent implements OnInit {


  download = false;

  constructor(private contactService: EmailService,
              private snackBar: MatSnackBar) { }

  ngOnInit() {
  }

  export(): void {
    this.contactService.exportContacts()
      .subscribe(data =>   this.download = true,
        error => {
          this.snackBar.open('Error exporting contacts', 'Error');
        });

  }
}
