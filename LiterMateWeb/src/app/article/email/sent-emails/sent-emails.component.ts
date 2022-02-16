import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Email} from '../../../services/email/email';
import {EmailService} from '../../../services/email/email.service';

@Component({
  selector: 'app-sent-emails',
  templateUrl: './sent-emails.component.html',
  styleUrls: ['./sent-emails.component.css']
})
export class SentEmailsComponent implements OnInit {

  emailList: Email[];
  constructor(private emailService: EmailService,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.findEmailList();
  }

  findEmailList(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.emailService.findEmailList(id)
      .subscribe(emailList => {
        this.emailList = emailList;
      });

  }
}
