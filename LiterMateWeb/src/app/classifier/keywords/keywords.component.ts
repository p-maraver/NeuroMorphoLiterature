import { Component, OnInit } from '@angular/core';
import {MatSnackBar} from '@angular/material';
import {FileUploader} from 'ng2-file-upload';
import {ClassifierService} from '../../services/classifier/classifier.service';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-keywords',
  templateUrl: './keywords.component.html',
  styleUrls: ['./keywords.component.css']
})
export class KeywordsComponent implements OnInit {

  file = 'term';
  keywordList: string;
  updated = false;
  userSubscription: Subscription;

  public uploader: FileUploader = new FileUploader({ url: 'http://localhost:4000/api/upload', itemAlias: 'photo' });

  constructor(private classifierService: ClassifierService,
              private snackBar: MatSnackBar,
              private route: ActivatedRoute) { }

  ngOnInit() {
    this.userSubscription = this.route.queryParams.subscribe(
      (params: ParamMap) => {
        this.readFile(params);
      });
  }

  async readFile(params) {
    this.file = this.route.snapshot.queryParamMap.get('file');
    const result = await this.classifierService.readFile(this.file);
    this.keywordList = result['text'];
    // if (response.status !== 200) {
    //   this.snackBar.open('Error accessing file', 'Error');
    // }

  }
  async updateFile(file) {
    await this.classifierService.updateFile(this.file, this.keywordList);
    this.updated = true;

  }
}
