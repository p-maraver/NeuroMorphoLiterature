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
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Article {

    @XmlElement(name = "floats", namespace="http://www.elsevier.com/xml/common/dtd")
    private Floats floats;
    
    @XmlElement(name = "head", namespace="http://www.elsevier.com/xml/ja/dtd")
    private Head head;
    
    @XmlElement(name = "body", namespace="http://www.elsevier.com/xml/ja/dtd")
    private Body body;

    @XmlElement(name = "tail", namespace="http://www.elsevier.com/xml/ja/dtd")
    private Tail tail;
    
    public List<Section> getSectionList(){
        try {
            return this.body.getSectionList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<BibReference> getReferenceList(){
        try {
            return this.tail.getReferenceList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<Figure> getFigureList(){
        try {
            return this.floats.getFigureList();
        } catch (NullPointerException ex) {
            return new ArrayList<>();
        }
    }


    public String getAcknowledgment(){
        try {
            return this.body.getAcknowledgmentContent();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getAbstract(){
        return this.head.getAbstract();
    }

    public AuthorGroup getAuthorGroup(){
        return this.head.getAuthorGroup();
    }

    public String getTitle(){
        return this.head.getTitle();
    }
}


