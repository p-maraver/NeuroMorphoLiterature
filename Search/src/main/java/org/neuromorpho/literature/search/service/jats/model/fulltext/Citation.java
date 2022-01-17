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


import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class Citation {

    @XmlElement(name = "person-group")
    private PersonGroup personGroup;

    @XmlElement(name = "name")
    private List<Name> nameList;

    @XmlElement(name = "article-title")
    private String title;
    @XmlElement(name = "source")
    private String source;

    @XmlElement(name = "year")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate date;

    @XmlElement(name = "volume")
    private String volume;
    @XmlElement(name = "fpage")
    private String firstPage;
    @XmlElement(name = "lpage")
    private String lastPage;

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public List<String> getPageList() {
        List<String> pageList = new ArrayList<>();
        pageList.add(this.firstPage);
        pageList.add(this.lastPage);
        return pageList;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getVolume() {
        return volume;
    }

    public List<String> getAuthorList() {
        try {
            if (this.personGroup != null) {
                return this.personGroup.getAuthorList();
            } else {
                return this.nameList.stream().map(x -> x.getGivenName() + " " + x.getSurname()).collect(Collectors.toList());
            }
        } catch (NullPointerException ex) {
            return new ArrayList<>();
        }
    }

}


