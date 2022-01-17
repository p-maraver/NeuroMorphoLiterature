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

package org.neuromorpho.literature.search.service.sciencedirect.model.fulltext;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class BibReference {


    @XmlElement(name = "reference", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Reference referece;

    public Reference getReferece() {
        return referece;
    }

    public void setReferece(Reference referece) {
        this.referece = referece;
    }

    public List<String> getAuthorList() {
        try {
            return this.referece.getAuthorList();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    
    public LocalDate getDate() {
        try {
            return this.referece.getDate();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    public String getJournal() {
        try {
            return this.referece.getJournal();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    
    public List<String> getPageList() {
        try {
            return this.referece.getPageList();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    public String getTitle() {
        try {
            return this.referece.getTitle();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getVolume() {
        try {
            return this.referece.getVolume();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<String> getEditorList() {
        try {
            return this.referece.getEditorList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getPublisherName() {
        try {
            return this.referece.getPublisherName();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    public String getPublisherLocation() {
        try {
            return this.referece.getPublisherLocation();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "BibReference{" +
                "referece=" + referece +
                '}';
    }
}


