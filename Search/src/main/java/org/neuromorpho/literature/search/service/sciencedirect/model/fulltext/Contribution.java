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
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Contribution {

    @XmlElement(name = "authors", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Authors authors;

    @XmlElement(name = "title", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Title title;

    public Authors getAuthors() {
        return authors;
    }

    public void setAuthors(Authors authors) {
        this.authors = authors;
    }

    public Title getTitle() {
        return title;
    }

    public String getMainTitle() {
        try {
            return title.getMainTitle();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public void setTitle(Title title) {
        this.title = title;
    }


    public List<String> getAuthorList() {
        try {
            return this.authors.getAuthorListStr();
        } catch (NullPointerException ex) {
            return null;
        }
    }


    @Override
    public String toString() {
        return "Contribution{" +
                "authors=" + authors +
                ", title=" + title +
                '}';
    }
}


