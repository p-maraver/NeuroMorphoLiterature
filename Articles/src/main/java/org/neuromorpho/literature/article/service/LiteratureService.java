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

package org.neuromorpho.literature.article.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.article.connection.Identifiers;
import org.neuromorpho.literature.article.connection.SearchConnection;
import org.neuromorpho.literature.article.controller.Search;
import org.neuromorpho.literature.article.model.article.*;
import org.neuromorpho.literature.article.repository.ArticleRepository;
import org.neuromorpho.literature.article.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LiteratureService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Article.ArticleStatus defaultStatus = Article.ArticleStatus.TO_EVALUATE;

    private ArticleDtoAssembler assembler = new ArticleDtoAssembler();

    @Autowired
    private SearchConnection searchConnection;


    @Autowired
    private ArticleRepository repository;

    public Map<String, Long> getSummary(String date) {
        return repository.getSummary(date);
    }

    public List<Object> getSummaryByDataUsage() {
        return repository.getSummaryByDataUsage();
    }

    public Page<ArticleDto> findArticleList(String articleStatus,
                                            Map<String, String> queryParams) throws ParseException {
        Article.ArticleStatus status = Article.ArticleStatus.getArticleStatus(articleStatus);

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (entry.getKey().contains("dataUsage")) {
                entry.setValue(ArticleData.DataUsage.getUsage(entry.getValue()).toString());
            }
        }
        Page<Article> articlePage = repository.findArticlesByQuery(status, queryParams);
        log.debug("Found #articles : " + articlePage.getTotalElements());

        List<ArticleDto> articleDtoList = articlePage.getContent().stream()
                .map(article -> assembler.createArticleDto(article)).collect(Collectors.toList());

        return new PageImpl(articleDtoList, articlePage.getPageable(), articlePage.getTotalElements());
    }


    public Article saveArticleManual(ArticleDto articleDto) {
        Article article = assembler.createArticle(articleDto);
        article.initialize();
        ObjectId objectId = new ObjectId();
        article.setId(objectId);
        repository.save(article, Article.ArticleStatus.getArticleStatus(articleDto.getStatus()));
        return article;
    }

    public String saveArticleAutomatedSearch(ArticleData article, Article.ArticleStatus status) {
        ObjectId id = repository.saveOrUpdate(new Article(article, status), status);
        return id.toString();

    }

    public void deleteArticle(String id) {
        repository.delete(new ObjectId(id));
    }

    public ArticleDto findArticle(String id) {

        log.debug("Reading article id: " + id);

        Article article = repository.findById(new ObjectId(id));
        List<Article> sharedArticleList = null;
        if (article.getSharedList() != null) {
            sharedArticleList = new ArrayList();
            for (Shared shared : article.getSharedList()) {
                Article sharedArticle = repository.findById(shared.getSharedId());
                sharedArticleList.add(sharedArticle);
            }
        }
        Article duplicateArticle = null;
        if (article.getDuplicate() != null) {
            duplicateArticle = repository.findById(article.getDuplicateId());
        }
        ArticleDto articleDto = assembler.createArticleDto(article, sharedArticleList, duplicateArticle);
        return articleDto;
    }

    public void updateStatus(String id, Article.ArticleStatus status, ReconstructionsStatus.SpecificDetails specificDetails) {
        Article article = repository.findById(new ObjectId(id));
        if (article != null && !article.getStatus().equals(status)) {
            log.debug("Updating collection for article id: " + id + " from: " + article.getStatus() + " to: " + status);
            article.updateCollection(status, specificDetails);
            Map<String, String> query = new HashMap<>();
            query.put("sharedList.sharedId", article.getId().toString());
            Page<Article> sharedList = repository.findArticlesByQuery(article.getStatus(), query);
            for (Article articleShared: sharedList){
                repository.updateCollection(articleShared, status);
            }
            repository.updateCollection(article, status);
        }

    }
    
    public void update(String id, String field, Object object) {
        Article article = repository.findById(new ObjectId(id));
        String collectionName = article.getStatus().getCollection();
        if (field.equals("reconstructions")) {
            ObjectMapper mapper = new ObjectMapper();
            ReconstructionsDto pojo = mapper.convertValue(object, ReconstructionsDto.class);
            List<ReconstructionsStatus> currentStatusList = assembler.createCurrentStatusList(pojo);
            article.updateCurrentStatusList(currentStatusList);
            object = article.getReconstructions();
        } else if (field.equals("sharedList")) {
            ObjectMapper mapper = new ObjectMapper();
            List<SharedDto> pojo = mapper.convertValue(object, new TypeReference<ArrayList<SharedDto>>() {
            });
            List<Shared> sharedList = assembler.createSharedList(pojo);
            object = sharedList;

        } else if (field.equals("data")) {
            ObjectMapper mapper = new ObjectMapper();
            ArticleData data = mapper.convertValue(object, ArticleData.class);
            //data.setAuthorListNoEmpty(data.getAuthorList());
            object = data;
        } else if (field.equals("searchPortal")) {
            ObjectMapper mapper = new ObjectMapper();
            Search pojo = mapper.convertValue(object, Search.class);
            article.updateSearchPortal(pojo.getSource(), pojo.getKeyWord());
            object = article.getSearchPortal();
        } else if (field.equals("data.dataUsage")) {
            ObjectMapper mapper = new ObjectMapper();
            List data = mapper.convertValue(object, List.class);
            if (data.stream().noneMatch(str -> str.equals(ArticleData.DataUsage.DESCRIBING_NEURONS.toString())) && article.hasCurrentReconstructions()) {
                article.updateCurrentStatusList(null);
                object = article.getReconstructions();
                repository.updateArticle(collectionName, new ObjectId(id), "reconstructions", object);
            }
            if (data.stream().noneMatch(str -> str.equals(ArticleData.DataUsage.SHARING.toString())) && article.hasSharedReconstructions()) {
                repository.updateArticle(collectionName, new ObjectId(id), "sharedList", null);
            }
            object = data;
        } else if (field.equals("locked")) {
            ObjectMapper mapper = new ObjectMapper();
            HashMap result = mapper.convertValue(object, HashMap.class);
            object = result.get("locked");
        } else if (field.equals("duplicate")) {
            ObjectMapper mapper = new ObjectMapper();
            Duplicate result = mapper.convertValue(object, Duplicate.class);
            object = result;
        } else if (field.equals("data.authorList")) {
            ObjectMapper mapper = new ObjectMapper();
            List<Author> pojo = mapper.convertValue(object, new TypeReference<ArrayList<Author>>() {
            });
            object = pojo;
        }

        repository.updateArticle(collectionName, new ObjectId(id), field, object);
    }

    public void updateContacts(Contact contact) {
        repository.updateContacts(contact);
    }

    public void update2NextReconstructionsStatus(String id, String reconstructionsStatus) {
        Article article = repository.findById(new ObjectId(id));
        article.update2NextReconstructionsStatus(reconstructionsStatus, article.isNegativeIfNoAnswer());
        String collectionName = article.getStatus().getCollection();
        repository.updateArticle(collectionName, new ObjectId(id), "reconstructions", article.getReconstructions());
    }
    

}
