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
