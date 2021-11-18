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

package org.neuromorpho.literature.api.service.dto;

import org.neuromorpho.literature.api.model.Shared;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ArticleDto {

    private String id;

    private String pmid;
    private String pmcid;

    private String doi;
    private String link;

    private String journal;

    private String title;
    private Date evaluatedDate;
    private Date publishedDate;

    private List<String> authorList;

    private List<String> dataUsage;
    private Map<String, Object> metadata;
    private String specificDetails;
    private Double nReconstructions;
    private String globalStatus;
    private String collection;
    private List<Shared> sharedList;


    public ArticleDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<String> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<String> authorList) {
        this.authorList = authorList;
    }

    public List<String> getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(List<String> dataUsage) {
        this.dataUsage = dataUsage;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getSpecificDetails() {
        return specificDetails;
    }

    public void setSpecificDetails(String specificDetails) {
        this.specificDetails = specificDetails;
    }

    public Double getnReconstructions() {
        return nReconstructions;
    }

    public void setnReconstructions(Double nReconstructions) {
        this.nReconstructions = nReconstructions;
    }

    public String getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(String globalStatus) {
        this.globalStatus = globalStatus;
    }

    public Date getEvaluatedDate() {
        return evaluatedDate;
    }

    public void setEvaluatedDate(Date evaluatedDate) {
        this.evaluatedDate = evaluatedDate;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public List<Shared> getSharedList() {
        return sharedList;
    }

    public void setSharedList(List<Shared> sharedList) {
        this.sharedList = sharedList;
    }
    

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id='" + id + '\'' +
                ", pmid='" + pmid + '\'' +
                ", pmcid='" + pmcid + '\'' +
                ", doi='" + doi + '\'' +
                ", link='" + link + '\'' +
                ", journal='" + journal + '\'' +
                ", title='" + title + '\'' +
                ", evaluatedDate=" + evaluatedDate +
                ", publishedDate=" + publishedDate +
                ", authorList=" + authorList +
                ", dataUsage=" + dataUsage +
                ", metadata=" + metadata +
                ", specificDetails='" + specificDetails + '\'' +
                ", nReconstructions=" + nReconstructions +
                ", globalStatus='" + globalStatus + '\'' +
                '}';
    }
}
