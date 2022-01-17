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

package org.neuromorpho.literature.evaluate.communication;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class FileAcquisitionCommunication {

    @Value("${uriFileAcquisition}")
    private String uri;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public List<String> extractFileLinks(String link) {
        log.debug("Extracting PDF links from: " + link);

        RestTemplate restTemplate = new RestTemplate();
        String url = uri;
        File file = new File(link, "PDF");
        log.debug("Creating rest connection for URI: " + url);
        return restTemplate.postForObject(url, file, List.class);

    }

    private class File {
        private String link;
        private String type;

        public File(String link, String type) {
            this.link = link;
            this.type = type;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
