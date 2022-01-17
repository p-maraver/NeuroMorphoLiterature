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

package org.neuromorpho.literature.api.model;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.util.*;

/**
 *  Article information Model. Spring MVC Pattern
 */
public class Article {

    private ObjectId id;

    private ArticleData data;

    private ArticleStatus status;

    private HashMap<String, Set<String>> searchPortal;

    private Map<String, Object> metadata;

    private Reconstructions reconstructions;

    private List<Shared> sharedList;

    private List<Article> sharedInfoList;

    private String pdf;


    public Article() {
    }
    
    public Article(ArticleData data) {
        this.data = data;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ArticleData getData() {
        return data;
    }

    public void setData(ArticleData data) {
        this.data = data;
    }

    public Reconstructions getReconstructions() {
        return reconstructions;
    }

    public List<ReconstructionsStatus> getCurrentStatus() {
        return reconstructions.getCurrentStatusList();
    }

    public void setReconstructions(Reconstructions reconstructions) {
        this.reconstructions = reconstructions;
    }


    public HashMap<String, Set<String>> getSearchPortal() {
        return searchPortal;
    }

    public void setSearchPortal(HashMap<String, Set<String>> searchPortal) {
        this.searchPortal = searchPortal;
    }


    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public Object getMetadataValue(String key) {
        return this.metadata.get(key);
    }


    public List<Shared> getSharedList() {
        return sharedList;
    }

    public void setSharedList(List<Shared> sharedList) {
        this.sharedList = sharedList;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getGlobalStatus() {
        return reconstructions.getGlobalStatus();
    }   
    
    @BsonIgnore
    public void updateCollection(ArticleStatus status) {
        if (status.isEvaluated()) {
            this.data.setEvaluatedDate(new Date());
        }
        if (status.isPositive()) {
            this.data.setApprovedDate(new Date());
            if (this.getMetadataValue("nReconstructions") != null && (Integer) this.getMetadataValue("nReconstructions")!= 0) {
                Reconstructions reconstructions = new Reconstructions();
                this.setReconstructions(reconstructions.initializeReconstructionsStatus(
                        Double.valueOf((Integer) this.getMetadataValue("nReconstructions"))));
            }
        }
    }
    
    @BsonIgnore
    public Boolean hasDoi() {
        return this.data.getDoi() != null;
    }

    @BsonIgnore
    public Boolean hasPmid() {
        return this.data.getPmid() != null;
    }

    @BsonIgnore
    public Boolean hasPmcid() {
        return this.data.getPmcid() != null;
    }

    @BsonIgnore
    public Boolean hasPublishedDate() {
        return this.data.getPublishedDate() != null;
    }

    @BsonIgnore
    public Boolean hasJournal() {
        return this.data.getJournal() != null && !this.data.getJournal().endsWith("…");
    }

    @BsonIgnore
    public Boolean hasTitle() {
        return !this.data.getTitle().endsWith("…");
    }

    @BsonIgnore
    public Boolean hasAuthorList() {
        return this.data.getAuthorList() != null && this.getData().getAuthorList().size() > 0;
    }

    @BsonIgnore
    public void setOcDate() {
        this.data.setOcDate(new Date());
    }

    public List<Article> getSharedInfoList() {
        return sharedInfoList;
    }

    public void setSharedInfoList(List<Article> sharedInfoList) {
        this.sharedInfoList = sharedInfoList;
    }

    public void updateCurrentStatusList(List<ReconstructionsStatus> currentStatusList) {
        if (this.reconstructions == null) {
            this.reconstructions = new Reconstructions();
        }
        this.reconstructions.updateCurrentStatusList(currentStatusList);
    }


    public enum ArticleStatus {
        ALL("all", "All"),
        TO_EVALUATE("article", "Pending evaluation"),
        POSITIVE("article.positives", "Positive"),
        NEGATIVE("article.negatives", "Negative"),
        EVALUATED("article.evaluated", "Evaluated"),
        INACCESSIBLE("article.inaccessible", "Inaccessible"),
        NEUROMORPHO("neuromorpho", "Neuromorpho"),
        NEUROMORPHO_EVALUATED("neuromorpho.evaluated", "Neuromorpho evaluated");

        private String collection;
        private String status;

        ArticleStatus(String s, String a) {
            collection = s;
            status = a;
        }

        public static ArticleStatus getArticleStatus(String value) {
            for (ArticleStatus v : values()) {
                if (v.getStatus().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException(value);
        }

        public Boolean isPositive() {
            return this.equals(ArticleStatus.POSITIVE);
        }

        public Boolean isNegative() {
            return this.equals(ArticleStatus.NEGATIVE);
        }

        public Boolean isEvaluated() {
            return this.equals(ArticleStatus.EVALUATED);
        }

        public Boolean isToEvaluate() {
            return this.equals(ArticleStatus.TO_EVALUATE);
        }

        public Boolean isInaccessible() {
            return this.equals(ArticleStatus.INACCESSIBLE);
        }

        public String getCollection() {
            return this.collection;
        }

        public String getStatus() {
            return this.status;
        }

    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", data=" + data +
                ", status=" + status +
                ", searchPortal=" + searchPortal +
                ", metadata=" + metadata +
                ", reconstructions=" + reconstructions +
                ", sharedList=" + sharedList +
                '}';
    }
}
