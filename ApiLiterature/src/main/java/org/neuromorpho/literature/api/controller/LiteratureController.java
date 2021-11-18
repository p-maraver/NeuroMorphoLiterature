/*
 * Copyright (c) 2015-2021, Patricia Maraver
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


package org.neuromorpho.literature.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.neuromorpho.literature.api.service.LiteratureService;
import org.neuromorpho.literature.api.service.dto.ArticleDto;
import org.neuromorpho.literature.api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Literature Controller Rest API. Spring MVC Pattern
 */
@RestController
@RequestMapping("/literature")
public class LiteratureController {


    @Autowired
    private LiteratureService literatureService;
    @Autowired
    private ReportService reportService;
    /**
     * <p>
     *     Returns the #summary of articles and reconstructions for each collection
     * </p>
     * @return hashmap that contains the summary
     * @since 1.0
     */
    /**
     * @apiVersion 1.0.0
     * @api {get} /literature/count Count
     * @apiDescription Returns the count summary for articles and reconstructions by status:
     * Available, Not available, Determining availability, Negative
     * @apiName count
     * @apiGroup NeuroMorpho.Org Literature API
     * @apiSuccess {String} label       Status of the article or reconstructions label
     * @apiSuccess {Number} value       #Reconstructions or #Articles
     * @apiSuccessExample {json} Success-Response:
     * {
     * "Determining availability": 357,
     * "Available": 1779,
     * "Negatives": 51525,
     * "Not available": 3210,
     * "Not available.nReconstructions": 158403,
     * "Available.nReconstructions": 188566,
     * "Determining availability.nReconstructions": 31589
     * }
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Map<String, Object> getSummary() {
        return literatureService.getSummary();
    }

    /**
     * <p>
     *     Returns the years for which data exists
     * </p>
     * @return a list of years
     * @since 1.0
     *
     */
    /**
     * @apiVersion 1.0.0
     * @api {get} /literature/years Publication years
     * @apiDescription Returns the years for which there are publications
     * @apiName years
     * @apiGroup NeuroMorpho.Org Literature API
     * @apiSuccess {Number[]} value       List of publication years
     * @apiSuccessExample {json} Success-Response:
     * [2016, 1992, ..., 1997, 1996]
     */
    @RequestMapping(value = "/years", method = RequestMethod.GET)
    public Set<Integer> getPublishedYears() {
        return literatureService.getPublishedYears();
    }

    /**
     * <p>
     *     Generates excel reports FrozenEvolution and CompleteDetails in the folder
     *     specified on app-properties
     * </p>
     * @param type the type of the report (CompleteDetails | FrozenEvolution)
     * @since 1.0
     */
    /**
     * @apiVersion 1.0.0
     * @api {get} /literature/reports?type= Reports
     * @apiParam {String} type Report type: FrozenEvolution|CompleteDetails
     * @apiDescription Generates the Excel reports FrozenEvolution and CompleteDetails in folder pre-defined on application.properties
     * @apiName reports
     * @apiGroup NeuroMorpho.Org Literature API
     */
    @RequestMapping(value = "/reports", method = RequestMethod.GET)
    public void getReport(String type) throws Exception {
        reportService.generateReport(type);
    }

    /**
     * <p>
     *     Returns the articles found by query
     * </p>
     * @param query the hashmap key value that contains the query.
     * @return Page of Article
     * @since 1.0
     */
    /**
     * @apiVersion 1.0.0
     * @api {get} /literature/articles?page=&data.publishedDate=&data.dataUsage= Publications data
     * @apiParam {integer} page Page number
     * @apiParam {String} data.publishedDate Publication year to filter by
     * @apiParam {String} data.dataUsage DataUsage: USING|CITING|SHARING|DESCRIBING_NEURONS|ABOUT to filter by
     * @apiDescription Returns all the publications and it's data found for the query
     * @apiName articles
     * @apiGroup NeuroMorpho.Org Literature API
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "content": [
                {
                    "id": "5ead7db59314014452056e88",
                    "pmid": "33494860",
                    "pmcid": "7837682",
                    "doi": "10.7554/eLife.60936",
                    "link": null,
                    "journal": "eLife",
                    "title": "Data-driven reduction of dendritic morphologies with preserved dendro-somatic responses",
                    "evaluatedDate": 1589414400000,
                    "publishedDate": 1611619200000,
                    "authorList": [
                                    "Willem Am Wybo",
                                    "Jakob Jordan",
                                    "Benjamin Ellenberger",
                                    "Ulisses Marti Mengual",
                                    "Thomas Nevian",
                                    "Walter Senn"
                                    ],
                    "dataUsage": [
                                    "Describing",
                                    "Using",
                                    "Citing"
                                    ],
                    "metadata": {
                        "tracingSystem": ["Neurolucida"],
                        "brainRegion": ["anterior cingulate cortex"],
                        "negativeIfNoAnswer": false,
                        "cellType": ["pyramidal"],
                        "nReconstructions": 1,
                        "species": ["mice"],
                    }
                    "specificDetails": "[No response]",
                    "nReconstructions": 1,
                    "globalStatus": "Not available",
                    "collection": "Positive",
                }, ...
            ],
        "last": false,
        "totalPages": 7,
        "totalElements": 329,
         "sort": null,
        "first": true,
        "numberOfElements": 50,
        "size": 50,
        "number": 0
    }
     */
    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    @ResponseBody
    public Page<ArticleDto> findPublicationsByQuery(@RequestParam HashMap<String, String> query) {
        Integer page = Integer.parseInt(query.get("page"));
        query.remove("page");
        return literatureService.findPublicationsByQuery(page, query);
    }
}
