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
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Section {

    @XmlElement(name = "section-title", namespace = "http://www.elsevier.com/xml/common/dtd")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String sectionTitle;

    @XmlElement(name = "section", namespace = "http://www.elsevier.com/xml/common/dtd")
    private List<Section> sectionList;

    @XmlElement(name = "para", namespace = "http://www.elsevier.com/xml/common/dtd")
    @XmlJavaTypeAdapter(value = ParaHandler.class)
    private List<Para> paraList;
    
    
    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public List<Para> getParaList() {
        return paraList;
    }

    public Para getSupplementaryMaterial() {
        try {

            for (Para para : this.paraList) {
                if (para.isSupplementaryMaterial()) {
                    return para;
                }
            }
        } catch (NullPointerException ex) {
        }
        return null;
    }

    public void setParaList(List<Para> paraList) {
        this.paraList = paraList;
    }

    public List<String> getParagraph() {
        try {
            List<String> paragraph = new ArrayList<>();
            for (Para para : this.paraList) {
                if (!para.isSupplementaryMaterial()) {
                    paragraph.add(para.getParagraph());
                }
            }
            return paragraph;
        } catch (NullPointerException ex) {
            return null;
        }
    }
    
    
}


