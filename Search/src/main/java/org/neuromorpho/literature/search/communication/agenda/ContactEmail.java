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

public class ContactEmail {
    private String email;
    private Boolean bounced;

    public ContactEmail() {
    }

    public ContactEmail(String email, Boolean bounced) {
        this.email = email;
        this.bounced = bounced;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getBounced() {
        return bounced;
    }

    public void setBounced(Boolean bounced) {
        this.bounced = bounced;
    }

    @Override
    public String toString() {
        return "ContactEmail{" +
                "email='" + email + '\'' +
                ", bounced=" + bounced +
                '}';
    }
}
