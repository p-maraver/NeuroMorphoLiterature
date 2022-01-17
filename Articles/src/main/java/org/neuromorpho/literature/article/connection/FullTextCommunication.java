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

package org.neuromorpho.literature.article.connection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class FullTextCommunication {

    @Value("${uriFullText}")
    private String uri;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ArticleContent getFullText(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = uri + "/" + id + "?images=false";
        log.debug("Creating rest connection for URI: " + url);
        ArticleContent articleContent = restTemplate.getForObject(url, ArticleContent.class);
        return articleContent;
    }

}
