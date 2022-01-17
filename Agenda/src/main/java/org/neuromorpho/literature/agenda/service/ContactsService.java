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

package org.neuromorpho.literature.agenda.service;

import org.neuromorpho.literature.agenda.repository.contacts.ContactRepository;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.agenda.exceptions.ConflictException;
import org.neuromorpho.literature.agenda.model.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ContactsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContactRepository contactRepository;

    public Contact createContact(Contact contact) throws ConflictException {
        log.debug("Creating new contact: " + contact.toString());
        List<Contact> contactList = contactRepository.findContactList(contact);
        if (contactList.size() == 0) {// insert new contact
            return contactRepository.addContact(contact);
        } else if (contactList.size() == 1) {
            Contact oldContact = contactList.get(0);
            if (contact.getEmailSet().size() > oldContact.getEmailSet().size()) {
                //update contact
                contactRepository.update(oldContact.getId(), "emailList", contact.getEmailList());
                oldContact.setEmailList(contact.getEmailList());
            }
            if (contact.getFirstName().length() > oldContact.getFirstName().length()) {
                contactRepository.update(oldContact.getId(), "firstName", contact.getFirstName());
                oldContact.setFirstName(contact.getFirstName());
            }
            return oldContact;
        } else { //merge authors
            Contact contactMerged = Contact.mergeContacts(contactList);
            List<ObjectId> contactIdList = contactList.stream().map(c -> c.getId())
                    .collect(Collectors.toList());
            contactRepository.delete(contactIdList);
            contactRepository.updateContact(contactMerged.getId().toString(), contactMerged);
            contactMerged.setReplacedListId(contactIdList);
            return contactMerged;
        }
    }
    
    public Page<Contact> findContactList(Integer pageIndex, Integer pageSize, String text) {
        log.debug("Finding contact list, pageIndex: " + pageIndex + " pageSize: " + pageSize);
        Page<Contact> contactList = contactRepository.findContactList(pageIndex, pageSize, text);
        log.debug("Total elements: " + contactList.getTotalElements());
        return contactList;
    }

    public Contact findContact(String id) {
        log.debug("Finding contact: " + id);
        return contactRepository.findContact(new ObjectId(id));
    }

    public Contact updateContact(String id,
                              Contact contact) {
        log.debug("Updating contact: " + contact.toString());
        List<ObjectId> replacedListId = contactRepository.deleteByEmail(id, contact.getEmailSet());
        contactRepository.updateContact(id, contact);
        contact.setReplacedListId(replacedListId);
        return contact;
    }

    public void exportContacts() throws IOException, InterruptedException {
        
        log.debug("Exporting contacts to csv ./extractEmails.sh" );
        Process p = Runtime.getRuntime().exec("./extractEmails.sh");
        p.waitFor();
        log.debug("DONE exporting csv");
    }
}
