

export enum Collection {
  All = 'All',
  ToBeEvaluated = 'Pending evaluation',
  Evaluated = 'Evaluated',
  NeuroMorpho = 'Neuromorpho',
  NeuroMorphoEvaluated = 'Neuromorpho evaluated',
  Positive = 'Positive',
  Negative = 'Negative',
  Inaccessible = 'Inaccessible',


}

export namespace Collection {
  export function isSelectableCollection(collection: Collection) {
    switch (collection) {
      case Collection.Positive:
      case Collection.Negative:
      case Collection.Inaccessible:
      case Collection.ToBeEvaluated:
        return true;
      default:
        return false;
    }
  }

  export function isClickableCollection(collection: Collection) {
    switch (collection) {
      case Collection.Positive:
      case Collection.Negative:
      case Collection.Inaccessible:
      case Collection.ToBeEvaluated:
      case Collection.NeuroMorpho:
      case Collection.NeuroMorphoEvaluated:
      case Collection.Evaluated:
      case Collection.All:
        return true;
      default:
        return false;
    }
  }

  export function getCollection(value: string) {
    const collection = Object.keys(Collection)[Object.values(Collection).indexOf(value)];
    return collection;
  }
}


