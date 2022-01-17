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

import org.neuromorpho.literature.article.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csv")
public class ReportsController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReportService service;

    /**
     * @apiVersion 1.0.0
     * @api {get} /csv?status= Export Articles to CSV
     * @apiParam {String} status The collection to be dumped on CSV: Positive, Negative, Evaluated, 
     * Pending evaluation, Inaccessible
     * @apiParam {Boolean} text Include or not the abstract
     *
     * @apiDescription Dumps on a CSV all the article data from the collection selected to be available to 
     * download
     * @apiName GetGenerateCSV
     * @apiGroup  Articles - Reports
     *
     */
    @RequestMapping(method = RequestMethod.GET)
    public void generateArticlesCsv(@RequestParam String status, 
                                    @RequestParam(required = false) Boolean text) {
        service.generateArticlesCsv(status, text);
    }

    /**
     * @apiVersion 1.0.0
     * @api {delete} /csv?status= Delete CSV
     * @apiParam {String} status The collection to be dumped on CSV: Positive, Negative, Evaluated, 
     * Pending evaluation, Inaccessible
     *
     * @apiDescription Removes the CSV for the collection selected 
     * @apiName DeleteGenerateCSV
     * @apiGroup  Articles - Reports
     *
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteArticlesCsv(@RequestParam String status) {
        service.deleteArticlesCsv(status);
    }
}
