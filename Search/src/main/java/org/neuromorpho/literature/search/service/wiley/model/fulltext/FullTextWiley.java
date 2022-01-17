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
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "component")
@XmlAccessorType(XmlAccessType.FIELD)
public class FullTextWiley {

    @XmlElement(name = "header")
    private Header header;

    @XmlElement(name = "body")
    private Body body;

    public List<Section> getSectionList() {
        try {
            return this.body.getSectionList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<Protocol> getProtocolList() {
        try {
            return this.body.getProtocolList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<Bib> getReferenceList() {
        try {
            return this.body.getReferenceList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<Figure> getFigureList() {
        return this.body.getFigureList();
    }
    
    public void fillFigureList() {
        try {
            this.body.fillFigureList();
        } catch (NullPointerException ex) {
        }
    }

    public String getAcknowledgment() {
        try {
            return this.body.getAcknowledgment();
        } catch (NullPointerException ex) {
            return null;
        }
    }
    
//    public List<String> getAffiliationList() {
//        return this.header.getAffiliationList();
//    }

    public String getAbstract() {
        return this.header.getAbstract();
    }

    public List<Creator> getCreatorList() {
        return this.header.getCreatorList();
    }

    public String getTitle() {
        return this.header.getTitle();
    }
}


