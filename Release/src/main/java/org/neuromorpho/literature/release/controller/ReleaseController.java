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

import java.io.IOException;

import org.neuromorpho.literature.release.service.ReleaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/release")
public class ReleaseController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ReleaseService releaseService;

    /**
     * @apiVersion 1.0.0
     * @api {get} /release?version= Dump DB
     * @apiParam {String} version The string version of the release
     * 
     * @apiDescription Dumps the database and labels with a string version.
     * Launches the shell script dumpDB.sh
     * @apiName GetDump
     * @apiGroup  Release - Release
     *
     */
    @RequestMapping(value = "/dump", method = RequestMethod.GET)
    public void dump(@RequestParam(required = true) String version) throws IOException, InterruptedException {
        releaseService.dumpLiteratureDB(version);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /scp2remote?version= SCP DB
     * @apiParam {String} version  The string version of the release
     * 
     * @apiDescription Copies all the dumped collections for the current version DB to the remote server cng.gmu.edu. 
     * Launches the shell script copy2Remote.sh
     * @apiName GetScp2Remote
     * @apiGroup  Release - Release
     *
     */
    @RequestMapping(value = "/scp2remote", method = RequestMethod.GET)
    public void scp2Remote(@RequestParam(required = true) String version) throws IOException, InterruptedException {
        releaseService.scpLiteratureDB(version);
    }
    /**
     * @apiVersion 1.0.0
     * @api {get} /restore?version=&type= Restore DB
     * @apiParam {String} version  The string version of the release
     * @apiParam {String} type  The type of db release: review, main
     *
     * @apiDescription Restores DB data for the version on cng.gmu.edu review or main depending on the type selected.
     * Launches the shell script importDBRemoteMain.sh or importDBRemoteReview.sh
     * @apiName GetRestore
     * @apiGroup  Release - Release
     *
     */
    @RequestMapping(value = "/restore", method = RequestMethod.GET)
    public void restore(@RequestParam(required = true) String version,
            @RequestParam(required = true) String type) throws IOException, InterruptedException {
        releaseService.restoreLiteratureDB(version, type);
    }
    /**
     * @apiVersion 1.0.0
     * @api {get} /scpFromRemote SCP Reports
     *
     * @apiDescription Copies the FrozenEvolution report from remote to local machine to be available for download 
     * Launches the shell script copyFromRemote.sh
     * @apiName GetScpFromRemote
     * @apiGroup  Release - Release
     *
     */
    @RequestMapping(value = "/scpFromRemote", method = RequestMethod.GET)
    public void scpFromRemote() throws IOException, InterruptedException {
        releaseService.scpFromRemote();
    }

}
