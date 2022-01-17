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

package org.neuromorpho.literature.reconstructions.status.expiration.communication;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LiteratureCommunication {

    @Value("${uriArticles}")
    private String uriArticles;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ArticlePage getArticles(Integer page, String status, Date expirationDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(expirationDate);
        String url = uriArticles + "/status/Positive?reconstructions.currentStatusList.specificDetails=" + status + "&reconstructions.currentStatusList.expirationDate=lt:" + date + "&page=" + page;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        ArticlePage reconstructionsPage = restTemplate.getForObject(url, ArticlePage.class);
        log.debug("#Articles in status Ultimatum " + reconstructionsPage.getTotalElements());

        return reconstructionsPage;
    }

    public void update(String id, Reconstructions reconstructions) {
        String url = uriArticles + "/" + id + "/reconstructions";
        log.debug("Creating rest connection for URI: " + url);
        log.debug("updating reconstructions: " + reconstructions.toString());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, reconstructions);
    }

    public void updateArticle2Negative(String id) {
        String url = uriArticles + "/status/" + id + "?newArticleStatus=Negative";
        log.debug("Creating rest connection for URI: " + url);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, null);
    }

}
