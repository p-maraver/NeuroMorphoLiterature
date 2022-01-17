
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

package org.neuromorpho.literature.search.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public class Author {

    private String name;
    private String email;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId contactId;

    public Author(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getContactId() {
        return contactId;
    }

    public void setContactId(ObjectId contactId) {
        this.contactId = contactId;
    }

    @JsonIgnore
    public String getFirstName(){
        return name.substring(0, name.lastIndexOf(' ') + 1).trim().replace(".", "");
    }

    @JsonIgnore
    public String getLastName(){
        return name.substring(name.lastIndexOf(' ') + 1, name.length()).trim();
    }
    
    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contactId=" + contactId +
                '}';
    }

}
