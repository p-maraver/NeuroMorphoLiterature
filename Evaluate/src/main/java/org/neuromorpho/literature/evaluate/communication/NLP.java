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


import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class NLP {

    @BsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Set<String> tracingSystem;
    private Set<String> cellType;
    private Set<String> keyword;
    private Set<String> term;

    public Set<String> getTracingSystem() {
        return tracingSystem;
    }

    public void setTracingSystem(Set<String> tracingSystem) {
        this.tracingSystem = tracingSystem;
    }

    public Set<String> getCellType() {
        return cellType;
    }

    public void setCellType(Set<String> cellType) {
        this.cellType = cellType;
    }

    public Set<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(Set<String> keyword) {
        this.keyword = keyword;
    }

    public Set<String> getTerm() {
        return term;
    }

    public void setTerm(Set<String> term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "NLP{" +
                "tracingSystem=" + tracingSystem +
                ", cellType=" + cellType +
                ", keyword=" + keyword +
                ", term=" + term +
                '}';
    }
}
