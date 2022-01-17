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


import org.neuromorpho.literature.search.service.ContentHandler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Section {

    @XmlElement(name = "title")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String title;


    @XmlElement(name = "p")
    @XmlJavaTypeAdapter(value = ParaHandler.class)
    private List<Para> paraList;

    @XmlElement(name = "sec")
    private List<Section> sectionList;

    @XmlElement(name = "fig")
    private List<Figure> figureList = new ArrayList<>();

    public String getTitle() {
        return title;
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


    public List<Section> getSectionList() {
        return sectionList;
    }

    public void fillFigureList() {
        try {
            for (Para para : this.paraList) {
                if (para.getFigureList() != null) {
                    figureList.addAll(para.getFigureList());
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    public List<Figure> getFigureList() {
        return figureList;
    }
}


