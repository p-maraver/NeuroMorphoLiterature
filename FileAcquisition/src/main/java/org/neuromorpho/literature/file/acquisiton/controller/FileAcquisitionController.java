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

package org.neuromorpho.literature.file.acquisiton.controller;

import org.neuromorpho.literature.file.acquisiton.model.File;
import org.neuromorpho.literature.file.acquisiton.service.FileAcquisitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping()
public class FileAcquisitionController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileAcquisitionService service;

    /**
     * @apiVersion 1.0.0
     * @api {post} / Extract URL links
     * @apiBody {String} link The link from which to search for files available to download
     * @apiBody {String} type The type of file: PDF, JPG
     *
     * @apiDescription Returns the list of URL links found for the file type
     * @apiName PostExtractFileLinks
     * @apiGroup  FileAcquisition
     *
     * @apiSuccess {String[]} URL links  The list of Url links object
     * @apiSuccessExample {json} Success-Response:
     * [
     *      "https://bpspsychub.onlinelibrary.wiley.com/doi/epdf/10.1111/j.2044-8260.1981.tb00489.x",
     *      "https://bpspsychub.onlinelibrary.wiley.com/doi/pdf/10.1111/j.2044-8260.1981.tb00489.x"
     * ]
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Set<String> extractFileLinks(@RequestBody File file) {
        return service.extractFileLinks(file);
    }
    
    
}


