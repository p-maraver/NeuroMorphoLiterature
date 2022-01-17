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


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ArticleMetadata {

    @XmlElement(name = "title-group")
    private TitleGroup titleGroup;
    @XmlElement(name = "contrib-group")
    private List<ContributorGroup> contribGroupList;
    @XmlElement(name = "aff")
    private List<String> affiliationList;
    @XmlElement(name = "abstract")
    private Abstract content;


    public List<String> getAffiliationList() {
        return affiliationList;
    }


    public String getAbstract() {
        return this.content != null ? this.content.getAbstract() : null;
    }


    public String getTitle() {
        try {
            return this.titleGroup.getTitle();
        } catch (NullPointerException ex){
            return null;
        }
    }

    public List<ContributorGroup> getContribGroupList() {
        return contribGroupList;
    }
}


