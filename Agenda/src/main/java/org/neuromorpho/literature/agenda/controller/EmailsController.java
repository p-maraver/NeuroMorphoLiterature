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

import org.neuromorpho.literature.agenda.communication.Article;
import org.neuromorpho.literature.agenda.model.Email;
import org.neuromorpho.literature.agenda.service.EmailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
public class EmailsController {

    @Autowired
    private EmailsService emailsService;

    private final FieldsAssembler fieldsAssembler = new FieldsAssembler();

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String home() {
        return "Email up & running!";
    }

    /**
     * @apiVersion 1.0.0
     * @api {post} ?type=&statusDetails= Generate Email
     * @apiParam {String} statusDetails  The type name of the email to be generated: inviteAuthor, reminderAuthor, 
     * pesteringAuthor, ultimatumAuthor, positiveResponseFirstWarn, inviteAuthorAncient, inviteAuthorAvailable,
     * inviteAuthorAncientAvailable
     * @apiParam {String} type The type of neuronal information: glia, neurons
     * @apiBody {Object} data  Article data
     * @apiBody {String} data.title The title of the article
     * @apiBody {String} data.journal The journal of the article
     * @apiBody {Date} data.publishedDate The published date of the article
     * @apiBody {Object[]} data.authorList The list of authors of the article
     * @apiBody {String} data.authorList.name First and last name of the author
     * @apiBody {String} data.authorList.contactId Unique access-key of the Contact object
     *
     * @apiDescription Updates for bulk emails
     * @apiName PostGenerateEmail
     * @apiGroup  Agenda - Emails
     *
     * @apiSuccess {Email} email The email generated for the data provided
     * @apiSuccessExample {json} Success-Response:
     * {
     *          "id":"61b756b4c0ae40429a1e4ada",
     *          "idArticle":"61a8107ebf371a9cbaaf945e",
     *          "type":"inviteAuthorAvailable",
     *          "from":"email@gmu.edu",
     *          "to":["email@bio.puc.cl","email@unab.cl"],
     *          "cc":["email@gmail.com","email@gmu.edu"],
     *          "subject":"Neuronal data sharing invitation/request",
     *          "content":"<p>Dear Drs. Andrew and Valerie,</p>\n<p>I'm writing once again on behalf of <a href=\"http://neuromorpho.org\">NeuroMorpho.Org</a>, 
     *           ... ",
     *         "sentDate":"2021-12-13"
     * }
     */
    @RequestMapping(method = RequestMethod.POST)
    public Email generateEmail(
            @RequestParam(required = true) String statusDetails,
            @RequestParam(required = true) String type,
            @RequestBody Article article) {
        return emailsService.generateEmail(article, fieldsAssembler.getFieldType(statusDetails), type);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /:id Read Email
     * @apiHeader {String} id unique access-key of the article
     *
     * @apiDescription Returns the list of emails sent for the given article id
     * @apiName GetEmail
     * @apiGroup  Agenda - Emails
     *
     * @apiSuccess {Email[]} emailList  The email list sent for the article id
     * @apiSuccessExample {json} Success-Response:
     * 
     *      [{
     *          "id":"61b756b4c0ae40429a1e4ada",
     *          "idArticle":"61a8107ebf371a9cbaaf945e",
     *          "type":"inviteAuthorAvailable",
     *          "from":"email@gmu.edu",
     *          "to":["email@bio.puc.cl","email@unab.cl"],
     *          "cc":["email@gmail.com","email@gmu.edu"],
     *          "subject":"Neuronal data sharing invitation/request",
     *          "content":"<p>Dear Drs. Andrew and Valerie,</p>\n<p>I'm writing once again on behalf of <a href=\"http://neuromorpho.org\">NeuroMorpho.Org</a>, 
     *           ... ",
     *         "sentDate":"2021-12-13"
     *       },
     *       {
     *           ...
     *       }]
     * 
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public List<Email> getEmail(
            @PathVariable String id) {
        return emailsService.findEmail(id);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /send Sends Email
     * @apiBody {String} idArticle
     * @apiBody {String} type
     * @apiBody {String} from
     * @apiBody {String[]} to
     * @apiBody {String[]} cc
     * @apiBody {String} subject
     * @apiBody {String} content
     * @apiBody {String} sentDate
     *
     * @apiDescription Sends an email
     * @apiName PostSendEmail
     * @apiGroup  Agenda - Emails
     *
     */
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public void sendEmail(
            @RequestBody Email email) throws MessagingException {
         emailsService.sendEmail(email);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /bounced Find Bounced
     *
     * @apiDescription Connects to the INBOX and searches for sent emails that had been bounced
     * if found labels the contact accordingly
     * @apiName PutBounced
     * @apiGroup  Agenda - Emails
     *
     */
    @RequestMapping(value = "/bounced", method = RequestMethod.PUT)
    public void extractBouncedFromInbox() {
        emailsService.extractBouncedFrom("INBOX");
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /generateandsend?type=&statusDetails= Generate and send Email
     * @apiParam {String} statusDetails  The type name of the email to be generated: inviteAuthor, reminderAuthor, 
     * pesteringAuthor, ultimatumAuthor, positiveResponseFirstWarn, inviteAuthorAncient, inviteAuthorAvailable,
     * inviteAuthorAncientAvailable
     * @apiParam {String} type The type of neuronal information: glia, neurons
     * @apiBody {Object} data  Article data
     * @apiBody {String} data.title The title of the article
     * @apiBody {String} data.journal The journal of the article
     * @apiBody {Date} data.publishedDate The published date of the article
     * @apiBody {Object[]} data.authorList The list of authors of the article
     * @apiBody {String} data.authorList.name First and last name of the author
     * @apiBody {String} data.authorList.contactId Unique access-key of the Contact object
     *
     * @apiDescription Generates and sends email, it is used for emails sent in bulk that do not need to be reviewed
     * @apiName PostGenerateAndSend
     * @apiGroup  Agenda - Emails
     *
     */
    @RequestMapping(path = "/generateandsend", method = RequestMethod.POST)
    public void generateAndSendEmail(
            @RequestParam(required = true) String statusDetails,
            @RequestParam(required = true) String type,
            @RequestBody Article article) throws MessagingException {
        emailsService.generateAndSendEmail(article, fieldsAssembler.getFieldType(statusDetails), type);
    }

}
