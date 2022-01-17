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

package org.neuromorpho.literature.fulltext.communication.articles;


import org.neuromorpho.literature.fulltext.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.fulltext.model.ArticleContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@Component
public class SearchCommunication {

    @Value("${uriSearch}")
    private String uri;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RestTemplate restTemplate;

    public ArticleContent getArticleContent(String id, String portalName) {
        try {
            String url = uri + "/content?" + "id=" + id 
                    + "&portalName=" + portalName;
            log.debug("Creating rest connection for URI: " + url);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<Object>(headers);
            ResponseEntity<ArticleContent> out = restTemplate.exchange(url, HttpMethod.GET, entity, ArticleContent.class);
            if (out.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
                throw new FullTextNotAvailableException("Article id: " + id + " portal: " + portalName);
            }
            return out.getBody();
        } catch (HttpServerErrorException ex) {
            log.error("Error:", ex);
        }
        return null;
    }

    public byte[] getPdf(String doi, String pmcid) {
        String url = uri + "/crossref/pdf?" + "doi=" + doi;
        if (pmcid != null){
            url = url + "&pmcid=" + pmcid;
        }
        log.debug("Creating rest connection for URI: " + url);
        byte[] pdf = restTemplate.getForObject(url, byte[].class);
        return pdf;
    }


}
