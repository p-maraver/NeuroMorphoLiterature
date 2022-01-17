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

@XmlRootElement(name = "full-text-retrieval-response")
@XmlAccessorType(XmlAccessType.FIELD)
public class FullText {

    @XmlElement(name = "originalText")
    private OriginalText originalText;

    @XmlElement(name = "objects")
    private Objects objects;

    public OriginalText getOriginalText() {
        return originalText;
    }

    public void setOriginalText(OriginalText originalText) {
        this.originalText = originalText;
    }

    public List<Section> getSectionList(){
        return this.originalText.getSectionList();
    }

    public List<BibReference> getReferenceList(){
        return this.originalText.getReferenceList();
    }

    public List<Figure> getFigureList(){
        return this.originalText.getFigureList();
    }

    public String getRawText(){
        return this.originalText.getRawText();
    }

    public String getAcknowledgment(){
        return this.originalText.getAcknowledgment();
    }

    public String getAbstract(){
        return this.originalText.getAbstract();
    }

    public String getTitle(){
        return this.originalText.getTitle();
    }

    public AuthorGroup getAuthorGroup(){
        return this.originalText.getAuthorGroup();
    }

    public Objects getObjects() {
        return objects;
    }

    public void setObjects(Objects objects) {
        this.objects = objects;
    }

    public List<Object> getObjectList() {
        return this.objects != null ? this.objects.getObjectList(): null;
    }
}


