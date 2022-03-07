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

import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {ArticlesService} from '../article/details/services/articles.service';
import {ArticlePage} from '../article/details/model/article-page';
import {Collection} from '../article/details/model/collection';
import {Subscription} from 'rxjs';
import {MatSidenav, MatSnackBar, MatTable} from '@angular/material';
import {Usage} from '../article/details/model/usage';
import {Article} from '../article/details/model/article';
import {EmailService} from '../services/email/email.service';
import {ReportsService} from '../services/reports/reports.service';
import {FullTextService} from '../services/full-text/full-text.service';
import {ExternalService} from '../services/external/external.service';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent implements OnInit, OnDestroy {

  @ViewChild(MatTable, {static: true}) table: MatTable<any>;
  @ViewChild('sidenav', {static: false}) sidenav: MatSidenav;

  displayedColumns: string[] = [];
  userSubscription: Subscription;

  articlePage: ArticlePage;
  sortDirection = 'DESC';
  sortProperty = 'data.publishedDate';
  page = 0;
  text = '';
  view = 'articles';
  collection: Collection;
  collectionOptions: Collection[] = [];
  usageList: string[] = [];
  usage: string;
  creating = false;
  exists = false;
  file: string;

  statusDetailsList: string[] = [];
  status: string;


  count = [];
  countStatusExpired = [];
  countStatusNotExpired = [];
  available = false;
  date = 'All';
  sent = 0;
  nSent = 0;


  constructor(
    private articlesService: ArticlesService,
    private emailService: EmailService,
    private reportsService: ReportsService,
    private fullTextService: FullTextService,
    private externalService: ExternalService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar) {

    Object.values(Collection).forEach(value => {
      if (Collection.isClickableCollection(value)) {
        this.collectionOptions.push(value);
      }
    });
    Object.values(Usage).forEach(value => {
      if (Usage.isClickable(value)) {
        this.usageList.push(Usage.getUsage(value));
      }
    });

  }

  ngOnInit() {
    this.userSubscription = this.route.queryParams.subscribe(
      (params: ParamMap) => {
        this.updateView(params);
      });
    this.userSubscription = this.route.params.subscribe(
      (params: ParamMap) => {
        this.updateView(params);
      });
    this.countArticles();
  }

  updateView(params) {
    this.collection = Collection[this.route.snapshot.paramMap.get('collection')];
    this.status = this.route.snapshot.queryParamMap.get('reconstructions.currentStatusList.specificDetails');
    this.date = this.route.snapshot.queryParamMap.get('data.publishedDate');
    this.view = this.route.snapshot.queryParamMap.get('view');
    this.usage = this.route.snapshot.queryParamMap.get('data.dataUsage');

    this.initContainer();
    this.getArticleList();
    if (this.collection === 'Positive') {
      this.countStatusDetails();
      this.getStatusDetails();
    }
    this.reportsService.exists(this.collection)
      .subscribe(
        data => {
          this.file = this.collection + '.csv';
          this.exists = true;
        },
        error => {
          if (error.status === 200) {
            this.file = this.collection + '.csv';
            this.exists = true;
          }
        });

  }

  ngOnDestroy() {
    this.userSubscription.unsubscribe();
  }


  updatePage(page: number) {
    this.page = page;
    this.getArticleList();
  }

  updateFilter(filter: string) {
    this.text = filter;
    this.getArticleList();
  }

  updateSort(active: string, direction: string) {
    this.sortDirection = direction.toUpperCase();
    this.sortProperty = active;
    this.getArticleList();

  }

  async getArticleList(): Promise<void> {
    let searchText = 'page=' + this.page +
      '&sortDirection=' + this.sortDirection +
      '&sortProperty=' + this.sortProperty;
    if (this.text != null && this.text !== '') {
      if (this.text.includes('@')) {
        // Search for the name on contacts
        const contactList = await this.emailService.findContactListAsync(0, 10, this.text);
        if (contactList.content.length > 0) {
          searchText = searchText + '&data.authorList.contactId=' + contactList.content[0].id;
        }
      } else {
        searchText = searchText + '&text=' + this.text;
      }
    }
    const collection = Collection[this.route.snapshot.paramMap.get('collection')];

    if (collection !== Collection.All) {
      this.route.snapshot.queryParamMap.keys.forEach(param => {
        if (param !== 'view' && this.route.snapshot.queryParamMap.get(param) !== 'All') {
          searchText = searchText + '&' + param + '=' + this.route.snapshot.queryParamMap.get(param);
        }
      });
    }

    this.articlesService.findAllByText(
      collection,
      searchText)
      .subscribe(articlePage => {
        this.articlePage = articlePage;
        this.fillData();
      });
  }

  countArticles(): void {
    this.articlesService.count()
      .subscribe(result => {
        result.All = result.Evaluated + result.Inaccessible + result.Negative
          + result.Neuromorpho + result.Positive + result['Pending evaluation'];
        this.count = result;
      });
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

  getStatusDetails(): void {
    this.articlesService.getReconstructionsStatusValues()
      .subscribe(result => {
        this.statusDetailsList = result;
      });
  }

  findStatusDetails(status): number {
    let resultNotExpired = null;
    this.countStatusNotExpired.forEach(x => {
      if (x.specificDetails === status) {
        resultNotExpired = x;
      }
    });
    if (resultNotExpired === null) {
      return 0;
    } else {
      return resultNotExpired.nArticles;
    }
  }

  initContainer() {
    if (this.collection === Collection.ToBeEvaluated && this.view !== 'classifier') {
      this.displayedColumns = [
        'collection',
        'locked',
        'data.publishedDate',
        'data.ocDate',
        'data.pmid',
        'data.title'
      ];
    } else if (this.collection === Collection.Evaluated && this.view !== 'classifier') {
      this.displayedColumns = [
        'locked',
        'data.publishedDate',
        'data.evaluatedDate',
        'data.pmid',
        'data.title',
        'metadata',
        'comment',
        'accept'
      ];
    } else if (this.view === 'classifier') {
      this.displayedColumns = [
        'locked',
        'data.publishedDate',
        'data.evaluatedDate',
        'data.pmid',
        'data.title',
        'metadata',
        'comment',
        'classifier.confidence',
        'classifier'
      ];
    } else if (this.collection === Collection.NeuroMorphoEvaluated) {
      this.displayedColumns = [
        'dataUsage',
        'locked',
        'data.publishedDate',
        'data.evaluatedDate',
        'data.pmid',
        'data.title',
        'comment',
        'accept'
      ];
    } else if (this.collection === Collection.Positive && this.view === 'emails') {
      this.displayedColumns = [
        'reconstructions.expirationDate',
        'reconstructions.status',
        'data.publishedDate',
        'data.pmid',
        'data.title',
        'authors',
        'generateEmail'
      ];
    } else if (this.collection === Collection.Positive && this.view !== 'emails') {
      this.displayedColumns = [
        'collection',
        'dataUsage',
        'data.publishedDate',
        'data.pmid',
        'data.title',
        'reconstructions.nReconstructions',
        'reconstructions.status',
        'comment',
        'submitStatus'
      ];
    } else {
      this.displayedColumns = [
        'collection',
        'dataUsage',
        'locked',
        'data.ocDate',
        'data.publishedDate',
        'data.evaluatedDate',
        'data.pmid',
        'data.title'
      ];
    }

  }

  containsCollection(collection: Collection) {
    return (this.collection === collection);
  }

  containsStatusDetails(status) {
    return (this.status === status);
  }

  containsUsage(usage) {
    return (this.usage === usage);
  }

  dateChange(value) {
    this.date = value;
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {'data.publishedDate': value},
        queryParamsHandling: 'merge'
      });

  }

  collectionChange(value) {
    this.collection = value;
    this.router.navigate(
      [`/articles/${Collection.getCollection(value)}`],
      {
        relativeTo: this.route,
        queryParamsHandling: 'merge'
      });
  }

  statusDetailsChange(value) {
    this.status = value;
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {'reconstructions.currentStatusList.specificDetails': value},
        queryParamsHandling: 'merge'
      });
  }

  usageChange(value) {
    this.usage = value;
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {'data.dataUsage': value},
        queryParamsHandling: 'merge'
      });
  }


  async accept(index, id, collection, article): Promise<void> {
    let bounced = true;
    if (collection === Collection.Positive) {
      for (const author of article.data.authorList) {
        if (author.contactId != null) {
          const contact = await this.emailService.findContact(author.contactId);
          contact.emailList.forEach(email => {
            if (!email.bounced) {
              bounced = false;
            }
          });
        }
      }
    }
    console.log('Article bounced: ' + bounced);

    let details = null;
    if (bounced && article.metadata != null && article.metadata.negativeIfNoAnswer) {
      details = 'Bounced Negative';
    } else if (bounced && article.metadata != null && !article.metadata.negativeIfNoAnswer) {
      details = 'Bounced';
    }
    console.log('Article status: ' + details);

    this.articlesService.updateCollection(id, collection.replace(/ H| L/g, ''), details)
      .subscribe(
        res => {
          this.articlePage.content.splice(index, 1);
          this.articlePage.numberOfElements--;
          this.articlePage.totalElements--;
          this.table.renderRows();
          this.countArticles();
        },
        error => {
          this.snackBar.open('Error accepting article', 'Error');
        });
  }

  updateMetadata(id, classifier): void {
    if (classifier.metadata == null) {
      classifier.metadata = {};
    }
    classifier.metadata.comment = 'Automated classification result: ' + classifier.articleStatus +
      ' with confidence: ' + classifier.confidence + '%';
    classifier.metadata.articleStatus = classifier.articleStatus.replace(/ *\([^)]*\) */g, '');
    classifier.metadata.nReconstructions = 1;
    this.articlesService.update(id, 'metadata', classifier.metadata)
      .subscribe(
        res => null,
        error => this.snackBar.open('Error updating article metadata', 'Error')
      );
  }

  getUsage(dataUsage): string[] {
    const usageList = [];
    dataUsage.forEach(usage => {
      Object.keys(Usage).forEach(key => {
        if (usage === Usage[key]) {
          usageList.push(key);
        }
      });
    });
    return usageList;
  }

  showKeyWords(searchPortal): string[] {
    if (searchPortal != null) {
      const keyWordList = new Set();
      const merged = [].concat.apply([], Object.values(searchPortal));
      merged.forEach(value => {
        let replacedValue = value.replace(/ AND | OR "/g, ' ');
        replacedValue = replacedValue.replace(/(|)/g, '');

        // value.replace('/ OR /g', ' ').replace('/ AND /g', ' ').
        //                           replace('/"/g', '').replace('/(/g', '').replace('/)/g', '');
        let arr = replacedValue.match(/(".*?"|[^" \s]+)(?=\s* |\s*$)/g);
        arr = arr || [];
// this will prevent JS from throwing an error in
// the below loop when there are no matches
        for (let i = 0; i < arr.length; i++) {
          keyWordList.add(arr[i]);
        }
      });
      return Array.from(keyWordList);
    } else {
      return Array.from([]);

    }
  }

  close() {
    this.sidenav.close();
  }

  metadataMissingClassifier(article): boolean {
    let existMail = false;
    article.data.authorList.forEach(author => {
      if (author.contactId != null) {
        existMail = true;
      }
    });
    if (article.classifier == null) {
      return true;
    }
    return article.classifier != null &&
      (article.classifier.articleStatus != null &&
        article.classifier.articleStatus.includes('Positive') &&
        article.data.dataUsage.includes(Usage.Describing) &&
        article.classifier.metadata != null &&
        article.classifier.metadata.tracingSystem != null &&
        !existMail);
  }

  metadataMissingIntern(article: Article): boolean {
    return article.metadata != null &&
      (article.metadata.articleStatus === undefined ||
        article.metadata.articleStatus != null &&
        article.metadata.articleStatus === 'Positive' &&
        article.data.dataUsage.includes(Usage.Describing) &&
        (
          article.metadata.cellType == null ||
          article.metadata.nReconstructions == null ||
          article.metadata.tracingSystem == null
        )
      );
  }

  getKeyWordList(keyWordListMap): string[] {
    return keyWordListMap != null ? Object.keys(keyWordListMap) : [];
  }


  describingNeurons(dataUsage) {
    return dataUsage.includes(Usage.Describing);
  }

  appendAQueryParam() {
    const urlTree = this.router.createUrlTree([], {
      queryParams: {newParamKey: 'newValue'},
      queryParamsHandling: 'merge',
      preserveFragment: true
    });

    this.router.navigateByUrl(urlTree);
  }

  fillData() {
    if (this.view === 'emails') {
      this.articlePage.content.forEach(article => {
        article.data.bulkEmail = true;
        article.data.authorList.forEach(author => {
          author.statuses = [];
          if (author.contactId != null) {
            article.data.available = false;
            this.articlesService.findAllByText('Positive', `data.authorList.contactId=${author.contactId}`).subscribe(
              res => {
                let toBeSend = false;
                res.content.forEach(a => {
                  if (a.reconstructions != null && a.reconstructions.reconstructionsList != null) {
                    a.reconstructions.reconstructionsList.forEach(function (rec) {
                      if (rec.statusDetails !== 'No response') {
                        author.statuses.push(rec.statusDetails);
                        if (rec.statusDetails === 'In repository' ||
                          rec.statusDetails === 'In processing pipeline' ||
                          rec.statusDetails === 'On hold' ||
                          rec.statusDetails === 'In release') {
                          article.data.available = true;
                        }
                      }
                    });
                  }
                });
                let count = 0;
                author.statuses.forEach(status => {
                  if (status === 'To be requested' ||
                    status === 'Positive response' ||
                    status === 'Invited' ||
                    status === 'Reminder' ||
                    status === 'Pestering' ||
                    status === 'Ultimatum' ||
                    status === 'In processing pipeline' ||
                    status === 'In release'
                  ) {
                    count++;
                  }
                });
                const reconstructionData = article.reconstructions.reconstructionsList.filter(
                  value => value.statusDetails === this.status);

                if (new Date(reconstructionData[0].expirationDate) <= new Date()) {
                  toBeSend = true;
                }
                if (count > 1 || !toBeSend) {
                  article.data.bulkEmail = false;
                }
              });
          }
        });
      });
    }
  }

  getNEmails() {
    if (this.articlePage != null) {
      return this.articlePage.content.filter(article => article.data.bulkEmail)
        .reduce((sum, current) => {
          return (sum + 1);
        }, 0);
    } else {
      return 0;
    }
  }

  sendBulkEmails(index) {
    this.sent = 1;
    const articleList = this.articlePage.content.slice(index, index + 1);
    if (articleList.length > 0) {
      const article = articleList[0];
      this.sendEmail(article, index);
    } else {
      this.sent = 2;
    }
  }

  sendEmail(article, index) {
    if (article.data.bulkEmail) {
      const ancient = new Date(article.data.publishedDate).getFullYear() <= 2013;
      const cellType = article.metadata.cellType.toString().toLowerCase();
      let emailType = 'neurons';
      if (cellType.indexOf('glia') !== -1 || cellType.indexOf('rocyte') !== -1) {
        emailType = 'glia';
      }
      this.emailService.generateAndSendEmail(article, emailType, ancient, article.data.available)
        .subscribe(
          res => {
            this.articlePage.content.splice(index, 1);
            this.articlePage.totalElements--;
            this.articlePage.numberOfElements--;
            const statusDetails = article.reconstructions.reconstructionsList[0].statusDetails;
            this.sent = 2;
            this.nSent++;
            this.articlesService.update2NextStatus(article.id, statusDetails)
              .subscribe(
                data => {
                  this.table.renderRows();
                  if (index < this.articlePage.numberOfElements) {
                    this.sendBulkEmails(index);
                  }
                },
                error => this.snackBar.open('Error transitioning to next status', 'Error'));
          },
          error => {
            this.sent = 0;
            this.snackBar.open(`Error sending email: ${article.id}`, 'Error');
          });
    } else {
      index++;
      this.sendBulkEmails(index);
    }
  }

  collectionChangeIntern(value) {
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {'metadata.articleStatus': value},
        queryParamsHandling: 'merge'
      });

  }

  collectionChangeClassifier(value) {
    this.router.navigate(
      [],
      {
        relativeTo: this.route,
        queryParams: {'classifier.articleStatus': value},
        queryParamsHandling: 'merge'
      });

  }

  submitStatus(article: Article) {
    this.articlesService.update(article.id, 'reconstructions', article.reconstructions)
      .subscribe(
        data => {
          article.updated = true;
          if (article.reconstructions.reconstructionsList[0].statusDetails === 'In processing pipeline' ||
            article.reconstructions.reconstructionsList[0].statusDetails === 'Positive response') {
            this.metadataCall(article);
          }
        },
        error => this.snackBar.open('Error updating article status details', 'Error'));
  }

  updated(article: Article) {
    article.changed = true;
  }

  generateReport() {
    this.creating = true;
    this.reportsService.delete(this.collection)
      .subscribe(
        data => {
          this.exists = false;
          this.reportsService.generate(this.collection)
            .subscribe(
              data2 => {
                this.creating = false;
                this.exists = true;
              },
              error => {
              });
        },
        error => {
        });
  }

  metadataCall(article) {
    this.fullTextService.findFullText(article.id)
      .subscribe(fullText => {
        this.externalService.sendData(article, fullText)
          .subscribe(
            res => {
              this.snackBar.open(`Metadata API correctly called`, 'Success');
            },
            error => {
              console.log(error);
              this.snackBar.open(`Error calling metadata webhook: ${error.statusText}`, 'Error');
            });
      });
  }


}
