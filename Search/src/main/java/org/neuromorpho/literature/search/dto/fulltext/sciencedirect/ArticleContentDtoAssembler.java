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


package org.neuromorpho.literature.search.dto.fulltext.sciencedirect;


import org.neuromorpho.literature.search.dto.fulltext.*;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.FullText;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ArticleContentDtoAssembler {

    private AuthorGroupDtoAssembler authorGroupAssembler = new AuthorGroupDtoAssembler();
    private SectionDtoAssembler sectionAssembler = new SectionDtoAssembler();
    private ReferenceDtoAssembler referenceAssembler = new ReferenceDtoAssembler();
    private FigureDtoAssembler attachmentAssembler = new FigureDtoAssembler();
    private SupplementaryMaterialDtoAssembler supplementaryMaterialAssembler = new SupplementaryMaterialDtoAssembler();

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ArticleContentDto createArticleContentDto(
            FullText fullText, 
            String pdfUrl, 
            String token) {
        try {
            ArticleContentDto articleContentDto = new ArticleContentDto();
            articleContentDto.setTitle(fullText.getTitle());
            articleContentDto.setAbstractContent(fullText.getAbstract());

            AuthorGroupDto authorGroupDto = authorGroupAssembler.createAuthorGroupDto(fullText.getAuthorGroup());
            articleContentDto.setAuthorGroup(authorGroupDto);

            List<SectionDto> sectionListDto = sectionAssembler.createSectionListDto(fullText.getSectionList());
            articleContentDto.setSectionList(sectionListDto);

            articleContentDto.setRawText(fullText.getRawText());

            List<ReferenceDto> referenceListDto = referenceAssembler.createReferenceListDto(fullText.getReferenceList());
            articleContentDto.setReferenceList(referenceListDto);

            List<FigureDto> figureListDto = attachmentAssembler.createAttachmentListDto(fullText.getObjectList(),
                    fullText.getFigureList(), token);
            articleContentDto.setFigureList(figureListDto);
            articleContentDto.setPdfUrl(pdfUrl);
            articleContentDto.setAcknowledgment(fullText.getAcknowledgment());

            SupplementaryMaterialDto supplementaryMaterialDto = supplementaryMaterialAssembler.createSupplementaryMaterialDto(
                    fullText.getSectionList(), fullText.getObjectList(), token);
            articleContentDto.setSupplementaryMaterial(supplementaryMaterialDto);
            
            return articleContentDto;
        } catch (NullPointerException ex) {
            log.error("Error creating article content: ", ex);
        }
        return null;
    }


}
