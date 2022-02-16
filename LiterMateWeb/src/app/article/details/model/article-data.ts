
import {Author} from './author';
import {Usage} from './usage';

export class ArticleData {
  pmid: string;
  pmcid: string;
  doi: string;
  link: string;
  pdfLink: string;
  journal: string;
  title: string;
  authorList: Author[];
  publishedDate: Date;
  ocDate: Date;
  evaluatedDate: Date;
  approvedDate: Date;
  dataUsage: Usage[];
  available: boolean;
  bulkEmail: boolean;
  fulltext: string;

}
