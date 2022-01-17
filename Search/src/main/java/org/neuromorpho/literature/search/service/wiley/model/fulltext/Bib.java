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

package org.neuromorpho.literature.search.service.wiley.model.fulltext;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Bib {

    @XmlElement(name = "citation")
    private Citation citation;

    public List<String> getAuthorList() {
        try {
            return this.citation.getAuthorList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public LocalDate getPubYear() {
        try {
            return this.citation.getPubYear();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<String> getPageList() {
        try {
            return this.citation.getPageList();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    public String getTitle() {
        try {
            return this.citation.getTitle();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getVolume() {
        try {
            return this.citation.getVolume();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    

    public String getJournal() {
        try {
            return this.citation.getJournal();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    
    
}


