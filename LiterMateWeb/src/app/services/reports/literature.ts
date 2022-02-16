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
