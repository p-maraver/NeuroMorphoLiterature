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

package org.neuromorpho.literature.agenda.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ArticleCommunication {

    @Value("${uriArticles}")
    private String uriArticles;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ArticlePage getArticles(Integer page, String status) {
        String url = uriArticles + "/status/" + status + "?page=" + page;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        ArticlePage reconstructionsPage = restTemplate.getForObject(url, ArticlePage.class);
        return reconstructionsPage;
    }

    public List<String> getStatus() {
        String url = uriArticles + "/status";
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        List statusList = restTemplate.getForObject(url, List.class);
        return statusList;
    }

    public void update2Status(String articleId, String reconstructionsStatus) {
        String url = uriArticles + "/reconstructions/" + articleId + "/" + reconstructionsStatus;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, null);
    }
    

}
