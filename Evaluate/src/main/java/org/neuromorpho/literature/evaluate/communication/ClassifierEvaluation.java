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

package org.neuromorpho.literature.evaluate.communication;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.neuromorpho.literature.evaluate.model.Classifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ClassifierEvaluation {

    @BsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Boolean relevant;
    private Float result;
    private String articleStatus;
    private Float confidence;
    private Set<String> keyWordList;
    private Set<String> termList;
    private Map<String, Object> metadata;

    public Boolean getRelevant() {
        return relevant;
    }

    public void setRelevant(Boolean relevant) {
        this.relevant = relevant;
    }

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

    public String getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(String articleStatus) {
        this.articleStatus = articleStatus;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public Set<String> getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(Set<String> keyWordList) {
        this.keyWordList = keyWordList;
    }

    public Set<String> getTermList() {
        return termList;
    }

    public void setTermList(Set<String> termList) {
        this.termList = termList;
    }
    

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean isNegativeHighConfidence() {
        return !this.relevant ||
                this.articleStatus.equals("Negative H");
    }
    @BsonIgnore
    @JsonIgnore
    public Boolean isPositiveHighConfidence() {
        return this.relevant && 
                this.articleStatus.equals("Positive H");
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean containsNeuroMorpho() {
        return this.keyWordList.stream().anyMatch(k -> (k.contains("neuromorph")));
    }


    @BsonIgnore
    @JsonIgnore
    public void createClassifierData(NLP nlp, Classifier classifier) {
        if (this.relevant) {
            this.confidence = result * 100;
            if (this.result > classifier.getThresholds().getPositiveH()) {
                this.articleStatus = "Positive H";  //9 % Erroneous emails
            } else if (this.result > classifier.getThresholds().getPositiveL()) {
                this.articleStatus = "Positive L";   // 13 % Erroneous emails
            } else if (this.result <= classifier.getThresholds().getNegativeH()) {
                this.articleStatus = "Negative H";  // 2 % Positives lost
                this.confidence = 100 - this.result * 100;
            } else if (this.result <= classifier.getThresholds().getNegativeL()) {
                this.articleStatus = "Negative L";   // 5.5 % Positives lost
                this.confidence = 100 - this.result * 100;
            } else {
                this.articleStatus = "Pending evaluation";
                this.confidence = result * 100;
            }
            this.keyWordList = nlp.getKeyword();
            this.termList = nlp.getTerm();
            this.metadata = new HashMap<>();
            List<String> cellType = new ArrayList<>();
            if (nlp.getCellType().size() > 0){
                cellType.add("glia");
            } else {
                cellType.add("neuron");
            }
            this.metadata.put("nReconstructions", 1);
            this.metadata.put("cellType", cellType);
            this.metadata.put("tracingSystem", nlp.getTracingSystem());
            this.metadata.put("articleStatus", this.getArticleStatusMetadata());
        }
    }

    @JsonIgnore
    public String getArticleStatusMetadata() {
        return articleStatus.replace(" H", "").replace(" L", "");
    }

    @Override
    public String toString() {
        return "ClassifierEvaluation{" +
                "relevant=" + relevant +
                ", result=" + result +
                ", articleStatus='" + articleStatus + '\'' +
                ", confidence=" + confidence +
                ", keyWordList=" + keyWordList +
                ", termList=" + termList +
                ", metadata=" + metadata +
                '}';
    }
}
