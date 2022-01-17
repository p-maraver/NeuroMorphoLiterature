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

package org.neuromorpho.literature.evaluate.communication.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.types.ObjectId;

public class Author {

    private String name;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId contactId;

    public Author() {
    }

    public Author(String name) {
        this.name = name;
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

    @BsonIgnore
    public Boolean sameLastName(Author author) {
        return this.name.substring(this.name.lastIndexOf(" ") + 1).equals(
                author.name.substring(author.name.lastIndexOf(" ") + 1));
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean hasEmail() {
        if (this.getContactId() != null){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean hasCompleteFirstName() {
        String name = this.name.substring(1, this.name.indexOf(" "));
        name.replaceAll ("\\.","");
        return name.length() > 1;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", contactId=" + contactId +
                '}';
    }
}
