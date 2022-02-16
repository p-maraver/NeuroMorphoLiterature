import {Section} from './section';
import {Bibliography} from './bibliography';
import {AuthorGroup} from './author-group';
import {Figure} from './figure';
import {Term} from './term';
import {SupplementaryMaterial} from './supplementary-material';

export class FullText {
  title: string;
  authorGroup: AuthorGroup;
  abstractContent: string;
  sectionList: Section[];
  figureList: Figure[];
  supplementaryMaterial: SupplementaryMaterial;
  referenceList: Bibliography[];
  acknowledgment: string;
  termList: { [key: string]: Term[]; };
  rawText: string;


}
