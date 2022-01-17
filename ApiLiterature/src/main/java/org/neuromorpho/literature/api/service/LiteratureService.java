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

/**
 *  Services. Spring MVC Pattern
 */
package org.neuromorpho.literature.api.service;

import org.neuromorpho.literature.api.model.Article;
import org.neuromorpho.literature.api.repository.LiteratureRepository;
import org.neuromorpho.literature.api.service.dto.ArticleDtoAssembler;
import org.neuromorpho.literature.api.model.ReconstructionsStatus;
import org.neuromorpho.literature.api.service.dto.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 *  Literature Service. Spring MVC Pattern
 */
@Service
public class LiteratureService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ArticleDtoAssembler assembler = new ArticleDtoAssembler();

    @Resource
    private LiteratureRepository repository;


    public Map<String, Object> getSummary() {
        Map<String, Object> articlesNumbers = new HashMap();
        
        //articles in negative collection
        Long negativesCount = repository.countNegatives();
        articlesNumbers.put("Negatives", negativesCount);

        HashMap<String, Integer> articleSummaryListArticles = repository.countGlobalStatusArticles();
        Double availableReconstructions = 0D;
        Double notAvailableReconstructions = 0D;
        Double determiningAvailabilityReconstructions = 0D;

        HashMap<String, Double> articleSummaryList = repository.countGlobalStatusReconstructions();
        for (Map.Entry<String, Double> summary : articleSummaryList.entrySet()) {
            ReconstructionsStatus.SpecificDetails details = ReconstructionsStatus.SpecificDetails.valueOf(summary.getKey());
            if (details.getStatus().equals("Available")) {
                availableReconstructions = availableReconstructions + summary.getValue();
            } else if (details.getStatus().equals("Not available")) {
                notAvailableReconstructions = notAvailableReconstructions + summary.getValue();
            } else if (details.getStatus().equals("Determining availability")) {
                determiningAvailabilityReconstructions = determiningAvailabilityReconstructions + summary.getValue();
            }
        }
        articlesNumbers.put("Available.nReconstructions", availableReconstructions);
        articlesNumbers.put("Determining availability.nReconstructions", determiningAvailabilityReconstructions);
        articlesNumbers.put("Not available.nReconstructions", notAvailableReconstructions);

        articlesNumbers.putAll(articleSummaryListArticles);

        log.debug("Article numbers: " + articlesNumbers.toString());
        return articlesNumbers;
    }


    public Set<Integer> getPublishedYears() {
        log.debug("Returning published years");
        return repository.getPublishedYears();
    }
    
    
	public Page<ArticleDto> findPublicationsByQuery(Integer page, HashMap<String, String> query){
        log.debug("Returning articles for query: " + query + " and page: " + page);
        Page<Article> articlePage = repository.findPublicationsByQuery(page, query);
        log.debug("Found #articles : " + articlePage.getTotalElements());
       
        List<ArticleDto> articleDtoList = new ArrayList<>();
        for (Article article: articlePage.getContent()){
            ArticleDto articleDto =  assembler.createArticleDto(article);
            articleDtoList.add(articleDto);
        }
        Pageable pageable = new PageRequest(page, articlePage.getSize());

        return new PageImpl(articleDtoList, pageable, articlePage.getTotalElements());	}


}
