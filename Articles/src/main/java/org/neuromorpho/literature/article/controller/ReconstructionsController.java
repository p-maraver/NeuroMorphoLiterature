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

import org.neuromorpho.literature.article.service.ReconstructionsService;
import org.neuromorpho.literature.article.repository.ArticleSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reconstructions")
public class ReconstructionsController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    
    @Autowired
    private ReconstructionsService reconstructionsService;

    
    
    /**
     * @apiVersion 1.0.0
     * @api {get} /reconstructions/specificDetails Values Reconstructions Statuses
     *
     * @apiDescription Returns the different values available to be used to label the reconstructions status
     * download
     * @apiName GetSpecificDetails
     * @apiGroup  Articles - Reconstructions
     *
     * @apiSuccess {String[]} specificDetails The list of specific details
     * @apiSuccessExample {json} Success-Response:
     *
     *[
     *      "To be requested",
     *      "Positive response",
     *      "Invited",
     *      "Reminder",
     *       ... 
     * ]
     */
    @RequestMapping(value = "/specificDetails", method = RequestMethod.GET)
    public List<String> findSpecificDetailsValues() {
        log.debug("Reading valid values for specificDetails");
        return reconstructionsService.findSpecificDetailsValues();

    }
    
    
    /**
     * @apiVersion 1.0.0
     * @api {get} /reconstructions/count?expired= Count Reconstructions Status
     * @apiParam {Boolean} expired Indicates if the expired date for requesting the reconstructions is in the past
     *
     * @apiDescription Returns the #articles and #reconstructions by reconstructions status
     * @apiName GetCount
     * @apiGroup  Articles - Reconstructions
     *
     * @apiSuccess {ArticleSummary[]}  Article list summary object
     * @apiSuccessExample {json} Success-Response:
     *
     * [
     *      {
     *          "specificDetails": "Invited",
     *          "status": "Determining availability",
     *          "nArticles": 20,
     *          "nSharedArticles": 0,
     *          "nReconstructions": 1981
     *      },
     *      {
     *          "specificDetails": "Lost data",
     *          "status": "Not available",
     *          "nArticles": 261,
     *          "nSharedArticles": 4.052631578947368,
     *          "nReconstructions": 12825
     *      },
     *      ...
     * ]
     */
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public List<ArticleSummary> getSummary(
            @RequestParam Boolean expired) {
       
        log.debug("Getting reconstructions summary, expired: " + expired);
        return reconstructionsService.getSummaryReconstructions(expired);
    }

    
}
