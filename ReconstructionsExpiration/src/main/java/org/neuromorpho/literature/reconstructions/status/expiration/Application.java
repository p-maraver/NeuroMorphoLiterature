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

package org.neuromorpho.literature.reconstructions.status.expiration;

import org.neuromorpho.literature.reconstructions.status.expiration.service.StatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private StatusService statusService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).close();
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} standalone Expire Reconstructions Status
     * 
     * @apiDescription Runs as a standalone application, if authors do not respond to the Ultimatum email the reconstructions
     * are labeled automatically as no response if Move to Negative flag is unselected, otherwise the article is moved to the 
     * negative collection
     * @apiName ExpireReconstructions
     * @apiGroup  Reconstructions Expiration
     *
     */
    @Override
    public void run(String... args) throws Exception {
       statusService.updateStatus("Ultimatum", "No response");
    }

}
