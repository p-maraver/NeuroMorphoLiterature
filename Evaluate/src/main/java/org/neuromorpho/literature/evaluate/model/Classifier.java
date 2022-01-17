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

package org.neuromorpho.literature.evaluate.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Classifier {
    @BsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String version;
    private LocalDate date;
    private Integer testSamples;
    private Integer trainSamples;
    private List<Double> trainLoss;
    private List<Double> trainAccuracy;
    private Double testLoss;
    private Double testAccuracy;
    private Boolean current;
    private Status status;
    private Thresholds thresholds;
    private List<List<Double>> classList;
    private List<String> keywordList;

    
    public Classifier() {
    }

    public Classifier(String version) {
        this.version = version;
        this.date = LocalDate.now();
        this.status = Classifier.Status.DUMPING_TEXT;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTestSamples() {
        return testSamples;
    }

    public void setTestSamples(Integer testSamples) {
        this.testSamples = testSamples;
    }

    public Integer getTrainSamples() {
        return trainSamples;
    }

    public void setTrainSamples(Integer trainSamples) {
        this.trainSamples = trainSamples;
    }

    public List<Double> getTrainLoss() {
        return trainLoss;
    }

    public void setTrainLoss(List<Double> trainLoss) {
        this.trainLoss = trainLoss;
    }

    public List<Double> getTrainAccuracy() {
        return trainAccuracy;
    }

    public void setTrainAccuracy(List<Double> trainAccuracy) {
        this.trainAccuracy = trainAccuracy;
    }

    public Double getTestLoss() {
        return testLoss;
    }

    public void setTestLoss(Double testLoss) {
        this.testLoss = testLoss;
    }

    public Double getTestAccuracy() {
        return testAccuracy;
    }

    public void setTestAccuracy(Double testAccuracy) {
        this.testAccuracy = testAccuracy;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<List<Double>> getClassList() {
        return classList;
    }

    public void setClassList(List<List<Double>> classList) {
        this.classList = classList;
    }

    public Thresholds getThresholds() {
        return thresholds;
    }

    public void setThresholds(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    public List<String> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    @BsonIgnore
    @JsonIgnore
    public void updateData(Classifier classifier){
        this.status = Status.TRAINED;
        this.testAccuracy = classifier.testAccuracy;
        this.testLoss = classifier.testLoss;
        this.trainAccuracy = classifier.trainAccuracy;
        this.trainLoss = classifier.trainLoss;
        this.testSamples = classifier.testSamples;
        this.trainSamples = classifier.trainSamples;
        this.classList = classifier.classList;
    }

    public enum Status {
        DUMPING_TEXT,
        EXTRACTING_RELEVANT_TEXT,
        TRAINING,
        TRAINED,
        ERROR
    }
    
}
