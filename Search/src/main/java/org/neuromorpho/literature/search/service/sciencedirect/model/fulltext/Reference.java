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
public class Reference {
    
    @XmlElement(name = "contribution", namespace="http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Contribution contribution;
    @XmlElement(name = "host", namespace="http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Host host;

    public Contribution getContribution() {
        return contribution;
    }

    public void setContribution(Contribution contribution) {
        this.contribution = contribution;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public List<String> getAuthorList() {
        return this.contribution.getAuthorList();
    }
    public LocalDate getDate() {
        return this.host.getDate();
    }
    public String getJournal() {
        return this.host.getJournal();
    }
    public List<String> getPageList() {
        return this.host.getPageList();
    }
    public String getTitle() {
        if (this.contribution.getMainTitle() != null){ //is an issue
            return this.contribution.getMainTitle();
        } else { //is a book
            return this.host.getTitle();
        }
    }
    
    
    public String getVolume() {
        return this.host.getVolume();
    }

    public List<String> getEditorList() {
        return this.host.getEditorList();
    }

    public String getPublisherName() {
        return this.host.getPublisherName();
    }
    public String getPublisherLocation() {
        return this.host.getPublisherLocation();
    }


    @Override
    public String toString() {
        return "Reference{" +
                "contribution=" + contribution +
                ", host=" + host +
                '}';
    }
}


