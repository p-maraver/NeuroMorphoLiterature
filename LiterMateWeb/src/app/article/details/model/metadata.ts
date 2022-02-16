import {Collection} from './collection';


export class Metadata {
  comment: string;
  species: string[];
  cellType: string[];
  brainRegion: string[];
  tracingSystem: string[];
  articleStatus: Collection;
  nReconstructions: number;
  negativeIfNoAnswer: boolean;
}
