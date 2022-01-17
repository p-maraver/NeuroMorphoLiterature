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

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Figure {
    

    @XmlElement(name = "title")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String label;

    @XmlElement(name = "caption")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String caption;

    @XmlElement(name = "mediaResourceGroup")
    private MediaResourceGroup mediaResourceGroup;

    @XmlElement(name = "figurePart")
    private List<FigurePart> figurePartList;

    private List<String> linkList;

    public List<String> getIdList() {
        List<String> idList = new ArrayList<>();
        if (this.mediaResourceGroup != null){
            idList.add(this.mediaResourceGroup.getId());
        } else {
            for (FigurePart part: this.figurePartList) {
                idList.add(part.getId());
            }
        }
        return idList;
    }

    public String getLabel() {
        return label;
    }
    
    public String getCaption() {
        return caption;
    }
    

    public void addLink(String link) {
        if (this.linkList == null){
            this.linkList = new ArrayList<>();
        }
        this.linkList.add(link);
    }
    
    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getLinkList() {
        return linkList;
    }
}


