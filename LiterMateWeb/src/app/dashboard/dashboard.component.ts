import {Component, OnInit} from '@angular/core';
import {ArticlesService} from '../article/details/services/articles.service';
import {ReleaseVersion} from '../services/release/release-version';
import {ReleaseService} from '../services/release/release.service';
import {PortalSearchService} from '../services/portal-search/portal-search.service';
import {EmailStatus} from '../services/email/email-status';
import {Portal} from '../services/portal-search/model/portal';
import {Collection} from '../article/details/model/collection';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  emailOptionsPending = [];
  emailOptionsPendingInterns = [];
  version: ReleaseVersion;
  portalList: Portal[] = [];

  positivesH: Promise<number>;
  review: Promise<number>;

  inaccessible: Promise<number>;

  notFullText: Promise<number>;
  total: Promise<number>;

  manualIntervention: Promise<number>;
  pendingClassifier: Promise<number>;

  neuromorpho: Promise<number>;
  neuromorphoEvaluated: Promise<number>;
  evaluated: Promise<number>;

  articleNumbers;
  constructor(private articlesService: ArticlesService,
              private releaseService: ReleaseService,
              private portalSearchService: PortalSearchService) {

  }

  ngOnInit() {
    this.countStatusDetails();
    this.countArticles();
    this.getReleaseVersion();
    this.getPortalList();
    // this.getIrrelevant();
    // this.getEvaluated();
    this.evaluated = this.getNumbers(Collection.Evaluated,
      'page=0');
    this.neuromorpho = this.getNumbers(Collection.NeuroMorpho,
  'page=0');
    this.neuromorphoEvaluated = this.getNumbers(Collection.NeuroMorphoEvaluated,
      'page=0');
    this.inaccessible = this.getNumbers(Collection.ToBeEvaluated,
      'page=0&metadata.articleStatus=Inaccessible');

    this.positivesH = this.getNumbers(Collection.ToBeEvaluated,
      'page=0&classifier.articleStatus=Positive H');

    this.review = this.getNumbers(Collection.ToBeEvaluated,
      'page=0&classifier.articleStatus=Review');

    this.total = this.getNumbers(Collection.ToBeEvaluated,
      'page=0');

    this.notFullText = this.getNumbers(Collection.ToBeEvaluated,
      'page=0&fulltext=false&identifiers=true');

    this.manualIntervention = this.getNumbers(Collection.ToBeEvaluated,
      'page=0&identifiers=false&classifier=false');

    this.pendingClassifier = this.getNumbers(Collection.ToBeEvaluated,
      'page=0&classifier=false&identifiers=true');
  }

  async getNumbers(collection, query) {
    const result = await this.articlesService.findAsyncAllByText(collection, query);
    return result.totalElements;
  }

  countArticles(): void {
    this.articlesService.count()
      .subscribe(articleNumbers => {
        this.articleNumbers = articleNumbers;
      });
  }

  countStatusDetails(): void {
    this.articlesService.countStatusDetails(true)
      .subscribe(emailsNumbers => {
        Object.values(EmailStatus).forEach(status => {
          const nArticles = this.findStatusDetails(status, emailsNumbers);
          const emailValue = {'status': status, 'nArticles': nArticles};
          if (status !== EmailStatus.bounced && status !== EmailStatus.bouncedNegative) {
            this.emailOptionsPending.push(emailValue);
          } else {
             this.emailOptionsPendingInterns.push(emailValue);
           }
        });
      });
  }

  findStatusDetails(status, emailsNumbers): number {
    let resultExpired;
    emailsNumbers.forEach(x => {
      if (x.specificDetails === status) {
        resultExpired = x;
      }
    });
    let result;
    result = resultExpired === undefined ? 0 : resultExpired.nArticles;
    return result;
  }

  getReleaseVersion(): void {
    this.releaseService.findVersion()
      .subscribe(version => {
        this.version = version;
      });
  }

  getPortalList(): void {
    this.portalSearchService.findPortalList()
      .subscribe(portalList => {
        this.portalList = portalList;
      });
  }

}

