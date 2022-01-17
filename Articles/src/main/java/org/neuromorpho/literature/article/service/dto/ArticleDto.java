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

package org.neuromorpho.literature.article.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.neuromorpho.literature.article.model.article.ArticleData;
import org.neuromorpho.literature.article.model.article.Classifier;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDto {
    
    private String id;
    private ArticleData data;
    private HashMap<String, Set<String>> searchPortal;
    private String status;
    private Map<String, Object> metadata;
    private ReconstructionsDto reconstructions;
    private List<ArticleDto> sharedList;
    private Boolean locked;
    private Classifier classifier;
    private ArticleDto duplicate;


    public ArticleDto() {
    }

    public ArticleDto(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ReconstructionsDto getReconstructions() {
        return reconstructions;
    }

    public void setReconstructions(ReconstructionsDto reconstructions) {
        this.reconstructions = reconstructions;
    }

   
    public HashMap<String, Set<String>> getSearchPortal() {
        return searchPortal;
    }

    public void setSearchPortal(HashMap<String, Set<String>> searchPortal) {
        this.searchPortal = searchPortal;
    }

    public ArticleData getData() {
        return data;
    }

    public void setData(ArticleData data) {
        this.data = data;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ArticleDto> getSharedList() {
        return sharedList;
    }

    public void setSharedList(List<ArticleDto> sharedList) {
        this.sharedList = sharedList;
    }

    @Override
    public String toString() {
        return "ArticleDto{" +
                "id='" + id + '\'' +
                ", data=" + data +
                ", searchPortal=" + searchPortal +
                ", status='" + status + '\'' +
                ", classifier=" + metadata +
                ", reconstructions=" + reconstructions +
                ", sharedList=" + sharedList +
                ", locked=" + locked +
                '}';
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Classifier getClassifier() {
        return classifier;
    }

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public ArticleDto getDuplicate() {
        return duplicate;
    }

    public void setDuplicate(ArticleDto duplicate) {
        this.duplicate = duplicate;
    }

    @JsonIgnore
    public String getTitle(){
        return this.data.getTitle();
    }
    @JsonIgnore
    public String getPmid(){
        return this.data.getPmid();
    }
    @JsonIgnore
    public String getDoi(){
        return this.data.getDoi();
    }
    @JsonIgnore
    public String getPmcid(){
        return this.data.getPmcid();
    }
}
