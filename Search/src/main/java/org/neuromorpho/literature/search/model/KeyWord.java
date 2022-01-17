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

import java.util.HashMap;

public class KeyWord {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String name;
    private String collection;
    private HashMap<String, Integer> executedList; // 0 to execute, 1 executed, -1 error

    public KeyWord() {
    }

    public KeyWord(String name, String collection) {
        this.name = name;
        this.collection = collection;
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

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public HashMap<String, Integer> getExecutedList() {
        return executedList;
    }

    public void setExecutedList(HashMap<String, Integer> executedList) {
        this.executedList = executedList;
    }

    @BsonIgnore
    public Boolean isEmpty(){
        return this.getName() == null || this.getName().isEmpty() || this.collection == null || this.collection.isEmpty();
    }
    
}
