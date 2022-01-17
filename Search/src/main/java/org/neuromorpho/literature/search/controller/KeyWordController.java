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

import org.neuromorpho.literature.search.service.portal.KeyWordService;
import org.neuromorpho.literature.search.model.KeyWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/keywords")
public class KeyWordController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private KeyWordService keyWordService;

    /**
     * @apiVersion 1.0.0
     * @api {get} /keywords?page= Read KeyWords
     * @apiParam {integer} page Page number
     *
     * @apiDescription Returns a list of KeyWords by page
     * @apiName GetKeyWords
     * @apiGroup  Search - KeyWords
     * @apiSuccess {Object} page The page containing a list of KeyWords
     * @apiSuccessExample {json} Success-Response:
     * {
     * "content": [
     *          {
     *              "id": "5bf07cf4931401d30ba87057",
     *              "name": "\"neuromorpho.org\" OR \"neuromorph.org\"",
     *              "collection": "Neuromorpho",
     *          },
     *          {
     *              "id": "5bf336dddfa883485f173cf1",
     *              "name": "TreesToolbox",
     *              "collection": "Pending evaluation",
     *          },
     * ...,
     * "pageable": {
     *      "sort": {
     *          "sorted": false,
     *          "unsorted": true,
     *          "empty": true
     *      },
     *      "pageSize": 25,
     *      "pageNumber": 0,
     *      "offset": 0,
     *      "paged": true,
     *      "unpaged": false
     *      },
     * "totalElements": 716,
     * "last": false,
     * "totalPages": 16,
     * "first": true,
     * "sort": {
     *      "sorted": false,
     *      "unsorted": true,
     *      "empty": true
     *      },
     * "size": 25,
     * "number": 0,
     * "numberOfElements": 25,
     * "empty": false
     * }
     */
    @RequestMapping(method = RequestMethod.GET)
    public Page<KeyWord> findAll(@RequestParam(required = false) Integer page) {
        if (page == null){
            page = 0;
        }
        return keyWordService.findAll(page);
       
    }

    /**
     * @apiVersion 1.0.0
     * @api {post} /keywords Create KeyWord
     * @apiBody {String} id  unique access-key
     * @apiBody {String} name  The keyword for the search
     * @apiBody {String} collection The collection in which the new article will be saved if found with the keywoord
     * 
     * @apiDescription Saves a new KeyWord on the database
     * @apiName PostKeyWord
     * @apiGroup  Search - KeyWords
     *
     */  
    @RequestMapping(method = RequestMethod.POST)
    public void save(
            @RequestBody KeyWord keyWord) {
        keyWordService.save(keyWord);
    }


    /**
     * @apiVersion 1.0.0
     * @api {delete} /keywords/:id Delete KeyWord
     * @apiHeader {String} id unique access-key
     *
     * @apiDescription Delete an existing Keyword data by id on the database
     * @apiName DeleteKeyWord
     * @apiGroup  Search - KeyWords
     *
     */    
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void delete(
            @PathVariable String id) {
        keyWordService.delete(id);
    }

}
