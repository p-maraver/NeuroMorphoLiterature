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

package org.neuromorpho.literature.agenda.controller;

import org.neuromorpho.literature.agenda.model.Config;
import org.neuromorpho.literature.agenda.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;


    /**
     * @apiVersion 1.0.0
     * @api {put} / Update Config
     * @apiBody {String} id unique access-key
     * @apiBody {String} username The email 
     * @apiBody {String} password The password of the email 
     *
     * @apiDescription Updates the config
     * @apiName PutConfig
     * @apiGroup  Agenda - Config
     *
     */
    @RequestMapping(method = RequestMethod.PUT)
    public void update(
            @RequestBody Config config) {
        configService.update(config);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} / Read Config
     *
     * @apiDescription Returns the config values
     * @apiName GetConfig
     * @apiGroup  Agenda - Config
     *
     * @apiSuccess {Config} config  The config email object
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "id": "601c6b2adfa88308ac768511",
     *      "username": "email@gmu.edu",
     *      "password": "password"
     * }
     */
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public Config find() {
        return configService.find();
    }


}
