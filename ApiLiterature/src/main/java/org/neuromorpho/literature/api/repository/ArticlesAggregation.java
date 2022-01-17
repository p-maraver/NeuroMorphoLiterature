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

package org.neuromorpho.literature.api.repository;

import org.neuromorpho.literature.api.model.Article;

import java.util.List;

/**
 *  Paginated Article Object returned by query
 */
public class ArticlesAggregation {
    
    private List<Article> paginatedResults;
    private List<Count> count;

    public ArticlesAggregation() {
    }

    public List<Article> getPaginatedResults() {
        return paginatedResults;
    }

    public void setPaginatedResults(List<Article> paginatedResults) {
        this.paginatedResults = paginatedResults;
    }

    public List<Count> getCount() {
        return count;
    }

    public void setCount(List<Count> count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ArticlesAggregation{" +
                "paginatedResults=" + paginatedResults +
                ", count=" + count +
                '}';
    }
}
