import {ArticleData} from './article-data';
import {Metadata} from './metadata';
import {Reconstructions} from './reconstructions';
import {Collection} from './collection';
import {Classifier} from './classifier';

export class Article {
  id: string;
  data: ArticleData;
  metadata: Metadata;
  reconstructions: Reconstructions;
  sharedList: Article[];
  duplicate: Article;
  searchPortal: { [key: string]: string[]; };
  status: Collection;
  locked: boolean;
  classifier: Classifier;
  changed: boolean;
  updated: boolean;

}
