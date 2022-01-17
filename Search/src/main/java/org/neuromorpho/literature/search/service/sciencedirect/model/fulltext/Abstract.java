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


import org.neuromorpho.literature.search.service.ContentHandler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Abstract {


    @XmlElement(name = "section-title", namespace = "http://www.elsevier.com/xml/common/dtd")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String title;

    @XmlElement(name = "abstract-sec", namespace = "http://www.elsevier.com/xml/common/dtd")
    private List<AbstractSec> sectionList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        String abstractContent = "";
        for (AbstractSec abstractSec : this.sectionList) {
            if (abstractSec.getSectionTitle() != null) {
                abstractContent = abstractContent + abstractSec.getSectionTitle() + ": ";
            }
            abstractContent = abstractContent + abstractSec.getParagraph() + "\n";
        }
        return abstractContent;
    }

    public Boolean isAbstract() {
        return this.title != null && this.title.equals("Abstract");
    }
}


