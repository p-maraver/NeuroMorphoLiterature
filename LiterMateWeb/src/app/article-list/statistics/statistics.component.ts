import {Component, OnInit} from '@angular/core';
import {ArticlesService} from '../../article/details/services/articles.service';
import {Usage} from '../../article/details/model/usage';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  reconstructions;
  usageList = [];

  constructor(private articlesService: ArticlesService,
  ) {
  }

  ngOnInit() {
    this.articlesService.countStatusDetails(false).subscribe(result => {
      this.reconstructions = result;
    });

    this.articlesService.countUsage().subscribe(resultList => {

      Usage.getUsages().forEach(usage => {
        let total = 0;
        let net = 0;
        let multi = 0;
        resultList.forEach(result => {
          if (result._id.includes(usage)) {
            total = total + result.count;
            net = net + result.count / result._id.length;
            if (result._id.length !== 1) {
              multi = multi + result.count;
            }
          }
        });
        const key = usage;
        this.usageList[key] = {};
        this.usageList[key] = {'total': total, 'net': net, 'multi': multi};
      });
    });
  }

  getNArticles(details) {
    if (this.reconstructions != null) {
      return this.reconstructions.filter(r => r.specificDetails === details)[0];
    }
  }

  getSumNArticles(field, details) {
    if (this.reconstructions != null) {
      return this.reconstructions.filter(r => r.status === details).map(el => el[field])
        .reduce((totalValue, currentValue) => totalValue + currentValue, 0);
    }
  }

}
