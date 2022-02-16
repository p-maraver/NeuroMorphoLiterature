import {Component, OnInit} from '@angular/core';
import {Classifier, Status} from '../../services/classifier/classifier';
import {ClassifierService} from '../../services/classifier/classifier.service';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-train',
  templateUrl: './train.component.html',
  styleUrls: ['./train.component.css']
})
export class TrainComponent implements OnInit {

  versionList = [];
  currentVersion = 'v2';
  newVersion = '';
  classifierList: Classifier[];
  training = false;
  updatedVersion = false;
  updatedThresholds = false;
  sortedList = [];
  thresholdListSorted = [];
  thresholdListSortedPositives = [];
  thresholdListSortedNegatives = [];

  public chartLabels = Array.from(Array(30).keys());

  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes: [
        {
          display: true,
          scaleLabel: {
            display: true,
            //             labelString: '% Total emails sent or Positive articles returned by the classifier or Labor saved',
            labelString: 'Accuracy',
          },
        },
      ],
      xAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: 'Iteration',
          },
        },
      ],
    },
  };

  public barChartOptionsPositive = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes: [
        {
          display: true,
          scaleLabel: {
            display: true,
            //             labelString: '% Total emails sent or Positive articles returned by the classifier or Labor saved',
            labelString: '% Labor saved',
          },
        },
      ],
      xAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: '% Incorrect emails sent or False positives',
          },
          ticks: {
            callback: function (value, index, values) {
              return (Math.round(value * 100) / 100).toFixed(2);
            }
          },
        },
      ],
    },
  };

  public barChartOptionsNegative = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes: [
        {
          display: true,
          scaleLabel: {
            display: true,
            //             labelString: '% Negative articles returned by the classifier or Labor saved',
            labelString: '% Labor saved',
          },
        },
      ],
      xAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: '% Lost positive articles or False negatives',
          },
          ticks: {
            callback: function (value, index, values) {
              return (Math.round(value * 100) / 100).toFixed(2);
            }
          },
        },
      ],
    },
  };

  public chartLabelsPositives = [];
  public chartLabelsNegatives = [];

  public chartType = 'line';
  public chartLegend = true;

  constructor(private classifierService: ClassifierService,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.getClassifierList();
  }

  async getClassifierList() {
    this.classifierList = await this.classifierService.findAll();
    this.classifierList.forEach(item => {
      this.versionList.push(item.version);
      if (item.current) {
        this.currentVersion = item.version;
      }
      if (item.trainAccuracy != null) {
        // item.chartData = [
        //   {data: item.trainAccuracy, label: 'Train accuracy'},
        //   {data: item.trainLoss, label: 'Train loss'}
        // ];
      }
      if (item.classList != null) {
        if (item.thresholds === null) {
          item.thresholds = {
            positiveH: 0, positiveL: 0, negativeH: 0, negativeL: 0,
            positiveLaborSavedH: 0, positiveLaborSavedL: 0, negativeLaborSavedH: 0, negativeLaborSavedL: 0,
            falseNegativesL: 0, falseNegativesH: 0, falsePositivesL: 0, falsePositivesH: 0
          };
        }
        this.sortedList = item.classList.sort((x, y) => x[1] - y[1]);
        const thresholdList = item.classList.map(x => x[1]);
        this.thresholdListSorted = thresholdList.filter((x, i, a) => a.indexOf(x) === i);
        this.thresholdListSortedPositives = this.thresholdListSorted.filter((x) => x >= 0.5);
        this.thresholdListSortedNegatives = this.thresholdListSorted.filter((x) => x <= 0.5);

        const totalPositives = item.classList.filter(x => x[0] === 1).length;
        const totalPositivesAbove = item.classList.filter(x => x[1] >= 0.5 && x[0] === 1).length;
        const totalNegatives = item.classList.filter(x => x[0] === 0).length;
        const totalNegativesBelow = item.classList.filter(x => x[1] <= 0.5 && x[0] === 0).length;

        console.log(item.version);
        this.calculatePositiveGraph(item, totalPositives, totalNegatives, totalPositivesAbove);
        this.calculateNegativeGraph(item, totalPositives, totalNegatives, totalNegativesBelow);

        this.calculateGraph(item);
        if (item.thresholds == null) {
          item.thresholds = {
            positiveH: 0, positiveL: 0, negativeH: 0, negativeL: 0,
            positiveLaborSavedH: 0, positiveLaborSavedL: 0, negativeLaborSavedH: 0, negativeLaborSavedL: 0,
            falseNegativesL: 0, falseNegativesH: 0, falsePositivesL: 0, falsePositivesH: 0
          };
        }

      }
    });
  }

  async train() {
    this.training = true;
    await this.classifierService.train(this.newVersion);
  }

  async update(id: string, field: string, object: Object) {
    await this.classifierService.update(id, field, object);
    this.updatedThresholds = true;
  }

  async updateVersion(version: string) {
    const versionNew = this.classifierList.find(element => element.version === version);
    await this.classifierService.update(versionNew.id, 'current', true);
    const versionOld = this.classifierList.find(element => element.current === true);
    await this.classifierService.update(versionOld.id, 'current', false);

    this.updatedVersion = true;

  }

  getStatus(status: Status) {
    return Status[status];
  }

  getValue(accuracy: number[]) {
    return accuracy == null ? null : accuracy[29];
  }

  calculatePositiveGraph(item, totalPositives, totalNegatives, totalPositivesAbove) {
    const laborSaved = [];
    const falsePositives = [];

    // Labor Saved
    for (let i = 0; i < this.thresholdListSortedPositives.length; i++) {
      const value = item.classList.filter(x => x[1] >= this.thresholdListSortedPositives[i] && x[0] === 1).length;
      laborSaved.push(value * 100 / totalPositivesAbove);
      const value2 = item.classList.filter(x => x[1] >= this.thresholdListSortedPositives[i] && x[0] === 0).length;
      falsePositives.push(value2 * 100 / totalNegatives);
    }

    this.chartLabelsPositives = falsePositives;
    item.chartDataPositives = [
      {data: laborSaved, label: 'Labor saved'}
    ];

  }

  calculateNegativeGraph(item, totalPositives, totalNegatives, totalNegativesBelow) {
    const falseNegatives = [];
    const laborSaved = [];
    // Labor Saved
    for (let i = 0; i < this.thresholdListSortedNegatives.length; i++) {
      const value = item.classList.filter(x => x[1] <= this.thresholdListSortedNegatives[i] && x[0] === 0).length;
      laborSaved.push(value * 100 / totalNegativesBelow);
      const value2 = item.classList.filter(x => x[1] <= this.thresholdListSortedNegatives[i] && x[0] === 1).length;
      falseNegatives.push(value2 * 100 / totalPositives);
    }

    this.chartLabelsNegatives = falseNegatives;
    item.chartDataNegatives = [
      {data: laborSaved, label: 'Labor saved'}
    ];
  }

  positiveChartClicked(e: any, classifier: Classifier) {
    if (e.active.length > 0) {
      classifier.thresholds.falsePositivesH = this.chartLabelsPositives[e.active[0]._index + 1];
      classifier.thresholds.positiveLaborSavedH = classifier.chartDataPositives[0].data[e.active[0]._index + 1];
      classifier.thresholds.positiveH = this.thresholdListSortedPositives[e.active[0]._index + 1];
    }
  }

  negativeChartClicked(e: any, classifier: Classifier) {
    if (e.active.length > 0) {
      classifier.thresholds.falseNegativesH = this.chartLabelsNegatives[e.active[0]._index + 1];
      classifier.thresholds.negativeLaborSavedH = classifier.chartDataNegatives[0].data[e.active[0]._index + 1];
      classifier.thresholds.negativeH = this.thresholdListSortedNegatives[e.active[0]._index + 1];
    }
  }

  calculateGraph(item) {
    if (item.trainAccuracy != null) {
      item.chartData = [
        {data: item.trainAccuracy, label: 'Train accuracy'},
        {data: item.trainLoss, label: 'Train loss'}
      ];
    }
  }


}
