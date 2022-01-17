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

package org.neuromorpho.literature.article.controller;

import org.neuromorpho.literature.article.exceptions.DuplicatedException;
import org.neuromorpho.literature.article.model.article.Article;
import org.neuromorpho.literature.article.model.article.ArticleData;
import org.neuromorpho.literature.article.model.article.Contact;
import org.neuromorpho.literature.article.model.article.ReconstructionsStatus;
import org.neuromorpho.literature.article.service.LiteratureService;
import org.neuromorpho.literature.article.service.dto.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
public class LiteratureController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LiteratureService literatureService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String home() {
        return "Literature up & running!";
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /count?date= Count Articles Collection
     * @apiParam {Date} date Used to filter before and after the date
     *
     * @apiDescription Returns the #articles by collection
     * @apiName GetSummary
     * @apiGroup  Articles - Literature
     *
     * @apiSuccess {Object} summary The summary of articles by collection
     * @apiSuccessExample {json} Success-Response:
     *  {
     *      "Citing": 599,
     *      "Using": 672,
     *      "Evaluated": 323,
     *      ...
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Map<String, Long> getSummary(
            @RequestParam(required = false) String date) {
        return literatureService.getSummary(date);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /count/usage Count Articles Usage
     *
     * @apiDescription Returns the #articles grouped by data usage: using, citing, about, sharing, duplicate, describing
     * @apiName GetSummaryUsage
     * @apiGroup  Articles - Literature
     *
     * @apiSuccess {Object[]} summary The summary of articles by collection
     * @apiSuccessExample {json} Success-Response:
     * [
     *      {
     *          "_id": ["CITING","USING"],
     *          "count": 17
     *      },
     *      {
     *          "_id": ["CITING","ABOUT"],
     *          "count": 9
     *      },
     *      ...
     */
    @RequestMapping(value = "/count/usage", method = RequestMethod.GET)
    public List<Object> getSummaryByDataUsage() {
        return literatureService.getSummaryByDataUsage();
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /:id Read Article
     * @apiHeader {String} id unique access-key
     * 
     * @apiDescription Returns the article data
     * @apiName GetArticle
     * @apiGroup  Articles - Literature
     *
     * @apiSuccess {Article} article The article object
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "id": "615b4f9cbf371a059f778c95",
     *      "data": {
     *              "pmid": "34647039",
     *              "pmcid": "8496329",
     *              "doi": "10.1016/j.xpro.2021.100867",
     *              "link": "https://api.elsevier.com/content/article/pii/S2666166721005736",
     *      ...
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ArticleDto findArticle(@PathVariable String id) {
        return literatureService.findArticle(id);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /status Values Collection 
     *
     * @apiDescription Returns the different values available to be used as collections
     * download
     * @apiName GetStatus
     * @apiGroup  Articles - Literature
     *
     * @apiSuccess {String[]} collections The list of collections
     * @apiSuccessExample {json} Success-Response:
     *
     *[
     *      "Pending evaluation",
     *      "Positive",
     *      "Negative",
     *       ... 
     * ]
     */
    @RequestMapping(value = "status", method = RequestMethod.GET)
    public List<String> findStatus() {
        List<String> statusList = new ArrayList<>();
        for (Article.ArticleStatus status: Article.ArticleStatus.values()){
            statusList.add(status.getStatus());
        }
        return statusList;
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /status/:status Read Articles
     * @apiHeader {String} status articles collection 
     * @apiParam {Object} queryParams query by attributes like Page number, text, pmid .. 
     *
     * @apiDescription Returns a list of articles by page and filtered by query
     * @apiName GetArticles
     * @apiGroup  Articles - Literature
     * @apiSuccess {Object} page  The page containing a list of articles
     * @apiSuccessExample {json} Success-Response:
     * {
     * "content": [
     * {
     *      "id": "615b4f9cbf371a059f778c95",
     *      "data": {
     *              "pmid": "34647039",
     *              "pmcid": "8496329",
     *              "doi": "10.1016/j.xpro.2021.100867",
     *              "link": "https://api.elsevier.com/content/article/pii/S2666166721005736",
     *      ...
     * },
     * ...,
     * "pageable": {
     *      "sort": {
     *          "sorted": false,
     *          "unsorted": true,
     *          "empty": true
     *      },
     *      "pageSize": 25,
     *      "pageNumber": 0,
     *      "offset": 0,
     *      "paged": true,
     *      "unpaged": false
     *      },
     * "totalElements": 18985,
     * "last": false,
     * "totalPages": 760,
     * "first": true,
     * "sort": {
     *      "sorted": false,
     *      "unsorted": true,
     *      "empty": true
     *      },
     * "size": 25,
     * "number": 0,
     * "numberOfElements": 25,
     * "empty": false
     * }
     */
    @RequestMapping(value = "status/{status}", method = RequestMethod.GET)
    public Page<ArticleDto> findArticleList(
            @PathVariable String status,
            @RequestParam(required = false) Map<String, String> queryParams) throws ParseException {
        log.debug("Find articles by text status : " + status + " and page: " + queryParams.get("page"));
        return literatureService.findArticleList(status, queryParams);
       
    }
    
    /**
     * @apiVersion 1.0.0
     * @api {get} /status/:status Create Manual Article
     * @apiBody {Object} data  The article data
     * @apiBody {String} data.pmid The PubMed id of the article
     * @apiBody {String} data.pmcid The PubMedCentral id of the article 
     * @apiBody {String} data.doi The doi of the article
     * @apiBody {String} data.link The URL link were the article can be read
     * @apiBody {String} data.pdfLink The URL link were the PDF of the article can be read
     * @apiBody {String} data.journal The journal of the article
     * @apiBody {String} data.title The title of the article
     * @apiBody {Author[]} data.authorList The list of authors of the article
     * @apiBody {Date} data.publishedDate The published date of the article
     * @apiBody {Date} data.ocDate The date when the article is found
     * @apiBody {Date} data.evaluatedDate The date when the article is evaluated
     * @apiBody {Date} data.approvedDate The date when the article is approved
     * @apiBody {String[]} dataUsage The list of data usage for the article: Describing, Using, Citing, About, Sharing, Duplicate
     * @apiBody {String} fulltext The type of text there is available for the article: FullText, Abstract, RawText, Empty
     * @apiBody {Object} searchPortal The portals and keywords where it is found
     * @apiBody {Object} metadata The metadata values
     * 
     * @apiBody {Object} reconstructions The reconstructions associated to the article if any
     * @apiBody {Object[]} reconstructions.reconstructionsList The reconstructions associated to the article if any
     * @apiBody {String} reconstructions.reconstructionsList.id unique access-key
     * @apiBody {String} reconstructions.reconstructionsList.statusDetails The status of the reconstructions
     * @apiBody {Date} reconstructions.reconstructionsList.expirationDate Date of expiration for the reconstructions status
     * @apiBody {Double} reconstructions.reconstructionsList.nReconstructions Number of reconstructions of the article
     * @apiBody {Date} reconstructions.reconstructionsList.date The date when the status is set for the reconstructions
     * @apiBody {Article[]} reconstructions.reconstructionsList.sharedList The list of articles that share reconstructions with the given article
     *
     * @apiBody {Boolean} locked If the articles is being edited
     * @apiBody {Object} classifier The relevancy result obtained by the classifier
     * @apiBody {String}  classifier.articleStatus The classifier guess for the collection result: Positive H, Positive L, Negative H, Negative L
     * @apiBody {Float} classifier.confidence The likelihood returned by the classifier
     * @apiBody {String[]} classifier.keyWordList The keywords found on the fulltext by the classifier
     * @apiBody {String[]} classifier.termList The terms found on the fulltext by the classifier
     * @apiBody {Object} classifier.metadata The metadata found on the fulltext by the classifier
     * 
     * @apiBody {Article} duplicate The related article if it is a duplicate with different name

     * @apiDescription Saves the article in the database
     * @apiName PostArticle
     * @apiGroup  Articles - Literature
     * @apiSuccess {Article} article  The article object created
     * @apiSuccessExample {json} Success-Response:
     * {
     *   "_id": ObjectId("615b4f9cbf371a059f778c95"),
     *   "classifier": {
     *     "articleStatus": "Positive H",
     *     "confidence": 99.75539398193359375,
     *     "keyWordList": [
     *       "tracing",
     *       "github",
     *       "morphology",
     *       "neuronal",
     *       "tree",
     *       "neuron",
     *       "neurite",
     *       "branch",
     *       "glia",
     *       "glial",
     *       "neuromorpho",
     *       "axonal",
     *       "arbor",
     *       "reconstruction",
     *       "dendritic",
     *       "swc"
     *     ],
     *     "metadata": {
     *       "cellType": [
     *         "glia"
     *       ],
     *       "articleStatus": "Positive",
     *       "nReconstructions": 1,
     *       "tracingSystem": [
     *
     *       ]
     *     },
     *     "termList": [
     *       "diagram",
     *       "image",
     *       "tracing",
     *       "morphological",
     *       "quantification",
     *       "morphology",
     *       "reconstruction",
     *       "dendritic",
     *       "sholl"
     *     ]
     *   },
     *   "data": {
     *     "approvedDate": new Date("2021-10-14T02:00:00+0200"),
     *     "authorList": [
     *       {
     *         "name": "Kayvan Bijari"
     *       },
     *       {
     *         "name": "Gema Valera"
     *       },
     *       {
     *         "name": "Hernán López-Schier"
     *       },
     *       {
     *         "name": "Giorgio A Ascoli"
     *       }
     *     ],
     *     "dataUsage": [
     *       "USING",
     *       "CITING",
     *       "ABOUT"
     *     ],
     *     "doi": "10.1016/j.xpro.2021.100867",
     *     "evaluatedDate": new Date("2021-10-14T02:00:00+0200"),
     *     "fulltext": "FULLTEXT",
     *     "journal": "STAR Protocols",
     *     "link": "https://api.elsevier.com/content/article/pii/S2666166721005736",
     *     "ocDate": new Date("2021-10-04T02:00:00+0200"),
     *     "publishedDate": new Date("2021-12-17T01:00:00+0100"),
     *     "title": "Quantitative neuronal morphometry by supervised and unsupervised learning",
     *     "pmid": "34647039",
     *     "pmcid": "8496329"
     *   },
     *   "metadata": {
     *     "cellType": [
     *       "glia"
     *     ],
     *     "negativeIfNoAnswer": false,
     *     "comment": "This is a protocol. Paper citing ans using reconstructions from Neuromorpho. Giorgio is an authors CT (+)",
     *     "nReconstructions": 1,
     *     "tracingSystem": [
     *
     *     ]
     *   },
     *   "searchPortal": {
     *     "ScienceDirect": [
     *       "SWC AND (neuron OR glia)"
     *     ],
     *     "PubMedCentral": [
     *       "\"neuromorpho.org\" OR \"neuromorph.org\"",
     *       "SWC AND (neuron OR glia)"
     *     ]
     *   },
     * }
     * ]
     * }
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Article saveArticleManual(
            @RequestBody ArticleDto articleDto) {
        return literatureService.saveArticleManual(articleDto);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /status/:status Create Automated Article
     * @apiBody {Object} data  The article data
     * @apiBody {String} data.pmid The PubMed id of the article
     * @apiBody {String} data.pmcid The PubMedCentral id of the article 
     * @apiBody {String} data.doi The doi of the article
     * @apiBody {String} data.link The URL link were the article can be read
     * @apiBody {String} data.pdfLink The URL link were the PDF of the article can be read
     * @apiBody {String} data.journal The journal of the article
     * @apiBody {String} data.title The title of the article
     * @apiBody {Author[]} data.authorList The list of authors of the article
     * @apiBody {Date} data.publishedDate The published date of the article
     * @apiBody {Date} data.ocDate The date when the article is found
     * @apiBody {Date} data.evaluatedDate The date when the article is evaluated
     * @apiBody {Date} data.approvedDate The date when the article is approved
     * @apiBody {String[]} dataUsage The list of data usage for the article: Describing, Using, Citing, About, Sharing, Duplicate
     * @apiBody {String} fulltext The type of text there is available for the article: FullText, Abstract, RawText, Empty
     * @apiBody {Object} searchPortal The portals and keywords where it is found
     * @apiBody {Object} metadata The metadata values
     *
     * @apiBody {Object} reconstructions The reconstructions associated to the article if any
     * @apiBody {Object[]} reconstructions.reconstructionsList The reconstructions associated to the article if any
     * @apiBody {String} reconstructions.reconstructionsList.id unique access-key
     * @apiBody {String} reconstructions.reconstructionsList.statusDetails The status of the reconstructions
     * @apiBody {Date} reconstructions.reconstructionsList.expirationDate Date of expiration for the reconstructions status
     * @apiBody {Double} reconstructions.reconstructionsList.nReconstructions Number of reconstructions of the article
     * @apiBody {Date} reconstructions.reconstructionsList.date The date when the status is set for the reconstructions
     * @apiBody {Article[]} reconstructions.reconstructionsList.sharedList The list of articles that share reconstructions with the given article
     *
     * @apiBody {Boolean} locked If the articles is being edited
     * @apiBody {Object} classifier The relevancy result obtained by the classifier
     * @apiBody {String}  classifier.articleStatus The classifier guess for the collection result: Positive H, Positive L, Negative H, Negative L
     * @apiBody {Float} classifier.confidence The likelihood returned by the classifier
     * @apiBody {String[]} classifier.keyWordList The keywords found on the fulltext by the classifier
     * @apiBody {String[]} classifier.termList The terms found on the fulltext by the classifier
     * @apiBody {Object} classifier.metadata The metadata found on the fulltext by the classifier
     *
     * @apiBody {Article} duplicate The related article if it is a duplicate with different name

     * @apiDescription Saves the article in the database
     * @apiName PostArticleAutomated
     * @apiGroup  Articles - Literature
     * @apiSuccess {Article} article  The article object created
     * @apiSuccessExample {json} Success-Response:
     * {
     *   "_id": ObjectId("615b4f9cbf371a059f778c95"),
     *   "classifier": {
     *     "articleStatus": "Positive H",
     *     "confidence": 99.75539398193359375,
     *     "keyWordList": [
     *       "tracing",
     *       "github",
     *       "morphology",
     *       "neuronal",
     *       "tree",
     *       "neuron",
     *       "neurite",
     *       "branch",
     *       "glia",
     *       "glial",
     *       "neuromorpho",
     *       "axonal",
     *       "arbor",
     *       "reconstruction",
     *       "dendritic",
     *       "swc"
     *     ],
     *     "metadata": {
     *       "cellType": [
     *         "glia"
     *       ],
     *       "articleStatus": "Positive",
     *       "nReconstructions": 1,
     *       "tracingSystem": [
     *
     *       ]
     *     },
     *     "termList": [
     *       "diagram",
     *       "image",
     *       "tracing",
     *       "morphological",
     *       "quantification",
     *       "morphology",
     *       "reconstruction",
     *       "dendritic",
     *       "sholl"
     *     ]
     *   },
     *   "data": {
     *     "approvedDate": new Date("2021-10-14T02:00:00+0200"),
     *     "authorList": [
     *       {
     *         "name": "Kayvan Bijari"
     *       },
     *       {
     *         "name": "Gema Valera"
     *       },
     *       {
     *         "name": "Hernán López-Schier"
     *       },
     *       {
     *         "name": "Giorgio A Ascoli"
     *       }
     *     ],
     *     "dataUsage": [
     *       "USING",
     *       "CITING",
     *       "ABOUT"
     *     ],
     *     "doi": "10.1016/j.xpro.2021.100867",
     *     "evaluatedDate": new Date("2021-10-14T02:00:00+0200"),
     *     "fulltext": "FULLTEXT",
     *     "journal": "STAR Protocols",
     *     "link": "https://api.elsevier.com/content/article/pii/S2666166721005736",
     *     "ocDate": new Date("2021-10-04T02:00:00+0200"),
     *     "publishedDate": new Date("2021-12-17T01:00:00+0100"),
     *     "title": "Quantitative neuronal morphometry by supervised and unsupervised learning",
     *     "pmid": "34647039",
     *     "pmcid": "8496329"
     *   },
     *   "metadata": {
     *     "cellType": [
     *       "glia"
     *     ],
     *     "negativeIfNoAnswer": false,
     *     "comment": "This is a protocol. Paper citing ans using reconstructions from Neuromorpho. Giorgio is an authors CT (+)",
     *     "nReconstructions": 1,
     *     "tracingSystem": [
     *
     *     ]
     *   },
     *   "searchPortal": {
     *     "ScienceDirect": [
     *       "SWC AND (neuron OR glia)"
     *     ],
     *     "PubMedCentral": [
     *       "\"neuromorpho.org\" OR \"neuromorph.org\"",
     *       "SWC AND (neuron OR glia)"
     *     ]
     *   },
     * }
     * ]
     * }
     */
    @RequestMapping(value = "/{status}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleDto saveArticleAutomatedSearch(
            @PathVariable String status,
            @RequestBody ArticleData article) {
        String id = literatureService.saveArticleAutomatedSearch(article, Article.ArticleStatus.getArticleStatus(status));
        return new ArticleDto(id);
    }

   
    @ExceptionHandler(DuplicatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    Map<String, Object> handleDuplicatedException(DuplicatedException e,
                                                  HttpServletRequest request,
                                                  HttpServletResponse resp) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("errorMessage", e.getMessage());
        return result;
    }


    /**
     * @apiVersion 1.0.0
     * @api {put} /status/:id Update Article Collection
     * @apiHeader {String} id unique access-key
     * @apiParam {String} newArticleStatus The new collection of the article
     * @apiParam {String} specificDetails The reconstructions status

     * @apiDescription Updates the collection and sets a value for the reconstructions if provided
     * @apiName UpdateCollection
     * @apiGroup  Articles - Literature
     *
     */
    @RequestMapping(value = "/status/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCollection(
            @PathVariable String id,
            @RequestParam String newArticleStatus,
            @RequestParam(required = false) String specificDetails) {
        ReconstructionsStatus.SpecificDetails details = null;
        if (specificDetails != null){
            details =  ReconstructionsStatus.SpecificDetails.getSpecificDetails(specificDetails);
        } else {
            details =  ReconstructionsStatus.SpecificDetails.TO_BE_REQUESTED;
        }
        literatureService.updateStatus(id,
                Article.ArticleStatus.getArticleStatus(newArticleStatus),
                details);
    }

    /**
     * @apiVersion 1.0.0
     * @api {delete} /:id Delete Article
     * @apiHeader {String} id unique access-key
     *
     * @apiDescription Removed the article from the database
     * @apiName DeleteArticle
     * @apiGroup  Articles - Literature
     *
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteArticle(@PathVariable String id) {
        literatureService.deleteArticle(id);

    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /reconstructions/:id/:reconstructionsStatus Update Article Reconstructions
     * @apiHeader {String} id unique access-key of the article
     * @apiHeader {String} reconstructionsStatus The current status of the reconstructions to be updated
     *
     * @apiDescription Updates the reconstructions to the next status expected
     * @apiName UpdateReconstructions
     * @apiGroup  Articles - Literature
     *
     */
    @RequestMapping(value = "/reconstructions/{id}/{reconstructionsStatus}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update2NextReconstructionsStatus(
            @PathVariable String id,
            @PathVariable String reconstructionsStatus) {
        literatureService.update2NextReconstructionsStatus(id, reconstructionsStatus);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /:id/:field Update Article Field
     * @apiHeader {String} id unique access-key of the article
     * @apiHeader {String} field The name of the field article
     * @apiBody {Object} object The field value object
     *
     * @apiDescription Updates the field of the article
     * @apiName UpdateFiled
     * @apiGroup  Articles - Literature
     *
     */
    @RequestMapping(value = "/{id}/{field}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void update(
            @PathVariable String id,
            @PathVariable String field,
            @RequestBody Object object) {
        literatureService.update(id, field, object);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /author Update Author
     * @apiBody {Object} contact The contact 
     * @apiBody {String} id unique access-key
     * @apiBody {String} firstName of the author
     * @apiBody {String} lastName of the author
     * @apiBody {String[]} replacedListId The list of unique identifiers to be updated
     * @apiDescription Updates an author in several articles to the new data provided
     * @apiName UpdateAuthor
     * @apiGroup  Articles - Literature
     *
     */
    @RequestMapping(value = "/author", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateContacts(
            @RequestBody Contact contact) {
        literatureService.updateContacts(contact);
    }
    

}
