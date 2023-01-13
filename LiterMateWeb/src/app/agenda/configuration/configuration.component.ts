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
import {EmailService} from '../../services/email/email.service';
import {Config} from '../model/config';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-configuration',
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.css']
})
export class ConfigurationComponent implements OnInit {
  hide = true;
  code: string;

  constructor(private emailService: EmailService,
              private snackBar: MatSnackBar) { }

  async ngOnInit() {
    // this.config = await this.emailService.findConfig();
  }

  async update() {
    await this.emailService.updateCode(this.code);
    this.snackBar.open('Configuration updated correctly', 'Success',
      {panelClass: 'success-dialog'});
  }

  authorizeEmailAccess() {
    // doc: https://learn.microsoft.com/en-us/graph/auth-v2-user
    const url = 'https://login.microsoftonline.com/1516d7e7-7c6d-4e95-9a23-e22c61b6b283/oauth2/v2.0/authorize?' +
      'client_id=a79ac578-4709-4ee4-9d07-99c361ace7d5' +
      '&response_type=code' +
      '&redirect_uri=https://neuromorpho.org/litermate' +
      '&response_mode=query' +
      '&scope=offline_access%20mail.read%20mail.send' +
      '&state=12345';
    window.open(url);
  }

}
