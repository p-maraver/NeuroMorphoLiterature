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

package org.neuromorpho.literature.search.service.springernature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neuromorpho.literature.search.communication.article.ArticleAssembler;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.dto.article.Author;
import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.dto.fulltext.jats.ArticleContentDtoAssembler;
import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.search.model.Portal;
import org.neuromorpho.literature.search.service.PortalSearch;
import org.neuromorpho.literature.search.service.springernature.model.fulltext.FullText;
import org.neuromorpho.literature.search.service.springernature.model.search.Creator;
import org.neuromorpho.literature.search.service.springernature.model.search.Record;
import org.neuromorpho.literature.search.service.springernature.model.search.SearchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class PortalSearchSpringerNatureService extends PortalSearch {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ArticleAssembler assembler = new ArticleAssembler();
    
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public void searchForTitlesApi() throws InterruptedException {
        Integer page = 1;

        /*
         * Springer incorrectly returning media type html instead of json
         */
        RestTemplate restTemplateSearch = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Note: here we are making this converter to process any kind of response, 
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplateSearch.setMessageConverters(messageConverters);
        
        /* ********************************* */

        Integer iterations = 0;
        do {//iterate over pages
            log.debug("Seconds to sleep: " + 5);
            log.debug("......................................");
            Thread.sleep(5 * 1000);
            String uri = this.portal.getSearchUrlApi()
                    + "q=(" + this.keyWord
                    + " AND year:" + this.portal.getStartSearchDate().getYear()
                    + ")&s=" + page
                    + "&api_key=" + this.portal.getToken();
            log.debug("API retrieving from URI: " + uri);
            page++;
            SearchResults searchResults = restTemplateSearch.getForObject(uri, SearchResults.class);

            iterations = searchResults.getTotal() / searchResults.getPageLength();
            log.debug("Articles Found : " + searchResults.getTotal());

            for (Record record : searchResults.getRecordList()) {
                article = new Article();
                article.setTitle(record.getTitle());
                article.setDoi(record.getDoi());
                article.setJournal(record.getJournal());
                article.setPublishedDate(assembler.tryParseDate(record.getPublicationDate()));

                List<Author> authorList = new ArrayList();
                for (Creator creator : record.getCreatorList()) {
                    Author author = new Author(creator.getName(), null);
                    authorList.add(author);
                }
                article.setAuthorList(authorList);
                article.setLink(record.getLink());

                this.saveArticle();
            }
        } while (iterations > 0 && iterations >= page - 1);

    }

    @Override
    public ArticleContentDto getArticleContent(String doi, Portal portal) {
        this.portal = portal;
        ArticleContentDtoAssembler assembler = new ArticleContentDtoAssembler();
        FullText fulltext = this.getFullText(doi);
        if (fulltext != null) {
            return assembler.createArticleContentDto(fulltext.getArticle(), portal, doi);
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


    protected FullText getFullText(String doi) {
        try {

            String uri = portal.getContentUrlApi()
                    + "q=doi:" + doi
                    + "&api_Key=" + this.portal.getToken();

            log.debug("API retrieving from URI full text: " + uri);

            return restTemplate.getForObject(uri, FullText.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error retrieving full text: " + ex.getMessage());
            if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new FullTextNotAvailableException("Id: " + doi + " portal: SpringerNature");
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
