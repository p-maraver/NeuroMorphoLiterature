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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AgendaConnection {

    @Value("${uriAgenda}")
    private String uri;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Contact findContact(String email) {
        try {
            String url = uri + "/contacts?pageIndex=0&pageSize=50&text=" + email;
            log.debug("Creating rest connection for URI: " + url);
            RestTemplate restTemplate = new RestTemplate();
            ContactPage response = restTemplate.getForObject(
                    url, ContactPage.class);
            return response.getTotalElements() > 0 ? response.getContent().get(0): null;
        } catch (Exception ex) {
            log.error("Error: ", ex);
        }
        return null;
    }

    public Contact saveContact(Contact contact) {
        try {
            String url = uri + "/contacts";
            log.debug("Creating rest connection for URI: " + url);
            RestTemplate restTemplate = new RestTemplate();
            Contact response = restTemplate.postForObject(
                    url, contact, Contact.class);
            return response;
        } catch (Exception ex) {
            log.error("Error: ", ex);
        }
        return null;
    }

    public void updateContact(Contact contact) {
        try {
            String url = uri + "/contacts/" + contact.getId();
            log.debug("Creating rest connection for URI: " + url);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(
                    url, contact, void.class);
        } catch (Exception ex) {
            log.error("Error: ", ex);
        }
    }

   
}
