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

package org.neuromorpho.literature.search.controller;

import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class SearchController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SearchService searchService;

    /**
     * @apiVersion 1.0.0
     * @api {get} /start Search Articles Portals
     *
     * @apiDescription Launches the search by keyword over the different portals and saves or updates the articles found
     * on the DB
     * @apiName getSearch
     * @apiGroup Search - Search
     *
     */    
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void launchSearch() throws Exception {
        log.debug("Launching full search");
        searchService.launchSearch();
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /content?id=&db= Get FullText Portals
     * @apiParam {String} id doi, pmid, or pmcid
     * @apiParam {String} portalName The name of the portal: PubMed, ScienceDirect, Wiley, SpringerNature, PubMedCentral
     *
     * @apiDescription Returns the article fulltext if found on the portal
     * @apiName getArticleContent
     * @apiGroup Search - Search
     *
     * @apiSuccess {ArticleContent} fulltext FullText object
     * @apiSuccessExample {json} Success-Response:
     *
     *{
     *      "id": "615b4f9cbf371a059f778c95",
     *      "title": "Quantitative neuronal morphometry by supervised and unsupervised learning",
     *      "abstractContent": "We present a protocol to characterize the morphological properties of individual  ...",
     *      "authorGroup": {
     *          "authorList": [
     *                  {
     *                      "givenName": "Kayvan",
     *                      "surname": "Bijari",
     *                      "email": null
     *                  },
     *                  {
     *                      "givenName": "Gema",
     *                      "surname": "Valera",
     *                      "email": null
     *                  },
     *                  ...
     *                  ],
     * "affiliationList": [
     *          "1Center for Neural Informatics, Structures, & Plasticity and Neuroscience Program, 
     *          Krasnow Institute for Advanced Study, George Mason University, Fairfax, VA 22030, 
     *          USACenter for Neural Informatics,...",
     *              ...
     *          ]
     * },
     * "sectionList": [
     *              {
     *                      "title": "Before you begin",
     *                      "paragraphList": [
     *                      "Technical details of data acquisition, imaging modalities and neuronal tracing 
     *                      are discussed in depth in the original publication (Valera et al., 2021). 
     *                      Continuous advances in both microscopy and computational power are making the
     *                      semi-automated reconstructions of neuronal arbors progres ..."
     *              ],
     * "sectionList": [
     *       ...
     *  }
     */
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public ArticleContentDto getArticleContent(
            @RequestParam String id,
            @RequestParam String portalName) throws Exception {
        return searchService.getArticleContent(id, portalName);
    }


}
