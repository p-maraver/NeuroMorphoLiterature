

export class Classifier {
  id: string;
  version: string;
  date: Date;
  trainSamples: number;
  testSamples: number;
  trainLoss: number[];
  trainAccuracy: number[];
  testLoss: number;
  testAccuracy: number;
  current: boolean;
  status: Status;
  chartData: any;
  chartDataPositives: any;
  chartDataNegatives: any;
  thresholds: Thresholds;
  classList: number[][];
  keywordList: string[];

}

export class Thresholds {
  positiveH: number;
  positiveL: number;
  negativeH: number;
  negativeL: number;
  positiveLaborSavedH: number;
  positiveLaborSavedL: number;
  negativeLaborSavedH: number;
  negativeLaborSavedL: number;
  falseNegativesL: number;
  falseNegativesH: number;
  falsePositivesL: number;
  falsePositivesH: number;
}

export enum Status {
  DUMPING_TEXT = 'Dumping text from Positive and Negative articles ...',
  EXTRACTING_RELEVANT_TEXT = 'Extracting relevant text ...',
  TRAINING = 'Training ...',
  TRAINED = 'Trained',
}
export namespace Classifier {

  export function getStatus(value: string) {
    const status = Object.keys(Status)[Object.values(Status).indexOf(value)];
    return status;
  }
}


