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

import org.neuromorpho.literature.search.service.ContentHandler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Abstract {

    @XmlAttribute(name="type")
    private String type;

    @XmlElement(name = "title")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String title;

    @XmlElement(name = "p")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private List<String> paraList;

    @XmlElement(name = "section")
    private List<Section> sectionList;
    
    public String getTitle() {
        return title;
    }

    public String getContent() {
        String abstractContent = "";
        if (this.sectionList != null){
            for (Section section: this.sectionList) {
                for (String para : section.getParaList()) {
                    abstractContent = abstractContent + para + "\n";
                }
            }
        }
        else{
            for (String para : paraList) {
                abstractContent = abstractContent + para + "\n";
            }
        }
        
        return abstractContent;
    }

    public String getType() {
        return type;
    }
}


