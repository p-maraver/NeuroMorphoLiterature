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

package org.neuromorpho.literature.search.crossref.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;


public class Message {
    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @JsonProperty("DOI")
    private String doi;
    
    @JsonProperty("title")
    private List<String> titleList;
    
    @JsonProperty("link")
    private List<Link> linkList;
    
    @JsonProperty("author")
    private List<Author> authorList;
    
    @JsonProperty("publisher")
    private String publisher;
    
    @JsonProperty("container-title")
    private List<String> journalList;

    @JsonProperty("institution")
    private List<Institution> institution;

    @JsonProperty("created")
    private Created created;

    public String getDoi() {
        return this.doi;
    }

    public String getTitle() {
        return this.titleList.get(0);
    }

    public String getXmlLink() {
        for (Link link: this.linkList){
            if (link.getContentType().equals("application/xml")
            || link.getContentType().equals("text/xml")){
                return link.getUrl();
            }
        }
        return null;
    }

    public String getHtmlLink() {
        for (Link link: this.linkList){
            if (link.getContentType().equals("text/html")){
                return link.getUrl();
            }
        }
        return null;
    }

    public String getPdfLink() {
        try {
            for (Link link : this.linkList) {
                if (link.getContentType().equals("application/pdf")) {
                    log.debug("PDF link available in crossRef: " + link.getUrl());
                    return link.getUrl();
                }
            }
        } catch (NullPointerException ex){
        }
        return null;
    }

    public List<String> getAuthorList() {
        return this.authorList.stream().map(author -> author.getAuthorName()).collect(Collectors.toList());
    }

    public String getJournal() {
        if (this.journalList != null && this.journalList.size() > 0) {
             return this.journalList.get(0);
        } else if (this.institution != null && this.institution.size()>0){
            return this.institution.get(0).getName();
        } else if (this.publisher != null){
            return this.publisher;
        } else {
            return null;
        }
    }

    public String getPublishedDate() {
        return this.created.getPublishedDate();
    }
}
