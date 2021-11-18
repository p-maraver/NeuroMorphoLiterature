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


import org.neuromorpho.literature.api.model.Version;
import org.neuromorpho.literature.api.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Version Controller Rest API. Spring MVC Pattern
 */
@RestController
@RequestMapping("/version")
public class VersionController {

    @Autowired
    private VersionService service;

    /**
     * <p>
     *     Returns the version object for the latest date
     * </p>
     * @param type the type of the version (neurons | literature)
     * @return Version
     * @since 1.0
     */
    /**
     * @apiVersion 1.0.0
     * @api {get} /version?type=literature Version
     * @apiDescription Returns the current version and date
     * @apiName version
     * @apiGroup NeuroMorpho.Org Literature API
     * @apiSuccess {String} version Version number
     * @apiSuccess {String} type Literature
     * @apiSuccess {String} date Release date
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "version": "8.1.6",
     *      "type": "literature",
     *      "date": 1635375600000
     * }
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Version getVersion(@RequestParam String type) {
        return service.getVersion(type);
    }
  
}
