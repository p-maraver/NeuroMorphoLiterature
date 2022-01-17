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

package org.neuromorpho.literature.search.service.wiley;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neuromorpho.literature.search.communication.article.ArticleAssembler;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.dto.article.Author;
import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.dto.fulltext.wiley.ArticleContentDtoAssembler;
import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.search.model.Portal;
import org.neuromorpho.literature.search.service.PortalSearch;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.Figure;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.FullTextWiley;
import org.neuromorpho.literature.search.service.wiley.model.search.Record;
import org.neuromorpho.literature.search.service.wiley.model.search.SearchResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PortalSearchWileyService extends PortalSearch {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ArticleAssembler assembler = new ArticleAssembler();
    private ArticleContentDtoAssembler assemblerFulltext = new ArticleContentDtoAssembler();

    @Override
    public void searchForTitlesApi() throws InterruptedException {
        String replacedKeyword = "(" + this.keyWord + ")";
        String year = Integer.toString(this.portal.getStartSearchDate().getYear());
        Integer startRecordPosition = 1;

        Boolean next = Boolean.FALSE;
        try {

            do {//iterate over pages

                String uri = this.portal.getSearchUrlApi()
                        + "query=cql.anywhere=" + replacedKeyword
                        + " and dc.date>" + year
                        + "&startRecord=" + startRecordPosition;
                log.debug("API retrieving from URI: " + uri);


                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                requestFactory.setHttpClient(httpClient);
                RestTemplate restTemplate = new RestTemplate(requestFactory);

                HttpHeaders headers = new HttpHeaders();
                headers.add("Cookie", "chocolate cookie");
                SearchResults searchResults = restTemplate.exchange(uri,
                        HttpMethod.GET,
                        new HttpEntity<String>(headers),
                        SearchResults.class).getBody();

                next = Boolean.FALSE;

                if (!searchResults.isEmpty()) {
                    log.debug("Articles Found : " + searchResults.getNumberOfRecords());

                    for (Record record : searchResults.getRecordList()) {
                        article = new Article();
                        if (record.getContributorList() != null) {
                            article.setTitle(record.getTitle());
                            article.setDoi(record.getDoi());
                            article.setJournal(record.getJournal());
                            article.setPublishedDate(assembler.tryParseDate(record.getPublicationDate()));

                            List<Author> authorList = new ArrayList();
                            for (String contributor : record.getContributorList()) {
                                Author author = new Author(contributor, null);
                                authorList.add(author);
                            }
                            article.setAuthorList(authorList);
                            article.setLink(record.getUrl());

                            this.saveArticle();
                        }
                    }

                    startRecordPosition = searchResults.getNextRecordPosition();
                    next = startRecordPosition != null && searchResults.getNumberOfRecords() >= startRecordPosition;
                }
            } while (next);
        } catch (RestClientException ex) {
            log.error("Error calling Wiley", ex);
        }
    }

    @Override
    public ArticleContentDto getArticleContent(String doi, Portal portal) {
        this.portal = portal;
        ArticleContentDtoAssembler assembler = new ArticleContentDtoAssembler();
        FullTextWiley fulltext = this.getFullText(doi);
        if (fulltext != null) {
            fulltext.fillFigureList();
            this.fillImagesLink(doi, fulltext.getFigureList());
            return assemblerFulltext.createArticleContentDto(fulltext, portal, doi);
        }
        throw new FullTextNotAvailableException("XML link empty");
        
    }

    protected FullTextWiley getFullText(String doi) {
        String uri = portal.getContentUrlApi() + "full-xml/" + doi;
        log.debug("API retrieving from URI full text: " + uri);

        try {

            RequestConfig config = RequestConfig.custom().setCircularRedirectsAllowed(true).build();
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .setDefaultRequestConfig(config)
                    .build();

            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            RestTemplate restTemplate = new RestTemplate(requestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cookie", "chocolate cookie");

            FullTextWiley result = restTemplate.exchange(uri,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    FullTextWiley.class).getBody();
            return result;
        } catch (HttpClientErrorException ex) {
            log.error("Error retrieving full text: " + ex.getMessage());
            if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)
                    || ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new FullTextNotAvailableException("Link: " + uri + " portal: Wiley");
            }
            return null;
        }
    }

    protected void fillImagesLink(String doi, List<Figure> figureList) {
        try {

            // Images must be downloaded from HTML 
            String uri = portal.getContentUrlApi() + "full/" + doi;
            log.debug("API retrieving from URI full text: " + uri);
            Document document = null;
            document = Jsoup.connect(uri)
                    .timeout(60 * 1000)
                    .followRedirects(true)
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36").get();


            for (Figure figure : figureList) {
                for (String id: figure.getIdList()) {
                    log.debug("Reading figure id: " + figure.getIdList());
                    Element element = document.getElementById(id);
                    if (element != null) {
                        String link = element.select("a img[class=figure__image]").attr("src");
                        if (link.isEmpty()) {
                            link = element.select("img[class=figure__image]").attr("src");
                        }
                        figure.addLink("https://onlinelibrary.wiley.com" + link);
                        if (figure.getCaption() == null) {
                            try {
                                String caption = element.select("div[class=figure__caption__header] strong[class=figure-title]").first().text();
                                figure.setLabel(caption);
                            } catch (NullPointerException ex) {
                                log.debug("No caption found for that label");
                            }
                        }
                    } else {
                        Elements elementByClassList = document.getElementsByClass("figure");
                        if (elementByClassList.size() == 0){
                            elementByClassList = document.getElementsByClass("image");
                        }
                        for (Element elementByClass : elementByClassList) {
                            Elements linkElements = elementByClass.select("a img[class=figure__image]");
                            List<String> linkList = new ArrayList<>();
                            for (Element linkElement: linkElements){
                                linkList.add(linkElement.attr("src"));
                            }
                            for (String link: linkList) {
                                if (link.contains(id.toLowerCase())) {
                                    figure.addLink("https://onlinelibrary.wiley.com" + link);
                                    if (figure.getCaption() == null) {
                                        try {
                                            String caption = element.select("div[class=figure__caption__header] strong[class=figure-title]").first().text();
                                            figure.setLabel(caption);
                                        } catch (NullPointerException ex) {
                                            log.debug("No caption found for that label");
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                    }
                }

            }
        } catch (IOException e) {
            log.error("Exception reading URL to retrieve Wiley images: " + doi);
        }
    }

    @Override
    protected Elements findArticleList() {
        return null;
    }

    @Override
    protected void searchPage() throws InterruptedException, IOException {

    }

    @Override
    protected Boolean loadNextPage() throws InterruptedException, IOException {
        return null;
    }

    @Override
    protected String fillTitle(Element articleData) {
        return null;
    }

    @Override
    protected void fillPublishedDate(Element articleData, Element articlePage) {

    }

    @Override
    protected void fillJournal(Element articleData, Element articlePage) {

    }

    @Override
    protected void fillAuthorList(Element articleData, Element articlePage) {

    }

    @Override
    protected void fillDoi(Element articleData, Element articlePage) {

    }

    @Override
    protected void fillLinks(Element articleData, Element articlePage) {

    }

}
