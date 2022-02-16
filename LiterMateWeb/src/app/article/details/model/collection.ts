

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


