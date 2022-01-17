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

package org.neuromorpho.literature.search.controller;

import java.util.List;

import org.neuromorpho.literature.search.service.portal.PortalService;
import org.neuromorpho.literature.search.model.Portal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portals")
public class PortalController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PortalService portalService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String home() {
        return "Portal up & running!";
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /portals Read Portals
     *
     * @apiDescription Returns the list of portals
     * @apiName GetPortals
     * @apiGroup  Search - Portals
     * @apiSuccess {Portal[]} Portal The list of portals
     * @apiSuccessExample {json} Success-Response:
     * [{
     *      "id": "57c709dcf139a309cc559a81",
     *      "name": "PubMed",
     *      "startSearchDate": "2021-01-01",
     *      "endSearchDate": "2021-01-01",
     *      "active": true,
     *      "db": "pubmed",
     *      "searchUrlApi": "https://eutils.ncbi.nlm.nih.gov/entrez/eutils",
     *      "contentUrlApi": "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&retmode=xml&id=",
     *      "token": "ddf998847f334d4b89bdff0cfd26117f1808",
     *          "log": {
     *                  "start": "2022-01-10",
     *                  "stop": "2022-01-10",
     *                  "threadId": null,
     *                  "cause": "Finished"
     *              }
     * },
     * {
     *      "id": "57c709dcf139a309cc559a82",
     *      "name": "PubMedCentral",
     *     ...
     * }]
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Portal> findAll() {
        return portalService.findAll();
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /portals Update Portal
     * @apiBody {String} id  unique access-key
     * @apiBody {String} name  Portal name: PubMed, PubMedCentral, ScienceDirect, Wiley, SpringerNature, GoogleScholar
     * @apiBody {Date} startSearchDate The start article published date used for the search
     * @apiBody {Date} endSearchDate The end article published date used for the search
     * @apiBody {String} searchUrlApi The URL API for the portal to search for new articles
     * @apiBody {String} contentUrlApi The URL API for the portal to download fulltext articles
     * @apiBody {String} token The token provided by the Portals to connect to their APIs
     *
     * @apiDescription Updates an existing portal data by id on the database
     * @apiName PutPortal
     * @apiGroup  Search - Portals
     *
     */
    @RequestMapping(method = RequestMethod.PUT)
    public void update(
            @RequestBody Portal portal) {
        portalService.update(portal);
    }
    

}
