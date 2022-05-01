import {Component, OnInit} from '@angular/core';
import {ReleaseVersion} from '../services/release/release-version';
import {Portal} from '../services/portal-search/model/portal';
import {ArticlesService} from '../article/details/services/articles.service';
import {ReleaseService} from '../services/release/release.service';
import {PortalSearchService} from '../services/portal-search/portal-search.service';
import {EmailStatus} from '../services/email/email-status';

@Component({
  selector: 'app-dashboard2',
  templateUrl: './dashboard2.component.html',
  styleUrls: ['./dashboard2.component.css']
})
export class Dashboard2Component implements OnInit {

  emailOptionsPending = [];
  emailOptionsPendingInterns = [];
  version: ReleaseVersion;
  portalList: Portal[] = [];
  articleNumbers = [];
  total_reconstructions = 0;
  reconstructions = [];
  reconstructionsExpired = [];
  selectedObject;

  constructor(private articlesService: ArticlesService,
              private releaseService: ReleaseService,
              private portalSearchService: PortalSearchService) {
  }

  ngOnInit() {
    this.countStatusDetails();
    this.countArticles();
    this.getReleaseVersion();
    this.getPortalList();

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
        this.reconstructionsExpired = emailsNumbers;
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
    this.articlesService.countStatusDetails(false)
      .subscribe(numbers => {
        this.reconstructions = numbers;
        numbers.forEach(number => {
          this.total_reconstructions = this.total_reconstructions + number.nReconstructions;
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

  getReconstructionsByFilter(filter: string): number {
    const result = this.reconstructions.filter(x => x.specificDetails === filter);
    if (result.length > 0) {
      return result[0].nArticles;
    }
    return 0;
  }

  getReconstructionsExpiredByFilter(filter: string): number {
    const result = this.reconstructionsExpired.filter(x => x.specificDetails === filter);
    if (result.length > 0) {
      return result[0].nArticles;
    }
    return 0;
  }

}
