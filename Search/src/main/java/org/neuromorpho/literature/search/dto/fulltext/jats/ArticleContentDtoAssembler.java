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

package org.neuromorpho.literature.search.dto.fulltext.jats;

import org.neuromorpho.literature.search.dto.fulltext.*;
import org.neuromorpho.literature.search.model.Portal;
import org.neuromorpho.literature.search.service.jats.model.fulltext.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class ArticleContentDtoAssembler {

    private AuthorGroupDtoAssembler authorGroupAssembler = new AuthorGroupDtoAssembler();
    private SectionDtoAssembler sectionAssembler = new SectionDtoAssembler();
    private ReferenceDtoAssembler referenceAssembler = new ReferenceDtoAssembler();
    private FigureDtoAssembler figureAssembler = new FigureDtoAssembler();

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    

    public ArticleContentDto createArticleContentDto(
            Article article,
            Portal portal,
            String value) {
        try {
            ArticleContentDto articleContentDto = new ArticleContentDto();
            articleContentDto.setTitle(article.getTitle());
            articleContentDto.setAbstractContent(article.getAbstract());
            
            AuthorGroupDto authorGroupDto = authorGroupAssembler.createAuthorGroupDto(article.getContributorGroup(), 
                    article.getAffiliationList());
            articleContentDto.setAuthorGroup(authorGroupDto);

            List<SectionDto> sectionListDto = sectionAssembler.createSectionListDto(article.getSectionList());
            articleContentDto.setSectionList(sectionListDto);

            List<ReferenceDto> referenceListDto = referenceAssembler.createReferenceListDto(article.getReferenceList());
            articleContentDto.setReferenceList(referenceListDto);


            List<FigureDto> figureListDto = figureAssembler.createFigureListDto(article.getFigureList(), portal, value);
            articleContentDto.setFigureList(figureListDto);
            articleContentDto.setAcknowledgment(article.getAcknowledgment());

//            SupplementaryMaterialDto supplementaryMaterialDto = supplementaryMaterialAssembler.createSupplementaryMaterialDto(
//                    fullText.getSectionList(), fullText.getObjectList(), token);
//            articleContentDto.setSupplementaryMaterial(supplementaryMaterialDto);

            return articleContentDto;
        } catch (NullPointerException ex) {
            log.error("Error creating article content: ", ex);
        }
        return null;
    }


}
