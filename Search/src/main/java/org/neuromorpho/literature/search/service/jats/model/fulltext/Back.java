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
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Back {


    @XmlElement(name = "fn-group")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String funding;

    @XmlElement(name = "ack")
    private Acknowledgment acknowledgment;

    @XmlElement(name = "sec")
    private Section sec;

    @XmlElement(name = "ref-list")
    private References references;


    @XmlElement(name = "app-group")
    private AppGroup appList;

    public List<Reference> getReferenceList() {
        return this.references.getReferenceList();
    }

    public String getAcknowledgment() {
        if (this.acknowledgment != null) {
            return this.acknowledgment.getAcknowledgment();
        } else if (this.sec != null) {
            return this.sec.getParagraph().get(0);
        } else {
            return this.funding;
        }
    }

}


