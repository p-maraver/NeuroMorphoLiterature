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

package org.neuromorpho.literature.search.service.jats.model.fulltext;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Reference {

    @XmlElement(name = "mixed-citation")
    private Citation mixedCitation;

    @XmlElement(name = "element-citation")
    private Citation elementCitation;

    @XmlElement(name = "citation")
    private Citation citation;

    public String getTitle() {
        if (this.citation != null) {
            return this.citation.getTitle();
        } else if (this.elementCitation != null) {
            return this.elementCitation.getTitle();
        } else
            return this.mixedCitation.getTitle();
    }

    public String getJournal() {
        if (this.citation != null) {
            return this.citation.getSource();
        } else if (this.elementCitation != null) {
            return this.elementCitation.getSource();
        } else
            return this.mixedCitation.getSource();
    }


    public List<String> getPageList() {
        if (this.citation != null) {
            return this.citation.getPageList();
        } else if (this.elementCitation != null) {
            return this.elementCitation.getPageList();
        } else
            return this.mixedCitation.getPageList();
    }

    public LocalDate getDate() {
        if (this.citation != null) {
            return this.citation.getDate();
        } else if (this.elementCitation != null) {
            return this.elementCitation.getDate();
        } else
            return this.mixedCitation.getDate();
    }

    public String getVolume() {
        if (this.citation != null) {
            return this.citation.getVolume();
        } else if (this.elementCitation != null) {
            return this.elementCitation.getVolume();
        } else
            return this.mixedCitation.getVolume();
    }


    public List<String> getAuthorList() {
        if (this.citation != null) {
            return this.citation.getAuthorList();
        } else if (this.elementCitation != null) {
            return this.elementCitation.getAuthorList();
        } else
            return this.mixedCitation.getAuthorList();
    }
}


