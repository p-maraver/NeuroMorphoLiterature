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

package org.neuromorpho.literature.search.dto.article;

import java.time.LocalDate;
import java.util.List;

public class Article {

    private String pmid;
    private String pmcid;
    private String title;
    private String link;
    private String pdfLink;
    private String journal;
    private String doi;
    private LocalDate publishedDate;
    private List<Author> authorList;


    public Article() {
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPmcid() {
        return pmcid;
    }

    public void setPmcid(String pmcid) {
        this.pmcid = pmcid;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    @Override
    public String toString() {
        return "Article{" +
                "pmid='" + pmid + '\'' +
                ", pmcid='" + pmcid + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", pdfLink='" + pdfLink + '\'' +
                ", journal='" + journal + '\'' +
                ", doi='" + doi + '\'' +
                ", publishedDate=" + publishedDate +
                ", authorList=" + authorList +
                '}';
    }

    public enum DB {
        PUBMED("pubmed"),
        PUBMEDCENTRAL("pmc");

        private final String db;

        private DB(String u) {
            db = u;
        }

        public static DB getDB(String value) {
            for (DB v : values()) {
                if (v.getDB().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException();
        }

        public String getDB() {
            return db;
        }

    }
}
