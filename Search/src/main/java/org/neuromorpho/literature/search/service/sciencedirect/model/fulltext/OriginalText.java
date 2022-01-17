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


import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class OriginalText {

    @XmlElement(name = "doc", namespace="http://www.elsevier.com/xml/xocs/dtd")
    private Doc doc;

    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
    }

    public List<Section> getSectionList(){
        return this.doc.getSectionList();
    }

    public List<BibReference> getReferenceList(){
        return this.doc.getReferenceList();
    }

    public List<Figure> getFigureList(){
        return this.doc.getFigureList();
    }
    
    public String getRawText(){
        return this.doc.getRawText();
    }

    public String getAcknowledgment(){
        return this.doc.getAcknowledgment();
    }

    public String getAbstract(){
        return this.doc.getAbstract();
    }

    public AuthorGroup getAuthorGroup(){
        return this.doc.getAuthorGroup();
    }

    public String getTitle(){
        return this.doc.getTitle();
    }
}


