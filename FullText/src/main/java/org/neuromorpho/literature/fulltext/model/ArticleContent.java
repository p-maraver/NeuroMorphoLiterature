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

package org.neuromorpho.literature.fulltext.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ArticleContent {
    @BsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String title;
    private String abstractContent;
    private AuthorGroup authorGroup;
    private List<Section> sectionList;
    private List<Figure> figureList;
    private SupplementaryMaterial supplementaryMaterial;
    private List<Reference> referenceList;
    private String rawText;
    private String acknowledgment;

    public ArticleContent() {
    }

    public ArticleContent(String rawText) {
        this.rawText = rawText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public List<Figure> getFigureList() {
        return figureList;
    }

    public void setFigureList(List<Figure> figureList) {
        this.figureList = figureList;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public List<Reference> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<Reference> referenceList) {
        this.referenceList = referenceList;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getAcknowledgment() {
        return acknowledgment;
    }

    public void setAcknowledgment(String acknowledgment) {
        this.acknowledgment = acknowledgment;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public AuthorGroup getAuthorGroup() {
        return authorGroup;
    }

    public void setAuthorGroup(AuthorGroup authorGroup) {
        this.authorGroup = authorGroup;
    }
    
    public SupplementaryMaterial getSupplementaryMaterial() {
        return supplementaryMaterial;
    }

    public void setSupplementaryMaterial(SupplementaryMaterial supplementaryMaterial) {
        this.supplementaryMaterial = supplementaryMaterial;
    }

    @BsonIgnore
    public void removeImages() {
        try {
            for (Figure figure : this.figureList) {
                figure.setImage(null);
            }
        } catch (NullPointerException ex) {
            //nothing to do if there are no images
        }
    }

    @BsonIgnore
    public ContentType getContentType() {
        ContentType result = ContentType.EMPTY;
        if (this.getSectionList() != null) {
            result = ContentType.FULLTEXT;
        } else if (this.getRawText() != null) {
            result = ContentType.RAWTEXT;
        } else if (this.getAbstractContent() != null) {
            result = ContentType.ABSTRACT;
        }
        return result;
    }


    @BsonIgnore
    public static Boolean hasText(ContentType type) {
        return type.equals(ContentType.FULLTEXT) ||
                type.equals(ContentType.RAWTEXT);
    }

    @BsonIgnore
    public Boolean hasText() {
        return this.sectionList != null || this.rawText != null;
    }

    @BsonIgnore
    public static Boolean isEmpty(ContentType type) {
        return type.equals(ContentType.EMPTY);
    }

    public enum ContentType {
        ABSTRACT, FULLTEXT, RAWTEXT, EMPTY;
    }

}
