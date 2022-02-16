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

import {Component, OnInit} from '@angular/core';
import {ParamMap, Router} from '@angular/router';
import {Collection} from './article/details/model/collection';
import {ArticlesService} from './article/details/services/articles.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'LiterMateWeb';
  countStatusExpired = [];
  countStatusNotExpired = [];

  constructor(private route: Router,
              private articlesService: ArticlesService) {
  }

  ngOnInit() {
    this.countStatusDetails();
  }

  countStatusDetails(): void {
    this.articlesService.countStatusDetails(false)
      .subscribe(result => {
        this.countStatusNotExpired = result;
      });
    this.articlesService.countStatusDetails(true)
      .subscribe(result => {
        this.countStatusExpired = result;
      });
  }

  findStatusDetails(status): object {
    let resultExpired;
    this.countStatusExpired.forEach(x => {
      if (x.specificDetails === status) {
        resultExpired = x;
      }
    });
    let resultNotExpired;
    this.countStatusNotExpired.forEach(x => {
      if (x.specificDetails === status) {
        resultNotExpired = x;
      }
    });
    let result;
    if (status !== 'To be requested' && status !== 'Ultimatum') {
      result = `${resultExpired === undefined ? 0 : resultExpired.nArticles}/
      ${resultNotExpired === undefined ? 0 : resultNotExpired.nArticles}
        [${resultExpired === undefined ? 0 : resultExpired.nReconstructions}/
        ${resultNotExpired === undefined ? 0 : resultNotExpired.nReconstructions}]`;
    } else {
      result = `${resultExpired === undefined ? 0 : resultExpired.nArticles}
        [${resultExpired === undefined ? 0 : resultExpired.nReconstructions}]`;
    }
    return result;
  }

  showHeather() {
    if (this.route.url.includes('/article/') &&
      !this.route.url.includes('/new/')) {
      return false;
    }
    return true;
  }
}


