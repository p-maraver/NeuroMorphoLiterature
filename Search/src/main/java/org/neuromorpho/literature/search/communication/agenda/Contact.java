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

package org.neuromorpho.literature.search.communication.agenda;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Contact {

    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private Boolean unsubscribed;
    private String firstName;
    private String lastName;
    private List<ContactEmail> emailList;

    public Contact() {
    }

    public Contact(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailList = new ArrayList<>();
        this.emailList.add(new ContactEmail(email.trim().toLowerCase(Locale.ROOT), Boolean.FALSE));
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
        return firstName.replace(".", "");
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
                '}';
    }
}
