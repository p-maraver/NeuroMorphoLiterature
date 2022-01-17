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

package org.neuromorpho.literature.search.service.springernature.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Record {

    @JsonProperty("title")
    private String title;

    @JsonProperty("creators")
    private List<Creator> creatorList;

    @JsonProperty("url")
    private List<Url> urlList;

    @JsonProperty("publicationName")
    private String journal;

    @JsonProperty("doi")
    private String doi;

    @JsonProperty("publicationDate")
    private String publicationDate;

    public String getTitle() {
        return title;
    }
    
    public String getJournal() {
        return journal;
    }

    public String getDoi() {
        return doi;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public List<Creator> getCreatorList() {
        return creatorList;
    }

    public String getLink() {
        for (Url url: urlList){
            if (url.getFormat().equals("pdf")){
                return url.getValue();
            }
        }
        return null;
    }
}
