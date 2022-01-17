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
public class Item {


    @XmlElement(name = "article", namespace = "http://www.elsevier.com/xml/ja/dtd")
    private Article article;

    @XmlElement(name = "simple-article", namespace = "http://www.elsevier.com/xml/ja/dtd")
    private SimpleArticle simpleArticle;

    @XmlElement(name = "converted-article", namespace = "http://www.elsevier.com/xml/ja/dtd")
    private Article convertedArticle;


    public List<Section> getSectionList() {
        if (this.article != null) {
            return this.article.getSectionList();
        } else if (this.convertedArticle != null) {
            return this.convertedArticle.getSectionList();
        } else if (this.simpleArticle != null) {
            return this.simpleArticle.getSectionList();
        }
        return null;
    }

    public List<BibReference> getReferenceList() {
        if (this.article != null) {
            return this.article.getReferenceList();
        } else if (this.convertedArticle != null) {
            return this.convertedArticle.getReferenceList();
        } else if (this.simpleArticle != null) {
            return this.simpleArticle.getReferenceList();
        }
        return null;
    }

    public List<Figure> getFigureList() {
        if (this.article != null) {
            return this.article.getFigureList();
        } else if (this.convertedArticle != null) {
            return this.convertedArticle.getFigureList();
        } else if (this.simpleArticle != null) {
            return this.simpleArticle.getFigureList();
        }
        return null;
    }

    public String getAcknowledgment() {
        if (this.article != null) {
            return this.article.getAcknowledgment();
        } else if (this.convertedArticle != null) {
            return this.convertedArticle.getAcknowledgment();
        } else if (this.simpleArticle != null) {
            return this.simpleArticle.getAcknowledgment();
        }
        return null;
    }

    public String getAbstract() {
        if (this.article != null) {
            return this.article.getAbstract();
        } else if (this.convertedArticle != null) {
            return this.convertedArticle.getAbstract();
        } else if (this.simpleArticle != null) {
            return this.simpleArticle.getAbstract();
        }
        return null;
    }

    public AuthorGroup getAuthorGroup() {
        if (this.article != null) {
            return this.article.getAuthorGroup();
        } else if (this.convertedArticle != null) {
            return this.convertedArticle.getAuthorGroup();
        } else if (this.simpleArticle != null) {
            return this.simpleArticle.getAuthorGroup();
        }
        return null;
    }

    public String getTitle() {
        if (this.article != null) {
            return this.article.getTitle();
        } else if (this.convertedArticle != null) {
            return this.convertedArticle.getTitle();
        } else if (this.simpleArticle != null) {
            return this.simpleArticle.getTitle();
        }
        return null;
    }
}


