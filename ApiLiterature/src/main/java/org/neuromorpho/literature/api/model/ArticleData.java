/*
 * Copyright (c) 2015-2021, Patricia Maraver
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

package org.neuromorpho.literature.api.model;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.util.Date;
import java.util.List;


/**
 *  Article data Model. Spring MVC Pattern
 */
public class ArticleData {

    private String pmid;
    private String pmcid;
    private String doi;
    private String link;
    private String journal;
    private String title;
    private List<Author> authorList;
    private Date publishedDate;
    private Date ocDate;
    private Date evaluatedDate;
    private Date approvedDate;
    private List<DataUsage> dataUsage;

    @BsonIgnore
    public Boolean isDescribingNeurons(){
        return this.dataUsage.contains(DataUsage.DESCRIBING_NEURONS);
    }

    public ArticleData() {
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getPmcid() {
        return pmcid;
    }

    public void setPmcid(String pmcid) {
        this.pmcid = pmcid;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getOcDate() {
        return ocDate;
    }

    public void setOcDate(Date ocDate) {
        this.ocDate = ocDate;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public Date getEvaluatedDate() {
        return evaluatedDate;
    }

    public void setEvaluatedDate(Date evaluatedDate) {
        this.evaluatedDate = evaluatedDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public List<DataUsage> getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(List<DataUsage> dataUsage) {
        this.dataUsage = dataUsage;
    }
    
    public enum DataUsage {
        USING("Using"),
        DESCRIBING_NEURONS("Describing"),
        CITING("Citing"),
        ABOUT("About"),
        SHARING("Sharing"),
        DUPLICATE("Duplicate");

        private final String usage;

        private DataUsage(String usage) {
            this.usage = usage;
        }

        public static DataUsage getUsage(String value) {
            for (DataUsage v : values()) {
                if (v.getUsage().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException(value);
        }

        public String getUsage() {
            return usage;
        }

        public Boolean isDescribingNeurons(){
            return this.equals(DataUsage.DESCRIBING_NEURONS);
        }
    }
}
