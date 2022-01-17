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

import org.neuromorpho.literature.agenda.exceptions.ConflictException;
import org.neuromorpho.literature.agenda.model.Contact;
import org.neuromorpho.literature.agenda.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("contacts")
public class ContactsController {

    @Autowired
    private ContactsService contactsService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String home() {
        return "Email up & running!";
    }

    /**
     * @apiVersion 1.0.0
     * @api {post} /contacts Create Contact
     * @apiBody {String} firstName  First name
     * @apiBody {String} lastName  Last name
     * @apiBody {String} unsubscribed  Unsubscribes a contact from the mailing list
     * @apiBody {Object[]} emailList    List of email object
     * @apiBody {String} emailList.email Email
     * @apiBody {Boolean} emailList.bounced If the email associated in the object is bounced
     * 
     * @apiDescription Creates a new contact object on the database
     * @apiName PostContact
     * @apiGroup Agenda - Contacts
     * 
     * @apiSuccess {Object} Contact  The contact object created
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "id": "60e6f895718975af0fc7cb00",
     *      "unsubscribed": null,
     *      "firstName": "FirstName",
     *      "lastName": "LastName",
     *      "emailList": [
     *                      {
     *                          "email": "flast@gmu.edu",
     *                          "bounced": false
     *                      }
     *                  ],
     *     "replacedListId": null
     * }
     */
    @RequestMapping(method = RequestMethod.POST)
    public Contact createContact(
            @RequestBody Contact contact) throws ConflictException {
        return contactsService.createContact(contact);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /contacts/:id Update Contact
     * @apiHeader {String} id unique access-key
     * @apiBody {String} firstName  First name
     * @apiBody {String} lastName  Last name
     * @apiBody {String} unsubscribed  Unsubscribes a contact from the mailing list
     * @apiBody {Object[]} emailList    List of email object
     * @apiBody {String} emailList.email Email
     * @apiBody {Boolean} emailList.bounced If the email associated in the object is bounced
     * 
     * @apiDescription Updates an existing contact data by id on the database
     * @apiName PutContact
     * @apiGroup  Agenda - Contacts
     * 
     * @apiSuccess {Contact} contact  The contact object updated
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "id": "60e6f895718975af0fc7cb00",
     *      "unsubscribed": null,
     *      "firstName": "FirstName",
     *      "lastName": "LastName",
     *      "emailList": [
     *                      {
     *                          "email": "flast@gmu.edu",
     *                          "bounced": false
     *                      }
     *                  ],
     *     "replacedListId": null
     * }
     */
    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Contact updateContact(
            @PathVariable String id,
            @RequestBody Contact contact) {
        return contactsService.updateContact(id, contact);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /contacts/:id Read Contact
     * @apiHeader {String} id unique access-key.
     * 
     * @apiDescription Returns the contact data by id from the database
     * @apiName GetContact
     * @apiGroup  Agenda - Contacts
     * 
     * @apiSuccess {Contact} contact The contact object
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "id": "60e6f895718975af0fc7cb00",
     *      "unsubscribed": null,
     *      "firstName": "FirstName",
     *      "lastName": "LastName",
     *      "emailList": [
     *                      {
     *                          "email": "flast@gmu.edu",
     *                          "bounced": false
     *                      }
     *                  ],
     *     "replacedListId": null
     * }
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Contact findContact(
            @PathVariable String id) {
        return contactsService.findContact(id);
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /contacts?pageIndex=&pageSize=&text= Read Contacts
     * @apiParam {integer} pageIndex Page number
     * @apiParam {String} pageSize Page length
     * @apiParam {String} text Text to be filtered in the search. Search is available by
     * first name, last name or email address
     * 
     * @apiDescription Returns a list of contacts by page and size, it could be filtered by text
     * @apiName GetContacts
     * @apiGroup  Agenda - Contacts
     * @apiSuccess {Object} page  The page containing a list of contacts
     * @apiSuccessExample {json} Success-Response:
     * {
     * "content": [
     * {
     *      "id": "60e6f895718975af0fc7cb00",
     *      "unsubscribed": null,
     *      "firstName": "FirstName",
     *      "lastName": "LastName",
     *      "emailList": [
     *                      {
     *                          "email": "flast@gmu.edu",
     *                          "bounced": false
     *                      }
     *                  ],
     *     "replacedListId": null
     * },
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
     * "totalElements": 18985,
     * "last": false,
     * "totalPages": 760,
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
    public Page<Contact> findContacts(@RequestParam(required = true) Integer pageIndex,
                                      @RequestParam(required = true) Integer pageSize,
                                      @RequestParam(required = false) String text) {
        return contactsService.findContactList(pageIndex, pageSize, text);
    }

    /**
     * @apiVersion 1.0.0
     * @api {put} /contacts/export Export Contacts to CSV
     * 
     * @apiDescription Exports all the contacts to csv using the script file extractEmails.sh.
     * Dumps DB contacts collection into the web folder /assets/contacts.csv
     * @apiName PutExport
     * @apiGroup  Agenda - Contacts
     */
    @RequestMapping(value = "/export", method = RequestMethod.PUT)
    public void exportContacts() throws IOException, InterruptedException {
        contactsService.exportContacts();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody
    Map<String, Object> handleDuplicatedException(ConflictException e,
                                                  HttpServletRequest request,
                                                  HttpServletResponse resp) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("errorMessage", e.getMessage());
        return result;
    }


}
