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

package org.neuromorpho.literature.search.communication.article;


import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.dto.article.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LiteratureConnection {

    @Value("${uriArticles}")
    private String uri;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ArticleResponse saveArticle(Article article, String collection) {
        try {
            String url = uri + "/" + collection;
            log.debug("Creating rest connection for URI: " + url);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ArticleResponse> response = restTemplate.postForEntity(
                    url, article, ArticleResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            log.error("Error: ", ex);
        }
        return null;
    }

    public void saveSearchPortal(String id, String source, String keyWord) {

        String url = uri + "/" + id + "/searchPortal";
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.put(url, new Search(source, keyWord));
    }

    public void update(String id, String field, Object object) {
        String url = uri + "/" + id + "/" + field;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, object);
    }


}
