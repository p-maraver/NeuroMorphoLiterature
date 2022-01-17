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

package org.neuromorpho.literature.search.dto.fulltext;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.neuromorpho.literature.search.dto.fulltext.sciencedirect.SupplementaryMaterialDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ArticleContentDto {

    private String title;
    private String abstractContent;
    private AuthorGroupDto authorGroup;
    private List<SectionDto> sectionList;
    private List<FigureDto> figureList;
    private SupplementaryMaterialDto supplementaryMaterial; 
    private List<ReferenceDto> referenceList;
    private String acknowledgment;
    private String pdfUrl;
    private String rawText;
    
    public ArticleContentDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public AuthorGroupDto getAuthorGroup() {
        return authorGroup;
    }

    public void setAuthorGroup(AuthorGroupDto authorGroup) {
        this.authorGroup = authorGroup;
    }

    public void setSectionList(List<SectionDto> sectionList) {
        this.sectionList = sectionList;
    }

    public List<SectionDto> getSectionList() {
        return sectionList;
    }

    public List<FigureDto> getFigureList() {
        return figureList;
    }

    public void setFigureList(List<FigureDto> figureList) {
        this.figureList = figureList;
    }

    public List<ReferenceDto> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<ReferenceDto> referenceList) {
        this.referenceList = referenceList;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
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
    
    public SupplementaryMaterialDto getSupplementaryMaterial() {
        return supplementaryMaterial;
    }

    public void setSupplementaryMaterial(SupplementaryMaterialDto supplementaryMaterial) {
        this.supplementaryMaterial = supplementaryMaterial;
    }
}
