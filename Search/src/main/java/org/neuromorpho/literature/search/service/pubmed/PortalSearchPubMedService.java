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

package org.neuromorpho.literature.search.service.pubmed;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neuromorpho.literature.search.dto.fulltext.pubmed.ArticleContentDtoAssembler;
import org.neuromorpho.literature.search.pubmed.service.PubMedService;
import org.neuromorpho.literature.search.service.pubmedcentral.model.fulltext.FullText;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.search.model.Portal;
import org.neuromorpho.literature.search.service.PortalSearch;
import org.neuromorpho.literature.search.service.jats.model.fulltext.localtor.OA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PortalSearchPubMedService extends PortalSearch {

    private final static Logger log = LoggerFactory.getLogger(PortalSearchPubMedService.class);
    
    RestTemplate restTemplate = new RestTemplate();
    
    @Autowired
    PubMedService pubMedService;

    @Override
    public void searchForTitlesApi() {

        try {

            String uri = this.portal.getSearchUrlApi() + "/esearch.fcgi?"
                    + "db=" + this.portal.getDb()
                    + "&apiKey=" + this.portal.getToken()
                    + "&retmode=json"
                    + "&retmax=5000"
                    + "&term=" + this.keyWord
                    + " AND " + this.portal.getStartSearchDate().getYear() + ":"
                    + this.portal.getEndSearchDate().getYear() + "[pdat]";
            log.debug("PubMed retrieving from URI: " + uri);

            Map<String, Object> pmidMap = restTemplate.getForObject(uri, Map.class);

            Map result = (HashMap) pmidMap.get("esearchresult");
            ArrayList<String> uidList = (ArrayList) result.get("idlist");
            log.debug("Articles Found : " + uidList.size());

            for (String uid : uidList) {
                try {
                    log.debug("Seconds to sleep: " + 2);
                    log.debug("......................................");
                    Thread.sleep(2 * 1000);
                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }
                    article = pubMedService.retrievePubMedArticleData(uid, Article.DB.getDB(this.portal.getDb()));
                    this.saveArticle();
                    log.debug("DB: " + this.portal.getDb() + " PMID: " + uid);


                } catch (HttpServerErrorException ex) {
                    log.error("Exception: ", ex);
                }

            }
        } catch (Exception ex) {
            log.error("Exception: ", ex);
        }
    }

    @Override
    public ArticleContentDto getArticleContent(String id, Portal portal) {
        try {

            Jaxb2RootElementHttpMessageConverter converter = new Jaxb2RootElementHttpMessageConverter();
            converter.setSupportDtd(true);
            List<HttpMessageConverter<?>> converters = new ArrayList<>();
            converters.add(converter);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.setMessageConverters(converters);
            
            this.portal = portal;
            String uri = portal.getContentUrlApi() + id + "&api_key="  + this.portal.getToken();
            try {
                log.debug("API retrieving from URI full text: " + uri);
                if (portal.getName().equals("PubMed")){
                    ArticleContentDtoAssembler assembler = 
                            new ArticleContentDtoAssembler();
                    org.neuromorpho.literature.search.service.pubmed.model.fulltext.FullText result = 
                            restTemplate.getForObject(uri, org.neuromorpho.literature.search.service.pubmed.model.fulltext.FullText.class);
                    if (result.getArticle()==null || result.getArticle().getAbstract() == null){
                        throw new FullTextNotAvailableException("id: " + id + " portal: " + portal.getName());
                    }
                    return assembler.createArticleContentDto(result.getArticle());
                } else {
                    org.neuromorpho.literature.search.dto.fulltext.jats.ArticleContentDtoAssembler assembler = 
                            new org.neuromorpho.literature.search.dto.fulltext.jats.ArticleContentDtoAssembler();
                    FullText result = 
                            restTemplate.getForObject(uri, FullText.class);

                    if (result.getArticle() == null 
                            || result.getArticle().getAbstract() == null 
                            || result.getArticle().getTitle() == null
                            || result.getArticle().getContributorGroup() == null){
                        throw new FullTextNotAvailableException("id: " + id + " portal: " + portal.getName());
                    }
                    return assembler.createArticleContentDto(result.getArticle(), portal, this.getURLAttachment(id));
                }

            } catch (HttpClientErrorException ex) {
                log.error("Error retrieving full text: " + ex.getMessage());
                return null;
            }
        } catch (FullTextNotAvailableException ex) {
            log.error(ex.getMessage());
            throw ex;
        }
    }

    private String getURLAttachment(String pmcid) {
        try {
            String url = "https://www.ncbi.nlm.nih.gov/pmc/utils/oa/oa.fcgi?id=PMC" + pmcid + "&api_key=" + this.portal.getToken();
            log.debug("Creating rest connection for URI: " + url);
            OA response = restTemplate.getForObject(url, OA.class);
            return response.getLink();
        } catch (Exception ex) {
            log.error("PubMed connection exception", ex);
        }
        return null;
    }


    @Override
    protected Elements findArticleList() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void searchPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Boolean loadNextPage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String fillTitle(Element articleData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fillPublishedDate(Element articleData, Element articlePage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fillJournal(Element articleData, Element articlePage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fillAuthorList(Element articleData, Element articlePage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fillDoi(Element articleData, Element articlePage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void fillLinks(Element articleData, Element articlePage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    protected void searchForTitles() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
