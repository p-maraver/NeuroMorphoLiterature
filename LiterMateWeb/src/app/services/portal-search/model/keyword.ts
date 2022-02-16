import {Collection} from '../../../article/details/model/collection';


export class Keyword {
  id: string;
  name: string;
  collection: string;
  executedList: Map<string, number>;

  constructor(name: string) {
    this.name = name;
    this.collection = Collection.ToBeEvaluated;

  }
}

