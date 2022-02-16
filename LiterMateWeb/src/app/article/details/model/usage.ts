import {Collection} from './collection';


export enum Usage {
  Using = 'USING',
  Describing = 'DESCRIBING_NEURONS',
  Citing = 'CITING',
  About = 'ABOUT',
  Sharing = 'SHARING',
  Duplicate = 'DUPLICATE'

}

export namespace Usage {
  export function getUsage(value: string) {
    const usage = Object.keys(Usage)[Object.values(Usage).indexOf(value)];
    return usage;
  }

  export function isClickable(usage: Usage) {
    switch (usage) {
      case Usage.Using:
      case Usage.Describing:
      case Usage.Citing:
      case Usage.About:
      case Usage.Sharing:
      case Usage.Duplicate:
        return true;
      default:
        return false;
    }
  }
  export function getUsages() {
    return [Usage.Using, Usage.Citing, Usage.About];
  }
}
