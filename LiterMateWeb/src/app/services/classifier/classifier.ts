

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


