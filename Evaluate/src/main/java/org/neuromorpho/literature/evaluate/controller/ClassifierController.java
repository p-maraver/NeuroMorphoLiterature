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

import org.neuromorpho.literature.evaluate.model.Classifier;
import org.neuromorpho.literature.evaluate.service.ClassifierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/classifier")
public class ClassifierController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClassifierService service;


    /**
     * @apiVersion 1.0.0
     * @api {get} /classifier Read Classifiers
     *
     *
     * @apiDescription Returns the list of all Classifier data
     * @apiName findAll
     * @apiGroup  Evaluate - Classifier
     *
     * @apiSuccess {Object[]} classifierList The list of trained classifier objects
     * @apiSuccessExample {json} Success-Response:
     * 
     * [
     *      {
     *          "id": "6064bdd2c20718f2bfb76ae9",
     *          "version": "v2",
     *          "date": "2020-11-01",
     *          "testSamples": 399,
     *          "trainSamples": 3231,
     *          "trainLoss": [0.19972947239875793, 0.12341580539941788, ... ]
     *          "testLoss": 0.1046905592083931,
     *          "testAccuracy": 0.8671678900718689,
     *          "current": false,
     *          "status": "TRAINED",
     *          "thresholds": {
     *                  "positiveH": 0.98,
     *                  "positiveL": 0.5,
     *                  "negativeH": 0.025,
     *                  "negativeL": 0.5,
     *                  "positiveLaborSavedH": 70.76023,
     *                  "positiveLaborSavedL": 100,
     *                  "negativeLaborSavedH": 55.86592,
     *                  "negativeLaborSavedL": 100,
     *                  "falseNegativesL": 11.398964,
     *                  "falseNegativesH": 1.5544041,
     *                  "falsePositivesL": 13.106796,
     *                  "falsePositivesH": 3.883495
     *                  },
     *          "classList": [[0, 0.9919941425323486], ... ],
     *          "keywordList": [
     *                  "trace",
     *                  "traced",
     *                  "reconstruct",
     *                  ...]
     *          }
     * ...]
     * 
     */    
    @RequestMapping(method = RequestMethod.GET)
    public List<Classifier> findAll() {
        log.debug("Find classifier data ");
        return service.findAll();
    }


    /**
     * @apiVersion 1.0.0
     * @api {post} /classifier/train Train
     * @apiParam {String} version The version of the classifier
     *
     * @apiDescription Dumps the list of texts on json files, each text is formed by a list of paragraphs,
     * for all the articles by collection (Positive|Negative), trains the classifier, and saves the Classifier 
     * result object on the database
     * @apiName trainClassifier
     * @apiGroup  Evaluate - Classifier
     *
     */  
    @RequestMapping(value = "/train", method = RequestMethod.POST)
    public void trainClassifier(@RequestParam String version) {
        log.debug("Training classifier");
        service.trainClassifier(version);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /file Update File
     * @apiParam {String} name The name of the file to be updated
     * @apiBody {String} content  The content of the file to be updated
     *
     * @apiDescription Updates the keyword files used by the classifier to find relevant paragraphs
     * @apiName updateFile
     * @apiGroup  Evaluate - Classifier
     *
     */ 
    @RequestMapping(value = "/file", method = RequestMethod.PUT)
    public void updateFile(@RequestParam String name,
                           @RequestBody String content) {
        log.debug("Updating file: " + name + " new content: " + content);
        service.updateFile(name, content);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /file Read File
     * @apiParam {String} name The name of the file to be read
     *
     * @apiDescription Returns the content of the keywords file used by the classifier to find relevant paragraphs
     * @apiName readFile
     * @apiGroup  Evaluate - Classifier
     *
     * @apiSuccess {Object} fileContent The content of the keyword file
     * @apiSuccessExample {json} Success-Response:
     *
     * {
     * "text": "#Add relevancy terms that determine if a paragraph is relevant for the classifier\n
     *          #This is a comment line\n
     *          trace\n
     *          traced\n
     *          reconstruct\n
     *          ..."
     * }
     */     
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public Map<String, String> readFile(@RequestParam String name) {
        log.debug("Reading file: " + name);
        String content = service.readFile(name);
        log.debug("Content: " + content);
        Map<String, String> text = new HashMap<>();
        text.put("text", content);
        return text;
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /:id Update Classifier field
     * @apiHeader {String} id unique access-key
     * @apiBody {Object} object Map<String, Object> to be updated
     * @apiDescription Updates a classifier field
     * @apiName updateField
     * @apiGroup  Evaluate - Classifier
     *
     *
     */
    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT)
    public void update(@PathVariable String id, 
                       @RequestBody Map<String, Object> object) {
        log.debug("Updating classifier version to be used");
        Map.Entry<String, Object> entry = object.entrySet().iterator().next();
        service.update(id, entry.getKey(), entry.getValue());
    }
    
}


