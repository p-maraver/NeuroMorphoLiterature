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

import javax.xml.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Host {

    @XmlTransient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @XmlElement(name = "issue", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Issue issue;
    @XmlElement(name = "book", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Book book;
    @XmlElement(name = "edited-book", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Book editedBook;
    @XmlElement(name = "pages", namespace = "http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Pages pages;

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Pages getPages() {
        return pages;
    }

    public void setPages(Pages pages) {
        this.pages = pages;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Book getEditedBook() {
        return editedBook;
    }

    public void setEditedBook(Book editedBook) {
        this.editedBook = editedBook;
    }

    public LocalDate getDate() {
        try {
            if (this.issue != null) {
                return this.issue.getDate();
            } else {
                return this.book.getDate();
            }
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<String> getPageList() {
        if (this.pages != null) {
            List<String> pageList = new ArrayList<>();
            pageList.add(this.pages.getFirstPage());
            pageList.add(this.pages.getLastPage());
            return pageList;
        }
        return null;
    }

    public String getVolume() {
        try {
            return this.issue.getVolume();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getJournal() {
        try {
            return this.issue.getJournal();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public List<String> getEditorList() {
        try {
            return this.book != null? this.book.getEditorList() : this.editedBook.getEditorList();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getPublisherName() {
        try {
            return this.book != null? this.book.getPublisherName() : this.editedBook.getPublisherName();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getPublisherLocation() {
        try {
            return this.book != null?this.book.getPublisherLocation() : this.editedBook.getPublisherLocation();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public String getTitle() {
        try {
            return this.book != null?this.book.getMainTitle() : this.editedBook.getMainTitle();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Host{" +
                "issue=" + issue +
                ", book=" + book +
                ", pages=" + pages +
                '}';
    }
}


