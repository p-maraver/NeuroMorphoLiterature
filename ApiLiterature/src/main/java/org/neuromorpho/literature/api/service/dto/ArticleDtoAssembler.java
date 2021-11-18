/*
 * Copyright (c) 2015-2021, Patricia Maraver
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

package org.neuromorpho.literature.api.service.dto;


import org.neuromorpho.literature.api.model.Article;
import org.neuromorpho.literature.api.model.ArticleData;
import org.neuromorpho.literature.api.model.Author;
import org.neuromorpho.literature.api.model.ReconstructionsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ArticleDtoAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ArticleDto createArticleDto(Article article) {
        ArticleDto articleDto = new ArticleDto();
        try {
            if (!article.getStatus().getStatus().equals("NEGATIVE")) {
                articleDto.setId(article.getId().toString());
                List<String> authorList = new ArrayList<>();
                for (Author author : article.getData().getAuthorList()) {
                    authorList.add(author.getName());
                }
                articleDto.setAuthorList(authorList);

                List<String> dataUsageList = new ArrayList<>();

                for (ArticleData.DataUsage usage : article.getData().getDataUsage()) {
                    dataUsageList.add(usage.getUsage());
                }
                articleDto.setDataUsage(dataUsageList);

                articleDto.setDoi(article.getData().getDoi());
                articleDto.setJournal(article.getData().getJournal());
                articleDto.setPmid(article.getData().getPmid());
                articleDto.setPmcid(article.getData().getPmcid());
                articleDto.setPublishedDate(article.getData().getPublishedDate());
                articleDto.setTitle(article.getData().getTitle());
                articleDto.setEvaluatedDate(article.getData().getEvaluatedDate());
                articleDto.setCollection(article.getStatus().getStatus());
                List<String> usageList = new ArrayList<>();
                if (article.getMetadata() != null) {
                    articleDto.setMetadata(article.getMetadata());
                }
                if (article.getReconstructions() != null && article.getCurrentStatus() != null) {
                    Double nReconstructions = 0D;
                    Set<String> specificDetailsSet = new HashSet();

                    for (ReconstructionsStatus status : article.getCurrentStatus()) {
                        nReconstructions = nReconstructions + status.getNReconstructions();
                        specificDetailsSet.add(status.getSpecificDetails().getDetails());
                    }
                    articleDto.setnReconstructions(nReconstructions);
                    articleDto.setSpecificDetails(specificDetailsSet.toString());
                    articleDto.setGlobalStatus(article.getGlobalStatus());
                }
                if ((article.getReconstructions() == null ||
                        article.getReconstructions().getCurrentStatusList() == null ||
                        article.getReconstructions().getCurrentStatusList().isEmpty())
                        && article.getSharedList() != null) {
                    Double nReconstructions = 0D;
                    Set<String> specificDetailsSet = new HashSet();
                    Map<String, Object> metadata = new HashMap();
                    for (Article sharedArticle : article.getSharedInfoList()) {
                        for (ReconstructionsStatus status : sharedArticle.getCurrentStatus()) {
                            specificDetailsSet.add(status.getSpecificDetails().getDetails());
                        }
                        articleDto.setnReconstructions(nReconstructions);
                        articleDto.setSpecificDetails(specificDetailsSet.toString());
                        articleDto.setGlobalStatus(sharedArticle.getGlobalStatus());
                        if (metadata.isEmpty()) { //replace all values except comment
                            metadata.putAll(sharedArticle.getMetadata());
                            if (article.getMetadata() != null) {
                                metadata.put("comment", article.getMetadataValue("comment"));
                            }
                        } else {
                            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                                if (sharedArticle.getMetadata().get(entry.getValue()) instanceof HashSet) {
                                    log.debug("SharedArticle: " + sharedArticle.getId() + " metadata: " + entry.getKey());
                                }
                                if (sharedArticle.getMetadata().get(entry.getKey()) instanceof List) {
                                    List<String> stringList = (List) sharedArticle.getMetadata().get(entry.getKey());
                                    List<String> stringList2 = (List) entry.getValue();
                                    stringList2.addAll(stringList);
                                    Set<String> set = new HashSet<>(stringList2);
                                    metadata.put(entry.getKey(), set);
                                }
                            }
                        }
                    }
                    articleDto.setMetadata(metadata);
                }
            }
        } catch (Exception e) {
            log.error("Exception for article id: ", e);
        }


        return articleDto;
    }

}
