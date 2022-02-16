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

@XmlAccessorType(XmlAccessType.FIELD)
public class Figure {

    @XmlElement(name = "label")
    private String label;
    @XmlElement(name = "caption")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String caption;
    @XmlElement(name = "graphic")
    private Graphic graphic;

    public Figure() {
    }

    public Figure(String label, String caption, Graphic graphic) {
        this.label = label;
        this.caption = caption;
        this.graphic = graphic;
    }

    public String getLabel() {
        return label;
    }

    public String getCaption() {
        return caption;
    }

    public String getReference() {
        try {
            return this.graphic.getReference();
        } catch (NullPointerException ex){
            return "";
        }
    }
}


