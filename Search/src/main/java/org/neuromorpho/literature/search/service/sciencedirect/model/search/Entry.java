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

public class Entry {
    
    @JsonProperty("prism:doi")
    private String doi;
    @JsonProperty("prism:url")
    private String url;
    @JsonProperty("dc:title")
    private String title;
    @JsonProperty("prism:publicationName")
    private String publicationName;
    @JsonProperty("authors")
    private Authors authors;
    @JsonProperty("prism:coverDate")
    private String coverDate;

    @JsonProperty("error")
    private String error;
    
    

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationName() {
        return publicationName;
    }

    public void setPublicationName(String publicationName) {
        this.publicationName = publicationName;
    }

    public Authors getAuthors() {
        return authors;
    }

    public void setAuthors(Authors authors) {
        this.authors = authors;
    }

    public String getCoverDate() {
        return coverDate;
    }

    public void setCoverDate(String coverDate) {
        this.coverDate = coverDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "doi='" + doi + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", publicationName='" + publicationName + '\'' +
                ", authors=" + authors +
                ", coverDate='" + coverDate + '\'' +
                '}';
    }
}
