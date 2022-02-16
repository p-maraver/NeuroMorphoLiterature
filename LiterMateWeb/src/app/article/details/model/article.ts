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
