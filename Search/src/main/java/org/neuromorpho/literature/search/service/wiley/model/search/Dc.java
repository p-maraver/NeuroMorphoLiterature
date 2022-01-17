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
public class Dc {

    @XmlElement(name = "identifier")
    private String doi;

    @XmlElement(name = "isPartOf")
    private String journal;

    @XmlElement(name = "title")
    private String title;

    @XmlElement(name = "contributor")
    private List<String> contributorList;

    @XmlElement(name = "date")
    private String publicationDate;

    @XmlElement(name = "url")
    private String url;

    public String getDoi() {
        return doi;
    }

    public String getJournal() {
        return journal;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getContributorList() {
        return contributorList;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getUrl() {
        return url;
    }
   
    
}