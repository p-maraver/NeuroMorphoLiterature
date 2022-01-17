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

package org.neuromorpho.literature.fulltext.controller;

import org.neuromorpho.literature.fulltext.model.ArticleContent;
import org.neuromorpho.literature.fulltext.communication.articles.Article;
import org.neuromorpho.literature.fulltext.service.ArticleContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping()
public class ArticleContentController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleContentService articleContentService;

    /**
     * @apiVersion 1.0.0
     * @api {get} /:id Read FullText
     * @apiParam {Boolean} images  Return fullText with or without images
        *
     * @apiDescription Returns the FullText by article id, with or without images
     * @apiName GetFullText
     * @apiGroup  FullText
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
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ArticleContent find(@PathVariable String id,
                               @RequestParam(required = false) Boolean images) {
        return articleContentService.find(id, images); 
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /:id/exists Type FullText
     * @apiBody {String} id  unique access-key
     *
     * @apiDescription Returns the type of fulltext available: ABSTRACT, FULLTEXT, RAWTEXT, EMPTY
     * @apiName TypeFullText
     * @apiGroup  FullText
     *
     */
    @RequestMapping(value = "/{id}/exists", method = RequestMethod.GET)
    public ArticleContent.ContentType exists(@PathVariable String id) {
        return articleContentService.exists(id);
    }


    /**
     * @apiVersion 1.0.0
     * @api {put} /:id Create FullText
     * @apiBody {String} id  unique access-key
     * @apiBody {String} title  The title of the article
     * @apiBody {String} abstractContent  The abstract of the article
     * @apiBody {Object} authorGroup The authors of the article
     * @apiBody {String[]} authorGroup.affiliationList The affiliation of the authors
     * @apiBody {Object[]} authorGroup.authorList The list of authors
     * @apiBody {String} authorGroup.authorList.givenName The author given name
     * @apiBody {String} authorGroup.authorList.surname The author surname
     * @apiBody {String} authorGroup.authorList.emails The author email
     * @apiBody {Object[]} sectionList The list of sections of the article
     * @apiBody {String} sectionList.title The title of the section
     * @apiBody {String[]} sectionList.paragraphList The paragraphs of the section
     * @apiBody {Object[]} sectionList.sectionList The list of sub-sections of the article (recursive)
     * @apiBody {Object[]} figureList The list of figures of the article
     * @apiBody {String} figureList.caption The caption of the figure
     * @apiBody {byte[]} figureList.image The array of bytes that form the image of the figure
     * @apiBody {String} figureList.label The label of the figure
     * @apiBody {Object[]} referenceList The list of references used on the article
     * @apiBody {String[]} referenceList.authorList The list of author of the reference
     * @apiBody {String} referenceList.title The title of the reference
     * @apiBody {String} referenceList.journal The journal of the reference
     * @apiBody {String} referenceList.publisherName The publisher name of the reference
     * @apiBody {String} referenceList.publisherLocation The publisher location of the reference
     * @apiBody {String[]} editorList The editor list of the reference
     * @apiBody {String[]} referenceListpages The pages of the reference
     * @apiBody {String} referenceList.volume The volume of the reference
     * @apiBody {Date} referenceList.date The publication date of the reference
     * @apiBody {String} rawText  The text of the article when returned or extract as plain text without sections.
     * @apiBody {String} acknowledgment  The acknowledgments of the article
     *
     * @apiDescription Saves the fulltext of the article on the database
     * @apiName SaveFullText
     * @apiGroup  FullText
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void save(@PathVariable String id,
                     @RequestBody ArticleContent articleContent) {
         articleContentService.save(id, articleContent);
    }
    
}


