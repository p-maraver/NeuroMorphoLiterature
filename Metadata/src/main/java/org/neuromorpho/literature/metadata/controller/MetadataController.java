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

package org.neuromorpho.literature.metadata.controller;


import org.neuromorpho.literature.metadata.service.MetadataService;
import org.neuromorpho.literature.metadata.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping()
public class MetadataController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetadataService metadataService;

    /**
     * @apiVersion 1.0.0
     * @api {get} /reviewed?type= Read Metadata 
     * @apiParam {String} type The type of metadata to be read: tracingSystem
     *
     * @apiDescription Returns the list of metadata values for the specified type
     * @apiName getMetadata
     * @apiGroup  Metadata
     *
     * @apiSuccess {String[]} metadata list of values
     * @apiSuccessExample {json} Success-Response:
     * [
     *      "Amira",
     *      "Catmaid",
     *      "Imaris",
     *      "Knossos",
     *      ...
     * ]
     */
    @RequestMapping(value = "/reviewed", method = RequestMethod.GET)
    public List<String> getReviewedMetadataValues(
            @RequestParam String type
    ) {
        // return only the values that had been reviewed
        return metadataService.getReviewedByType(type);
    }

}
