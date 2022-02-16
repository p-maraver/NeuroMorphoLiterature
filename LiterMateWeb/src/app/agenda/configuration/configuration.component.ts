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
