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


package org.neuromorpho.literature.search.dto.fulltext.pubmed;


import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.dto.fulltext.AuthorGroupDto;
import org.neuromorpho.literature.search.service.pubmed.model.fulltext.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ArticleContentDtoAssembler {

    private AuthorGroupDtoAssembler authorGroupAssembler = new AuthorGroupDtoAssembler();

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    

    public ArticleContentDto createArticleContentDto(
            Article article) {
        try {
            ArticleContentDto articleContentDto = new ArticleContentDto();
            articleContentDto.setTitle(article.getTitle());
            articleContentDto.setAbstractContent(article.getAbstract());

            AuthorGroupDto authorGroupDto = authorGroupAssembler.createAuthorGroupDto(article.getAuthorList());
            articleContentDto.setAuthorGroup(authorGroupDto);
            
            return articleContentDto;
        } catch (NullPointerException ex) {
            log.error("Error creating article content: ", ex);
        }
        return null;
    }


}
