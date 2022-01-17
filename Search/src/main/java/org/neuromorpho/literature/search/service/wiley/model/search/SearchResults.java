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

package org.neuromorpho.literature.search.service.wiley.model.search;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "searchRetrieveResponse", namespace="http://docs.oasis-open.org/ns/search-ws/sruResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchResults {

    @XmlElement(name = "numberOfRecords")
    private Integer numberOfRecords;

    @XmlElement(name = "nextRecordPosition")
    private Integer nextRecordPosition;

    @XmlElement(name = "records")
    private Records records;

    public Integer getNumberOfRecords() {
        return numberOfRecords;
    }

    public List<Record> getRecordList() {
        return this.records.getRecordList();
    }

    public Boolean isEmpty(){
        return this.numberOfRecords == 0;
    }

    public Integer getNextRecordPosition() {
        return nextRecordPosition;
    }

    public Records getRecords() {
        return records;
    }
}