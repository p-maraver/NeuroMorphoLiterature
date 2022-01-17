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

import org.neuromorpho.literature.agenda.service.TemplateService;
import org.neuromorpho.literature.agenda.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * @apiVersion 1.0.0
     * @api {get} /templates?type= Read Template
     * @apiParam {String} type  The type name of the template to be retrieved: inviteAuthor, reminderAuthor, 
     * pesteringAuthor, ultimatumAuthor, positiveResponseFirstWarn, inviteAuthorAncient, inviteAuthorAvailable,
     * inviteAuthorAncientAvailable
     *
     * @apiDescription Returns the email template by type
     * @apiName GetTemplate
     * @apiGroup  Agenda - Templates
     *
     * @apiSuccess {Email} template  The email object
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "id":"599ef319148964047c7bf31b",
     *      "type":"inviteAuthor",
     *      "subject":"${subjectType} data sharing invitation/request",
     *      "content":"<p>${dears},</p>
     *                  <p>I'm writing on behalf of&nbsp;<a href=\"http://neuromorpho.org/\">NeuroMorpho.Org</a>, 
     *                  the public NIH-sponsored repository containing over 160,000 digital 
     *                   ... "
     * }
     */
    @RequestMapping(method = RequestMethod.GET)
    public Email findTemplate(@RequestParam(required = true) String type) {
        return templateService.find(type);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /templates/:id Update Template
     * @apiHeader {String} id unique access-key
     * @apiBody {String} id unique access-key
     * @apiBody {String} type The type name of the template to be retrieved
     * @apiBody {String} subject The subject of the email 
     * @apiBody {String} content The content of the email in html format
     *
     * @apiDescription Updates the email template by id
     * @apiName PutTemplate
     * @apiGroup  Agenda - Templates
     *
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateTemplate(
            @PathVariable String id,
            @RequestBody Email template) {
         templateService.update(id, template);
    }
    

}
