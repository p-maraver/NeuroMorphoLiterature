import {FullText} from '../../article/full-text/model/full-text';
import {Article} from '../../article/details/model/article';
import {Author} from '../../article/details/model/author';


export class Literature {
  pmid: string;
  pmcid: string;
  doi: string;
  journal: string;
  title: string;
  fulltext: FullText;
  authorList: Author[];

  constructor(article: Article, fulltext: FullText) {
    this.pmid = article.data.pmid;
    this.pmcid = article.data.pmcid;
    this.doi = article.data.doi;
    this.journal = article.data.journal;
    this.title = article.data.title;
    this.authorList = article.data.authorList;
    this.fulltext = fulltext;
  }
}
