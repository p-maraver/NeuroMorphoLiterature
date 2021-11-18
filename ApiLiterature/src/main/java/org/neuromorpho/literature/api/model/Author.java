/*
 * Copyright (c) 2015-2021, Patricia Maraver
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

package org.neuromorpho.literature.api.model;


/**
 *  Author of the Article Model. Spring MVC Pattern
 */
public class Author{

    private String name;
    private String email;

    public Author() {
    }

    public Author(String name, String email) {
        this.name = name;
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void mergeAuthorEmail(Author newAuthor) {
        if (newAuthor.email != null && this.email == null) {
            this.email = newAuthor.email;
        }
    }

    public void mergeAuthorName(Author newAuthor) {
        if (newAuthor.name.length() > this.name.length()) {
            this.name = newAuthor.name;
        }
    }

    public Boolean hasContactEmail() {
        return this.email != null && !this.email.isEmpty();
    }

}
