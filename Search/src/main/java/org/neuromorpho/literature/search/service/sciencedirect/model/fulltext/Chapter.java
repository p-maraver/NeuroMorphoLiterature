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
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class Chapter {

    @XmlElement(name = "floats", namespace="http://www.elsevier.com/xml/common/dtd")
    private Floats floats;

    @XmlElement(name = "title", namespace="http://www.elsevier.com/xml/common/dtd")
    private String title;
    @XmlElement(name = "author-group", namespace="http://www.elsevier.com/xml/common/dtd")
    private AuthorGroup authorGroup;
    @XmlElement(name = "abstract", namespace="http://www.elsevier.com/xml/common/dtd")
    private List<Abstract> abstractList;
    @XmlElement(name = "sections", namespace="http://www.elsevier.com/xml/common/dtd")
    private Sections sections;
    @XmlElement(name = "bibliography", namespace="http://www.elsevier.com/xml/common/dtd")
    private Bibliography bibliography;

    public Floats getFloats() {
        return floats;
    }

    public void setFloats(Floats floats) {
        this.floats = floats;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AuthorGroup getAuthorGroup() {
        return authorGroup;
    }

    public void setAuthorGroup(AuthorGroup authorGroup) {
        this.authorGroup = authorGroup;
    }

    public List<Abstract> getAbstractList() {
        return abstractList;
    }

    public void setAbstractList(List<Abstract> abstractList) {
        this.abstractList = abstractList;
    }

    public Sections getSections() {
        return sections;
    }

    public void setSections(Sections sections) {
        this.sections = sections;
    }

    public Bibliography getBibliography() {
        return bibliography;
    }

    public void setBibliography(Bibliography bibliography) {
        this.bibliography = bibliography;
    }
    public List<Section> getSectionList(){
        return this.sections.getSectionList();
    }

    public List<BibReference> getReferenceList(){
        try{
            return this.bibliography.getReferenceList();
        } catch (NullPointerException ex){
            return null;
        }
    }
    public List<Figure> getFigureList(){
        return this.floats.getFigureList();
    }

    public String getAbstract() {
        try {
            List<Abstract> abstractList = this.abstractList.stream()
                    .collect(Collectors.toList());
            return abstractList.size() > 0 ? abstractList.get(0).getContent() : null;
        } catch (NullPointerException ex) {
            return null;
        }
    }
}


