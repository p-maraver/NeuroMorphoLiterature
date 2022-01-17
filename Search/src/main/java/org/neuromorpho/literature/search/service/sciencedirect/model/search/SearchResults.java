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

package org.neuromorpho.literature.search.service.sciencedirect.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SearchResults {
    
    @JsonProperty("search-results")
    private SearchResultsContent results;
    
    public SearchResultsContent getResults() {
        return results;
    }

    public void setResults(SearchResultsContent results) {
        this.results = results;
    }
    
    
    public Integer getResultsSize(){
        return this.results.getEntryListSize();
    }
    
    public List<Entry> getEntryList(){
        return this.results.getEntryList();
    }
    public List<Link> getLinkList(){
        return this.results.getLinkList();
    }
    
    public Boolean isEmpty(){
        return this.results == null || this.results.isEmpty();
    }

}
