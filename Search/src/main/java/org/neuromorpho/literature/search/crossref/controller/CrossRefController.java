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

package org.neuromorpho.literature.search.crossref.controller;


import org.neuromorpho.literature.search.crossref.service.CrossRefService;
import org.neuromorpho.literature.search.dto.article.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class CrossRefController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrossRefService crossRefService;

    /**
     * @apiVersion 1.0.0
     * @api {get} /crossref?doi= Get Article CrossRef 
     * @apiParam {String} doi The doi of the article
     *
     * @apiDescription Returns the article data if found on CrossRef
     * @apiName getCrossRef
     * @apiGroup Search - CrossRef
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
    @RequestMapping(value = "/crossref", method = RequestMethod.GET)
    public Article retrieveCrossRefArticleData(String doi) {
        log.debug("Calling crossref with doi: " + doi);
        Article article = crossRefService.retrieveArticleData(doi);
        log.debug("Data from crossef: " + article.toString());
        return article;

    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /crossref/pdf?doi= Get PDF Link CrossRef 
     * @apiParam {String} doi The doi of the article
     *
     * @apiDescription Returns the PDF link if found on CrossRef
     * @apiName getCrossRefPDF
     * @apiGroup Search - CrossRef
     *
     * @apiSuccess {String} link PDF link 
     * @apiSuccessExample {json} Success-Response:
     *
     *{
     *      https://onlinelibrary.wiley.com/doi/pdf/10.1111/cpr.13165
     *  }
     */
    @RequestMapping(value = "/crossref/pdf", method = RequestMethod.GET)
    public String retrievePdfLink(String doi, String pmcid) {
        log.debug("Calling crossref with doi: " + doi + " pmcid: " + pmcid);
        return crossRefService.retrievePdfLink(doi, pmcid);
    }
    

}
