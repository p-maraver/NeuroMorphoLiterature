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

package org.neuromorpho.literature.search.pubmed.controller;


import org.neuromorpho.literature.search.pubmed.service.PubMedService;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.pubmed.model.Identifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pubmed")
public class PubMedController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PubMedService pubMedService;
    
    /**
     * @apiVersion 1.0.0
     * @api {get} /pubmed?pmid=&db= Get Article PubMed/PubMedCentral 
     * @apiParam {String} pmid The pmid of the article
     * @apiParam {String} db The db to search the article: pubmed, pmc
     *
     * @apiDescription Returns the article data if found on CrossRef
     * @apiName getPubMed
     * @apiGroup Search - PubMed
     *
     * @apiSuccess {Article} article Article object
     * @apiSuccessExample {json} Success-Response:
     *
     *{
     *      "pmid": "34647039",
     *      "pmcid": "8496329",
     *      "title": "Quantitative neuronal morphometry by supervised and unsupervised learning",
     *      "link": null,
     *      "pdfLink": null,
     *      "journal": "STAR protocols",
     *      "doi": "10.1016/j.xpro.2021.100867",
     *      "publishedDate": "2021-09-30",
     *      "authorList": [
     *          {
     *              "name": "Kayvan Bijari",
     *              "email": null,
     *          },
     *          {
     *              "name": "Gema Valera",
     *              "email": null,
     *          }
     * ...
     *          ]
     *}
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Article retrievePubMedArticleData(
            @RequestParam String pmid,
            @RequestParam String db){
        log.debug("Calling pubmed with pmid: " + pmid);
        Article article = pubMedService.retrievePubMedArticleData(pmid, Article.DB.getDB(db));
        log.debug("Data from pubmed: " + article.toString());
        return article;

    }

    /*
     * Retrieves article identifiers from id & db
     */
    /**
     * @apiVersion 1.0.0
     * @api {get} /identifiers?title=&id=&db= Get Identifiers PubMed/PubMedCentral 
     * @apiParam {String} pmid The pmid of the article
     * @apiParam {String} db The database to search the article: pubmed, pmc
     * @apiParam {String} title The title of the article
     *
     * @apiDescription Returns the article identifiers pmid, pmcid, and doi, by id or by title
     * @apiName getPubMedIdentifiers
     * @apiGroup Search - PubMed
     *
     * @apiSuccess {Article} identifiers Identifiers object
     * @apiSuccessExample {json} Success-Response:
     *
     *{
     *      "pmcid": "8496329",
     *      "doi": "10.1016/j.xpro.2021.100867",
     *      "pmid": "34647039"
     *  }
     */
    @RequestMapping(value = "/identifiers", method = RequestMethod.GET)
    public Identifiers retrievePMIDFromTitle(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String id,
            @RequestParam String db)
            throws Exception {
        log.debug("Calling pubmed with title: " + title + " and DB: " + db);
        return pubMedService.retrieveIdentifiersFromTitleDB(title, id, Article.DB.getDB(db));

    }

}
