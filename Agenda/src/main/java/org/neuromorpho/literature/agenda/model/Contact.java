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

package org.neuromorpho.literature.agenda.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.neuromorpho.literature.agenda.exceptions.ConflictException;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Contact {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private Boolean unsubscribed;
    private String firstName;
    private String lastName;
    private List<ContactEmail> emailList;
    @BsonIgnore
    private List<ObjectId> replacedListId;

    public Contact() {
    }
    public Contact(List<ContactEmail> emailList) {
        this.emailList = emailList;
    }
    
    
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Boolean getUnsubscribed() {
        return unsubscribed;
    }

    public void setUnsubscribed(Boolean unsubscribed) {
        this.unsubscribed = unsubscribed;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ContactEmail> getEmailList() {
        return emailList;
    }

    public List<ObjectId> getReplacedListId() {
        return replacedListId;
    }

    public void setReplacedListId(List<ObjectId> replacedListId) {
        this.replacedListId = replacedListId;
    }

    @JsonIgnore
    @BsonIgnore
    public List<String> getEmailSet() {
        List<String> emailList = new ArrayList<>();
        for (ContactEmail contactEmail: this.emailList){
            if (!contactEmail.getBounced()){
                emailList.add(contactEmail.getEmail());
            }
        }
        return emailList;
    }

    @JsonIgnore
    @BsonIgnore
    public static Contact mergeContacts(List<Contact> contactList) throws ConflictException {
        Contact contact2Merge = contactList.remove(0);
        for (Contact contact : contactList) {
            if (contact2Merge.firstName.toLowerCase().charAt(0) != contact.firstName.toLowerCase().charAt(0)
                    || !contact2Merge.lastName.toLowerCase().equals(contact.lastName.toLowerCase())) {
                throw new ConflictException("Trying to merge contact with different firstName or lastName: " + 
                        " author1: " + contact2Merge.firstName + " " + contact2Merge.lastName +
                        " author2: " + contact.firstName + " " + contact.lastName);
            }
            if (contact2Merge.firstName.length() < contact.firstName.length()) {
                contact2Merge.firstName = contact.firstName;
            }
            contact2Merge.emailList = Contact.mergeEmails(contact2Merge.emailList, contact.emailList);
        }
        return contact2Merge;
    }

    @JsonIgnore
    @BsonIgnore
    public static List<ContactEmail> mergeEmails(List<ContactEmail> emailList2Merge, List<ContactEmail> emailList) {
        for (ContactEmail contactEmail: emailList){
            if (!emailList2Merge.contains(contactEmail)){
                emailList2Merge.add(contactEmail);
            }
        }
        return emailList2Merge;
    }
    
    @BsonIgnore
    @JsonIgnore
    public Boolean existNotBounced(){
        return this.emailList.stream().anyMatch(s -> s.getBounced().equals(Boolean.FALSE));
    }

    public void setEmailList(List<ContactEmail> emailList) {
        this.emailList = emailList;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", unsubscribed=" + unsubscribed +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailList=" + emailList +
                ", replacedListId=" + replacedListId +
                '}';
    }
}
