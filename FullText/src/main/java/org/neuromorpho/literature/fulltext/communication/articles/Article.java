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

package org.neuromorpho.literature.fulltext.communication.articles;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class Article {
    @BsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String id;
    private ArticleData data;
    private Map<String, Object> metadata;
    private HashMap<String, Set<String>> searchPortal;

    public Article() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArticleData getData() {
        return data;
    }

    public void setData(ArticleData data) {
        this.data = data;
    }

    public HashMap<String, Set<String>> getSearchPortal() {
        return searchPortal;
    }

    public void setSearchPortal(HashMap<String, Set<String>> searchPortal) {
        this.searchPortal = searchPortal;
    }

    public String getDoi() {
        return this.data.getDoi();
    }

    public String getPmcid() {
        return this.data.getPmcid();
    }

    public String getPmid() {
        return this.data.getPmid();
    }


    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public LocalDate getPublishedDate() {
        return this.data.getPublishedDate();
    }

    public List<String> getPortalList() {
        try {
            List<String> portalNameList = this.searchPortal.keySet().stream().map(p -> {
                if (p.equals("SpringerLink")
                        || p.equals("SpringerNature")
                        || p.equals("Nature")) {
                    return "SpringerNature";
                } else {
                    return p;
                }
            }).collect(Collectors.toList());
            return portalNameList;

        } catch (NullPointerException ex) {
            log.info("Article without portal for id: " + this.id);
        }
        return new ArrayList<>();
    }

    public List<String> getPortalNameList() {
        List<String> portalNameList = new ArrayList<>();
        portalNameList.add("ScienceDirect");
        portalNameList.add("SpringerNature");
        portalNameList.add("Wiley");
        portalNameList.add("PubMedCentral");
        return portalNameList;
    }


    public Boolean containsKeyWord(String keyWord) {
        Set<String> keyWordSet = new HashSet<>();
        this.searchPortal.forEach((k, v) -> keyWordSet.addAll(v));
        //treat keyWords
        return keyWordSet.contains(keyWord);
    }

    public String getPdfLink() {
        return this.data.getPdfLink();
    }

    public String getMetadataStatus() {
        return (String) this.metadata.get("articleStatus");
    }
}
