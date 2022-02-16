import {Component, OnInit} from '@angular/core';
import {ArticlesService} from '../../article/details/services/articles.service';
import {Collection} from '../../article/details/model/collection';
import {ActivatedRoute} from '@angular/router';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-bulk-status',
  templateUrl: './bulk-status.component.html',
  styleUrls: ['./bulk-status.component.css']
})
export class BulkStatusComponent implements OnInit {

  pmidCommaList: string;
  statusListUpdated = true;
  statusDetailsFrom: string;
  statusDetailsTo: string;
  error = 'Articles not found in DB: ';
  statusDetailsList: string[] = [];

  constructor(private articlesService: ArticlesService,
              private route: ActivatedRoute,
              private snackBar: MatSnackBar
  ) {
  }

  ngOnInit() {
    this.getStatusDetails();
  }

  getStatusDetails(): void {
    this.articlesService.getReconstructionsStatusValues()
      .subscribe(result => {
        this.statusDetailsList = result;
      });
  }

  submitStatusList(): void {
    const pmidList = this.pmidCommaList.trim().replace(/(\r\n|\n|\r)/g, '').split(',');

    pmidList.forEach(pmid => {
      const collection = Collection[this.route.snapshot.paramMap.get('collection')];

      this.articlesService.findAllByText(collection, 'text=' + pmid).subscribe(result => {
        if (result.totalElements === 0) {
          this.error = this.error + pmid + ',';
        }
        const article = result.content[0];
        article.reconstructions.reconstructionsList.forEach(function (status) {
          if (status.statusDetails === this.statusDetailsFrom) {
            status.statusDetails = this.statusDetailsTo;
          }
        });
        this.articlesService.update(article.id, 'reconstructions', article.reconstructions).subscribe(result => {
          this.statusListUpdated = true;
        },
          error => this.snackBar.open('Error updating article status details', 'Error'));
      });
    });
  }

}
