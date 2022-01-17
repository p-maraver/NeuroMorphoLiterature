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

package org.neuromorpho.literature.search.crossref.service;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.neuromorpho.literature.search.communication.article.ArticleAssembler;
import org.neuromorpho.literature.search.crossref.model.CrossRefArticle;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.search.pubmed.model.Identifiers;
import org.neuromorpho.literature.search.pubmed.service.PubMedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class CrossRefService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    protected PubMedService pubMedService;

    RestTemplate restTemplate = new RestTemplate();
    ArticleAssembler articleAssembler = new ArticleAssembler();


    public Article retrieveArticleData(String doi) {

        String url = "https://api.crossref.org/works/" + doi;
        log.debug("Accesing crossRef using url: " + url);

        ResponseEntity<CrossRefArticle> response = restTemplate.getForEntity(url, CrossRefArticle.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            CrossRefArticle crossRefArticle = response.getBody();

            Article article = articleAssembler.createArticle(crossRefArticle);
            Identifiers identifiers = pubMedService.retrieveIdentifiersFromTitleDB(article.getTitle(), null, Article.DB.PUBMED);
            if (identifiers != null) {
                article.setPmid(identifiers.getPmid());
                article.setPmcid(identifiers.getPmcid());
            }

            return article;
        } else if (HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
            throw new FullTextNotAvailableException("DOI not found in CrossRef");
        } else {
            throw new FullTextNotAvailableException("Unknown Exception accessing CrossRef");
        }

    }


    public String retrievePdfLink(String doi, String pmcid) {

        String url = "https://api.crossref.org/works/" + doi;
        log.debug("Accesing crossRef using url: " + url);
        ResponseEntity<CrossRefArticle> responseCrossRef = restTemplate.getForEntity(url, CrossRefArticle.class);

        if (HttpStatus.OK.equals(responseCrossRef.getStatusCode())) {
            CrossRefArticle crossRefArticle = responseCrossRef.getBody();
            return crossRefArticle.getPdfLink();
        } else if (HttpStatus.NOT_FOUND.equals(responseCrossRef.getStatusCode())) {
            //Workaround depending portal
            this.getPDFFromPubMedCentral(pmcid);

            throw new FullTextNotAvailableException("DOI not found in CrossRef");
        } else {
            throw new FullTextNotAvailableException("Unknown Exception accessing CrossRef");
        }
    }

    private String getPDFFromPubMedCentral(String pmcid) {
        try {
            Document searchDoc = Jsoup.connect("https://www.ncbi.nlm.nih.gov/pmc/articles/PMC" + pmcid)
                    .timeout(60 * 1000)
                    .followRedirects(true)
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36").get();

            Element elem = searchDoc.select("div[class=format-menu] > li > a").first();
            String pdfLink = "https://www.ncbi.nlm.nih.gov/" + elem.attr("href");

            return pdfLink;

        } catch (NullPointerException ex) {
            throw new FullTextNotAvailableException("PDF not found in PubMedCentral web page");

        } catch (IOException e) {
            throw new FullTextNotAvailableException("Unable to load PubMedCentral web page");
        }
    }

}
