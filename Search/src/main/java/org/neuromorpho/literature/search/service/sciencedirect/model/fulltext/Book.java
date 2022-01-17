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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Book {

    @XmlTransient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @XmlElement(name = "title", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Title title;
    @XmlElement(name = "editors", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Editors editors;
    @XmlElement(name = "publisher", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Publisher publisher;

    @XmlElement(name = "date", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate date;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Editors getEditors() {
        return editors;
    }

    public void setEditors(Editors editors) {
        this.editors = editors;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public List<String> getEditorList() {
        return this.editors.getEditorListStr();
    }

    public String getPublisherName() {
        return this.publisher.getName();
    }

    public String getPublisherLocation() {
        return this.publisher.getLocation();
    }

    public String getMainTitle() {
        return this.title.getMainTitle();
    }

    @Override
    public String toString() {
        return "Book{" +
                "title=" + title +
                ", editors=" + editors +
                ", publisher=" + publisher +
                ", date=" + date +
                '}';
    }
}


