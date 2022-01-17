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

package org.neuromorpho.literature.release.controller;

import java.util.List;

import org.neuromorpho.literature.release.model.Version;
import org.neuromorpho.literature.release.service.VersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/version")
public class VersionController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VersionService versionService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String home() {
        return "Version up & running!";
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /version Create version
     * @apiBody {String} version The version string of the release
     * @apiBody {String} type The type of the release: literature
     * @apiBody {Date} date The release date
     * @apiDescription Saves the version object
     * @apiName CreateVersion
     * @apiGroup  Release - Version
     *
     */
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void save(
            @RequestBody Version version) {

        versionService.save(version);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /version?type= Read Version
     * @apiParam {String} type  The type of the release: literature
     *
     * @apiDescription Returns the latest deployed version of the literature on NeuroMorpho.Org
     * @apiName GetVersion
     * @apiGroup  Release - Version
     *
     * @apiSuccess {Version} version  The version object
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "id":"5a02080214896411e60bd381",
     *      "version":"8.1.8",
     *      "type":"literature",
     *      "date":"2022-01-04T00:00:00.000+0000"
     * }
     */
    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public Version find(@PathVariable String type) {
        return versionService.find(type);

    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /version Read Versions
     *
     * @apiDescription Returns the list of version values
     * @apiName GetVersions
     * @apiGroup  Release - Version
     *
     * @apiSuccess {String[]} versionList  The list of all the versions released, needed to downgrade
     * @apiSuccessExample {json} Success-Response:
     * ["7.2.1","7.2.2","7.3.1", ...]
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<String> getVersionList() {
        return versionService.findAll();
    }

}
