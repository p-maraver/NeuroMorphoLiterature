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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
public class ArticleCommunication {

    @Value("${uriArticle}")
    private String uri;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public List<String> getStatusList() {
        RestTemplate restTemplate = new RestTemplate();
        String url = uri + "/status";
        return restTemplate.getForObject(url, List.class);

    }

    public ArticlePage getArticles(String status, Integer page, String query) {
        RestTemplate restTemplate = new RestTemplate();
        String url = uri + "/status/" + status + "?&page=" + page + "&" + query;
        log.debug("Creating rest connection for URI: " + url);
        ArticlePage articlePage = restTemplate.getForObject(url, ArticlePage.class);
        log.debug("#Articles: " + articlePage.getTotalElements());
        return articlePage;
    }

    public void updateArticle(String id, String field, Object object) {
        String url = uri + "/" + id + "/" + field;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, object);
    }
    
    public void updateCollection(String id, String newCollection) {
        String url = uri + "/status/" + id + "?newArticleStatus=" + newCollection;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, null);
    }


    public Article getArticle(String id) {
        String url = uri + "/" + id;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        Article article = restTemplate.getForObject(url, Article.class);
        return article;
    }


}
