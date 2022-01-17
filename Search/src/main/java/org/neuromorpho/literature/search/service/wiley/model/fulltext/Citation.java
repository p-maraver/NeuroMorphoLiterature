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

    @XmlElement(name = "author")
    private List<Author> authorList;

    @XmlElement(name = "articleTitle")
    private String title;

    @XmlElement(name = "journalTitle")
    private String journal;

    @XmlElement(name = "pubYear")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate pubYear;

    @XmlElement(name = "vol")
    private String volume;
    @XmlElement(name = "issue")
    private String issue;
    @XmlElement(name = "pageFirst")
    private String firstPage;
    @XmlElement(name = "pageLast")
    private String lastPage;

    public String getTitle() {
        return title;
    }

    public String getJournal() {
        return journal;
    }

    public List<String> getPageList() {
        List<String> pageList = new ArrayList<>();
        pageList.add(this.firstPage);
        pageList.add(this.lastPage);
        return pageList;
    }

    public LocalDate getPubYear() {
        return pubYear;
    }

    public String getVolume() {
        return volume;
    }

    public List<String> getAuthorList() {
        try {
            return this.authorList.stream().map(x -> x.getGivenName() + " " + x.getSurname()).collect(Collectors.toList());
        } catch (NullPointerException ex) {
            return new ArrayList<>();
        }
    }

}


