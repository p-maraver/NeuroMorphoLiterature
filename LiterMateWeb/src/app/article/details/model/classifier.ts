import {Collection} from './collection';
import {Metadata} from './metadata';

export class Classifier {

  articleStatus: Collection;
  confidence: number;
  keyWordList:  { [key: string]: string[]; };
  termList:  { [key: string]: string[]; };
  metadata: Metadata;
}
