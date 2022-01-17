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
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Record {
    

    @XmlElement(name = "recordData")
    private RecordData recordData;

    public String getDoi() {
        return this.recordData.getDoi();
    }

    public String getJournal() {
        return this.recordData.getJournal();
    }

    public String getTitle() {
        return this.recordData.getTitle();
    }

    public List<String> getContributorList() {
        try {
            return this.recordData.getContributorList();
        } catch (NullPointerException ex){
            return null;
        }
    }

    public String getPublicationDate() {
        return this.recordData.getPublicationDate();
    }

    public String getUrl() {
        return this.recordData.getUrl();
    }
    
}