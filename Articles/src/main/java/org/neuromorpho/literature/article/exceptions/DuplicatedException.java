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

package org.neuromorpho.literature.article.exceptions;

import org.bson.types.ObjectId;
import org.neuromorpho.literature.article.model.article.Article;

public class DuplicatedException extends RuntimeException {

    public DuplicatedException(Article.ArticleStatus collection, ObjectId id) {
        super("collection: " + collection.getStatus() + "; id: " + id.toString());
    }
}
