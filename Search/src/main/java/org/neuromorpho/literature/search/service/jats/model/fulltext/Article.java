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
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Article {

    @XmlElement(name = "front")
    private Front front;
    @XmlElement(name = "body")
    private Body body;
    @XmlElement(name = "back")
    private Back back;
    @XmlElement(name = "floats-group")
    private FloatsGroup floatsGroup;


    public List<Section> getSectionList() {
        try {
            return this.body.getSectionList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<Reference> getReferenceList() {
        try {
            return this.back.getReferenceList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<Figure> getFigureList() {
        try {
            return this.floatsGroup != null && this.floatsGroup.getFigureList() != null ? this.floatsGroup.getFigureList() : this.body.getFigureList();
        } catch (NullPointerException ex) {
            return new ArrayList();
        }
    }

    public String getAcknowledgment() {
        try {
            return this.back.getAcknowledgment();
        } catch (NullPointerException ex) {
            return null;
        }
    }


    public List<String> getAffiliationList() {
        return this.front.getAffiliationList();
    }

    public String getAbstract() {
        return this.front.getAbstract();
    }

    public List<ContributorGroup> getContributorGroup() {
        return this.front.getContributorGroup();
    }

    public String getTitle() {
        return this.front.getTitle();
    }
}


