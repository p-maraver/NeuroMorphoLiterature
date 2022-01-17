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

public class SearchResultsContent {
    
    @JsonProperty("entry")
    private List<Entry> entryList;

    @JsonProperty("link")
    private List<Link> linkList;

    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    public List<Link> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Link> linkList) {
        this.linkList = linkList;
    }

    public Integer getEntryListSize(){
        return this.entryList.size();
    }

    public Boolean isEmpty(){
        return this.entryList == null || (this.entryList.get(0).getError()!= null && 
                this.entryList.get(0).getError().contains("was empty"));
    }
}
