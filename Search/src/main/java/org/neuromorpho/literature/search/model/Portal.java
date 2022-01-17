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

package org.neuromorpho.literature.search.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Portal {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    private String name;
    private String url;
    private String base;
    private LocalDate startSearchDate;
    private LocalDate endSearchDate;
    private Boolean active;
    private String db;
    private String searchUrlApi;
    private String contentUrlApi;
    private String token;
    private Log log;

    public Portal(String name) {
        this.name = name;
    }

    public Portal() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getStartSearchDate() {
        if (this.startSearchDate == null) {
            this.startSearchDate = LocalDate.now();
        }
        return startSearchDate;
    }

    public void setStartSearchDate(LocalDate startSearchDate) {
        this.startSearchDate = startSearchDate;
    }

    public LocalDate getEndSearchDate() {
        if (this.endSearchDate == null) {
            this.endSearchDate = LocalDate.now();
        }
        return endSearchDate;
    }

    public void setEndSearchDate(LocalDate endSearchDate) {
        this.endSearchDate = endSearchDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }
    
    public Boolean hasSearchAPI() {
        return searchUrlApi != null;
    }
    
    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSearchUrlApi() {
        return searchUrlApi;
    }

    public void setSearchUrlApi(String searchUrlApi) {
        this.searchUrlApi = searchUrlApi;
    }

    public String getContentUrlApi() {
        return contentUrlApi;
    }

    public void setContentUrlApi(String contentUrlApi) {
        this.contentUrlApi = contentUrlApi;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }
    
    @BsonIgnore
    public List<String> getPortalNameDoiList() {
        List<String> portalNameList = new ArrayList<>();
        portalNameList.add("ScienceDirect");
        portalNameList.add("SpringerNature");
        portalNameList.add("Wiley");
        return portalNameList;
    }
    
}
