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


package org.neuromorpho.literature.article.service.dto;


import org.bson.types.ObjectId;
import org.neuromorpho.literature.article.model.article.Article;
import org.neuromorpho.literature.article.model.article.ReconstructionsStatus;
import org.neuromorpho.literature.article.model.article.Shared;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ArticleDtoAssembler {

    private ReconstructionsDtoAssembler reconstructionsAssembler = new ReconstructionsDtoAssembler();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ArticleDto createArticleDto(Article article) {
        ArticleDto articleDto = new ArticleDto();
        try {
            articleDto.setId(article.getId().toString());
            articleDto.setData(article.getData());
            articleDto.setClassifier(article.getClassifier());
            articleDto.setMetadata(article.getMetadata());
            articleDto.setSearchPortal(article.getSearchPortal());
            articleDto.setReconstructions(reconstructionsAssembler.createReconstructionsDto(article.getReconstructions()));
            articleDto.setStatus(article.getStatus().getStatus());
            articleDto.setLocked(article.getLocked() != null ? article.getLocked() : false);
            articleDto.setClassifier(article.getClassifier());
        }catch (NullPointerException ex){
            log.debug("Error creating articleDto: " + ex);
        }
        return articleDto;

    }

    public ArticleDto createArticleDto(Article article, List<Article> sharedArticleList, Article duplicateArticle) {
        ArticleDto articleDto = this.createArticleDto(article);
        if (sharedArticleList != null) {
            List<ArticleDto> sharedList = sharedArticleList.stream().map(sharedArticle -> this.createArticleDto(sharedArticle)).collect(Collectors.toList());
            articleDto.setSharedList(sharedList);
        }
        if (duplicateArticle != null){
            articleDto.setDuplicate(this.createArticleDto(duplicateArticle));
        }
        articleDto.setStatus(article.getStatus().getStatus());
        return articleDto;
    }

    public Article createArticle(ArticleDto articleDto) {
        Article article = new Article();
        if (articleDto.getId()!= null) {
            article.setId(new ObjectId(articleDto.getId()));
        }
        article.setData(articleDto.getData());
        article.setMetadata(articleDto.getMetadata());
        article.setSearchPortal(articleDto.getSearchPortal());
        return article;
    }

    public List<ReconstructionsStatus> createCurrentStatusList(ReconstructionsDto reconstructionsDto) {
        return reconstructionsAssembler.createCurrentStatusList(reconstructionsDto);
    }

    public List<Shared> createSharedList(List<SharedDto> sharedDtoList) {
        return sharedDtoList.stream().map(sharedDto -> new Shared(sharedDto.getId())).collect(Collectors.toList());
    }
    
}
