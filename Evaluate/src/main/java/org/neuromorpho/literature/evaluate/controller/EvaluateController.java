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

package org.neuromorpho.literature.evaluate.controller;

import org.neuromorpho.literature.evaluate.service.EvaluateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping()
public class EvaluateController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EvaluateService service;

    /**
     * @apiVersion 1.0.0
     * @api {get} /:status Evaluate Articles
     * @apiParam {String} status The collection which to evaluate
     *
     *
     * @apiDescription Evaluates the relevancy of all the articles in collection
     * @apiName EvaluateArticles
     * @apiGroup  Evaluate - Evaluate
     *
     */
    @RequestMapping(method = RequestMethod.GET)
    public void evaluateText(
            @RequestParam String status,
            @RequestParam(required = false) String classified) {
        log.debug("Evaluating text");
        if (status != null) {
            if (classified == null){
                classified = "false";
            }
            service.evaluateText(status, classified);
        } else {
            service.evaluateText();
        }
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /:id Evaluate Article
     * @apiHeader {String} id unique access-key
     * 
     *
     * @apiDescription Evaluates the relevancy of an article
     * @apiName EvaluateArticle
     * @apiGroup  Evaluate - Evaluate
     *
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void evaluateTextById(@PathVariable String id,
                                 @RequestParam(required = false) Boolean replace) {
        if (replace == null){
            replace = Boolean.FALSE;
        }
        service.evaluateTextById(id, replace);
    }


    
}


