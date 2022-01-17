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

package org.neuromorpho.literature.article.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.article.exceptions.ExceptionAssigningReconstructions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {

    @BsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private ArticleData data;

    private ArticleStatus status;

    private HashMap<String, Set<String>> searchPortal;

    private Map<String, Object> metadata;

    private Reconstructions reconstructions;

    private List<Shared> sharedList;

    private String pdf;

    private Float distance;

    private Double score;

    private Boolean locked;

    private Classifier classifier;

    private Duplicate duplicate;

    public Article() {
    }

    public Article(ObjectId id, ArticleData data) {
        this.id = id;
        this.data = data;
    }

    public Article(ArticleData data, ArticleStatus status) {
        if (status.equals(ArticleStatus.NEUROMORPHO)) {
            data.createDataUsage(ArticleData.DataUsage.CITING);
        } else {
            data.createDataUsage(ArticleData.DataUsage.DESCRIBING_NEURONS);
        }
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
        return this.metadata != null ? this.metadata.get(key) : null;
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

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @BsonIgnore
    public Boolean isEmptyEvaluatedDate() {
        return this.data.isEmptyEvaluatedDate();
    }


    @BsonIgnore
    public List<Author> getAuthorList() {
        return this.data.getAuthorList();
    }

    @BsonIgnore
    public void updateCollection(ArticleStatus status, ReconstructionsStatus.SpecificDetails specificDetails) {
        try {
            if (this.data.isEmptyEvaluatedDate()) {
                this.data.setEvaluatedDate(LocalDate.now());
            }
            if (status.isPositive()) {
                this.data.setApprovedDate(LocalDate.now());
                if (this.data.getDataUsage().contains(ArticleData.DataUsage.DESCRIBING_NEURONS) &&
                        this.getMetadataValue("nReconstructions") != null &&
                        (Integer) this.getMetadataValue("nReconstructions") != 0 &&
                        (this.reconstructions == null || !this.reconstructions.exists())) {
                    Reconstructions reconstructions = new Reconstructions();
                    this.setReconstructions(reconstructions.initializeReconstructionsStatus(
                            Double.valueOf((Integer) this.getMetadataValue("nReconstructions")), specificDetails));
                }
            }
        } catch (Exception e) {
            log.error("Error assigning new reconstructions", e);
            throw new ExceptionAssigningReconstructions("Error assigning new reconstructions");
        }
    }

    @BsonIgnore
    public void update2NextReconstructionsStatus(String reconstructionsStatus, Boolean isNegativeIfNoAnswer) {
        this.reconstructions.update2Next(reconstructionsStatus, isNegativeIfNoAnswer);
    }

    @BsonIgnore
    public Boolean hasDoi() {
        return this.data.hasDoi();
    }

    @BsonIgnore
    public Boolean hasPmid() {
        return this.data.hasPmid();
    }

    @BsonIgnore
    public Boolean hasPmcid() {
        return this.data.hasPmcid();
    }

    @BsonIgnore
    public Boolean hasPublishedDate() {
        return this.data.hasPublishedDate();
    }


    @BsonIgnore
    public Boolean hasJournal() {
        return this.data.hasJournal();
    }

    @BsonIgnore
    public Boolean isRxivJournal() {
        return this.data.isRxivJournal(this.data.getJournal());
    }

    @BsonIgnore
    public Boolean hasTitle() {
        return !this.data.hasTitle();
    }

    @BsonIgnore
    public Long countEmailList() {
        return this.data.countEmailList();
    }

    @BsonIgnore
    public Integer countAuthorList() {
        return this.data.countAuthorList();
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @BsonIgnore
    public void initialize() {
        this.data.setOcDate(LocalDate.now());
        this.data.setEvaluatedDate(LocalDate.now());
        this.data.setApprovedDate(LocalDate.now());
        List<ArticleData.DataUsage> dataUsageList = new ArrayList<>();
        dataUsageList.add(ArticleData.DataUsage.DESCRIBING_NEURONS);
        this.data.setDataUsage(dataUsageList);
    }

    @BsonIgnore
    public void setOcDate() {
        this.data.setOcDate(LocalDate.now());
    }


    public Float getDistance() {
        return distance;
    }

    @BsonIgnore
    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public void updateSearchPortal(String portalName, String keyWord) {
        if (this.searchPortal == null) {
            this.searchPortal = new HashMap();
        }
        this.searchPortal.computeIfAbsent(portalName, k -> new HashSet<>()).add(keyWord);

    }

    public void updateCurrentStatusList(List<ReconstructionsStatus> currentStatusList) {
        if (this.reconstructions == null) {
            this.reconstructions = new Reconstructions();
        }
        this.reconstructions.updateCurrentStatusList(currentStatusList);
    }

    @BsonIgnore
    public ArticleStatus getMetadataStatus() {
        return ArticleStatus.getArticleStatus((String) this.metadata.get("articleStatus"));
    }

    @BsonIgnore
    public Boolean hasCurrentReconstructions() {
        return reconstructions != null && reconstructions.getCurrentStatusList() != null && reconstructions.getCurrentStatusList().size() > 0;
    }

    @BsonIgnore
    public Boolean hasSharedReconstructions() {
        return sharedList != null && sharedList.size() > 0;
    }

    @BsonIgnore
    public boolean similarData(Article article) {
        return this.data.similarData(article.data, article.getScore());
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

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public Duplicate getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(Duplicate duplicate) {
        this.duplicate = duplicate;
    }

    @BsonIgnore
    @JsonIgnore
    public ObjectId getDuplicateId() {
        return this.duplicate.getDuplicateId();

    }

    @BsonIgnore
    @JsonIgnore
    public Boolean isNegativeIfNoAnswer() {
        return (this.metadata.get("negativeIfNoAnswer") != null &&
                this.metadata.get("negativeIfNoAnswer").equals(Boolean.TRUE));
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
                ", pdf='" + pdf + '\'' +
                ", distance=" + distance +
                ", score=" + score +
                ", locked=" + locked +
                ", classifier=" + classifier +
                ", duplicate=" + duplicate +
                '}';
    }

    @BsonIgnore
    public List<String> getAuthorListStr() {
        try {
            return this.data.getAuthorList().stream().map(author -> author.getName()).collect(Collectors.toList());
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @BsonIgnore
    public String getTitle() {
        return data.getTitle() == null ? "" : data.getTitle();
    }

    @BsonIgnore
    public String publishedDate() {
        return data.getPublishedDate() == null ? "" : data.getPublishedDate().toString();
    }

    @BsonIgnore
    public String getDoi() {
        return data.getDoi() == null ? "" : data.getDoi();
    }

    @BsonIgnore
    public String getPmid() {
        return data.getPmid() == null ? "" : data.getPmid();
    }

    @BsonIgnore
    public String getPmcid() {
        return data.getPmcid() == null ? "" : data.getPmcid();
    }

    @BsonIgnore
    public String getJournal() {
        return data.getJournal() == null ? "" : data.getJournal();
    }

    @BsonIgnore
    public String getTextType() {
        return data.getFulltext() == null ? "" : data.getFulltext();
    }

    @BsonIgnore
    public List<ArticleData.DataUsage> getUsage() {
        return data.getDataUsage() == null ? new ArrayList<>() : data.getDataUsage();
    }

    @BsonIgnore
    public List<String> getKeyWords() {
        Set<String> keyWordList = new HashSet<>();

        try {
            Set<String> keyWordSet = this.searchPortal.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
            for (String keyword : keyWordSet) {
                String[] keywordArray = keyword.split(" OR | AND");
                keyWordList.addAll(Arrays.asList(keywordArray));
            }
            return new ArrayList<>(keyWordList);
        } catch (NullPointerException ex) {
//            log.info("Article without portal for id: " + this.id);
        }
        return new ArrayList<>()
                ;
    }

    @BsonIgnore
    public List<String> getPortalNameList() {
        try {
            Set<String> portalNameList = this.searchPortal.keySet();
            return new ArrayList<>(portalNameList);
        } catch (NullPointerException ex) {
//            log.info("Article without portal for id: " + this.id);
        }
        return new ArrayList<>();
    }

    @BsonIgnore
    public Boolean hasText() {
        return this.data.hasText();
    }
}
