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

package org.neuromorpho.literature.fulltext.service;


import org.neuromorpho.literature.fulltext.communication.articles.Article;
import org.neuromorpho.literature.fulltext.communication.articles.SearchCommunication;
import org.neuromorpho.literature.fulltext.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.fulltext.model.ArticleContent;
import org.neuromorpho.literature.fulltext.repository.ArticleDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ArticleContentService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SearchCommunication searchCommunication;

    @Autowired
    private ArticleDataRepository contentRepository;


    public ArticleContent find(String id, Boolean images) {
        ArticleContent content = contentRepository.findById(id);
        if (content != null && images != null && images == Boolean.FALSE) {
            content.removeImages();
        }
        return content;
    }

    public ArticleContent.ContentType exists(String id) {
        return contentRepository.exists(id);
    }
    
    

    public void save(String id, ArticleContent articleContent) {
        try {
            ArticleContent.ContentType contentType = contentRepository.exists(id);
            if (contentType.equals(ArticleContent.ContentType.EMPTY)) {
                contentRepository.save(id, articleContent);
            } else {
                if (articleContent.getContentType().equals(ArticleContent.ContentType.FULLTEXT) ||
                        articleContent.getContentType().equals(ArticleContent.ContentType.RAWTEXT)) {
                    contentRepository.replace(id, articleContent);
                }
            }
        } catch (NullPointerException ex) {
            log.error("Error retrieving full text for article: " + id + "- " + ex.getCause());
            log.error("Exception: ", ex);
        }
    }

    public ArticleContent extractFullTextById(Article article) {
        log.debug("Downloading fulltext for article with id " + article.getId());
        ArticleContent.ContentType contentType = contentRepository.exists(article.getId());
        ArticleContent content = new ArticleContent();
        if (ArticleContent.hasText(contentType)) {
            content = contentRepository.findById(article.getId());
        } else {
            Boolean saved = Boolean.FALSE;
            // for each portal try to download text
            List<String> portalNameList = article.getPortalNameList();
            for (String portalName : portalNameList) {
                try {
                    if (!saved) {
                        String searchId = null;
                        if (this.isDoiPortal(portalName, article.getDoi())) {
                            searchId = article.getDoi();
                        } else if (this.isPubMedCentralPortal(portalName, article.getPmcid())) {
                            searchId = article.getPmcid();
                        }
                        if (searchId != null) {
                            content = searchCommunication.getArticleContent(searchId, portalName);
                            this.save(article.getId(), content);
                            if (content.hasText()) {
                                saved = Boolean.TRUE;
                            }
                        }
                    }

                } catch (NullPointerException ex) {
                    log.error("Error retrieving full text for article: " + article.getId(), ex.getCause());
                } catch (FullTextNotAvailableException ex) {
                    log.info(ex.getMessage());
                }
            }
            //Download PubMed abstract
            if (!saved && ArticleContent.isEmpty(contentType) && article.getPmid() != null) {
                try {
                    //It could be available in PubMed but not in the other Portals
                    content = searchCommunication.getArticleContent(article.getPmid(), "PubMed");
                    this.save(article.getId(), content);
                } catch (FullTextNotAvailableException ex){
                    log.debug("Abstract not available");
                }
            }
        }
        content.removeImages(); // update content remove images, return plain text
        return content;
    }

   
    private Boolean isDoiPortal(String portalName, String doi) {
        return (portalName.equals("ScienceDirect")
                || portalName.equals("Wiley")
                || portalName.equals("SpringerNature")
                || portalName.equals("Springer")
                || portalName.equals("Nature"))
                && doi != null;
    }

    private Boolean isPubMedCentralPortal(String portalName, String pmcid) {
        return portalName.equals("PubMedCentral")
                && pmcid != null;
    }

}
    

