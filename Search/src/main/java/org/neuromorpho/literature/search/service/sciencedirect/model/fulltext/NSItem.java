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
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class NSItem {


    @XmlElement(name = "chapter")
    private Chapter chapter;

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public List<Section> getSectionList(){
        return this.chapter.getSectionList();
    }

    public List<BibReference> getReferenceList(){
        return this.chapter.getReferenceList();
    }
    public List<Figure> getFigureList(){
        return this.chapter.getFigureList();
    }

    public String getAbstract(){
        return this.chapter.getAbstract();
    }

    public AuthorGroup getAuthorGroup(){
        return this.chapter.getAuthorGroup();
    }

    public String getTitle(){
        return this.chapter.getTitle();
    }
}


