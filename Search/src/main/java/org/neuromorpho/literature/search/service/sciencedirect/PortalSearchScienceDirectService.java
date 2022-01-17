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

package org.neuromorpho.literature.search.service.sciencedirect;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neuromorpho.literature.search.communication.article.ArticleAssembler;
import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.dto.fulltext.sciencedirect.ArticleContentDtoAssembler;
import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.search.model.Portal;
import org.neuromorpho.literature.search.service.PortalSearch;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.FullText;
import org.neuromorpho.literature.search.service.sciencedirect.model.search.Entry;
import org.neuromorpho.literature.search.service.sciencedirect.model.search.Link;
import org.neuromorpho.literature.search.service.sciencedirect.model.search.SearchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PortalSearchScienceDirectService extends PortalSearch {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    RestTemplate restTemplate = new RestTemplate();
    ArticleAssembler assembler = new ArticleAssembler();

    @Override
    public void searchForTitlesApi() throws InterruptedException {
        Integer ocurrences = StringUtils.countMatches(this.keyWord, "\"");
        String replacedKeyword = this.keyWord;
        for (Integer i = 0; i < ocurrences / 2; i++) {
            replacedKeyword = replacedKeyword.replaceFirst("\"", "%7B");
            replacedKeyword = replacedKeyword.replaceFirst("\"", "%7D");
        }

        String year = Integer.toString(this.portal.getEndSearchDate().getYear());
        if (this.portal.getStartSearchDate().getYear() != this.portal.getEndSearchDate().getYear()) {
            year = this.portal.getStartSearchDate().getYear() + "-" + this.portal.getEndSearchDate().getYear();
        }
        String uri = this.portal.getSearchUrlApi()
                + "query=" + replacedKeyword
                + "&date=" + year
                //+ "&start=" + startRecord
                + "&apiKey=" + this.portal.getToken();
        Boolean next = Boolean.FALSE;
        do {//iterate over pages

            log.debug("API retrieving from URI: " + uri);
            SearchResults searchResults = restTemplate.getForObject(uri, SearchResults.class);
            next = Boolean.FALSE;

            if (!searchResults.isEmpty()) {
                log.debug("Articles Found : " + searchResults.getResultsSize());

                for (Entry entry : searchResults.getEntryList()) {
                    this.article = assembler.createArticle(entry);
                    this.saveArticle();
                }
                for (Link link : searchResults.getLinkList()) {
                    if (link.existsNextPage()) {
                        uri = link.getLink();
                        next = Boolean.TRUE;
                    }
                }
            }

        } while (next);


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

    @Override
    public ArticleContentDto getArticleContent(String doi, Portal portal) {
        this.portal = portal;
        ArticleContentDtoAssembler assembler = new ArticleContentDtoAssembler();
        FullText fulltext = this.getFullText(doi);
        if (fulltext != null) {
            return assembler.createArticleContentDto(fulltext, this.getPDF(doi), "&apiKey=" + portal.getToken());
        }

        return null;
    }

    protected FullText getFullText(String doi) {
        try {

            String uri = portal.getContentUrlApi()
                    + doi
                    + "?apiKey=" + this.portal.getToken();

            log.debug("API retrieving from URI full text: " + uri);
            return restTemplate.getForObject(uri, FullText.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error retrieving full text: " + ex.getMessage());
            if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new FullTextNotAvailableException("id: " + doi + " portal: ScienceDirect");
            }
            return null;
        }
    }


    protected String getPDF(String doi) {

        return portal.getContentUrlApi()
                + doi
                + "?apiKey=" + this.portal.getToken()
                + "&httpAccept=application/pdf";
    }


}
