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


import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Doc {
    
    @XmlElement(name = "serial-item", namespace="http://www.elsevier.com/xml/xocs/dtd")
    private Item item;
    
    @XmlElement(name = "nonserial-item", namespace="http://www.elsevier.com/xml/xocs/dtd")
    private NSItem nsItem;    
    
    @XmlElement(name = "rawtext", namespace="http://www.elsevier.com/xml/xocs/dtd")
    private String rawText;
    
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public NSItem getNsItem() {
        return nsItem;
    }

    public void setNsItem(NSItem nsItem) {
        this.nsItem = nsItem;
    }

    public List<Section> getSectionList(){
        return this.item != null? this.item.getSectionList():this.nsItem.getSectionList();
    }
    
    public List<BibReference> getReferenceList(){
        return this.item != null? this.item.getReferenceList():this.nsItem.getReferenceList();
    }
    
    public List<Figure> getFigureList(){
        return this.item != null? this.item.getFigureList():this.nsItem.getFigureList();
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getAcknowledgment(){
        try{
            return this.item.getAcknowledgment();
        } catch (NullPointerException ex){
            return null;
        }
    }

    public String getAbstract(){
        return this.item != null? this.item.getAbstract():this.nsItem.getAbstract();
    }

    public AuthorGroup getAuthorGroup(){
        return this.item != null? this.item.getAuthorGroup():this.nsItem.getAuthorGroup();
    }

    public String getTitle(){
        try{
            return this.item != null? this.item.getTitle():this.nsItem.getTitle();
        } catch (NullPointerException ex){
            throw new FullTextNotAvailableException("");
        }
    }

}


