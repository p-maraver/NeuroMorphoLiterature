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
  config: Config;

  constructor(private emailService: EmailService,
              private snackBar: MatSnackBar) { }

  async ngOnInit() {
    this.config = await this.emailService.findConfig();
  }

  async update() {
    await this.emailService.updateConfig(this.config);
    this.snackBar.open('Configuration updated correctly', 'Success',
      {panelClass: 'success-dialog'});
  }

}
