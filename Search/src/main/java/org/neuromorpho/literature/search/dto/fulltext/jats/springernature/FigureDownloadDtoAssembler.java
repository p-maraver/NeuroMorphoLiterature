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


package org.neuromorpho.literature.search.dto.fulltext.jats.springernature;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;


public class FigureDownloadDtoAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public byte[] download(String url, String doi) {
        if (!url.contains("MediaObjects")){
            url = "/MediaObjects/" + url;
        }
        String uri = "https://media.springernature.com/lw685/springer-static/image/art:" + doi 
                + "/" + url;

        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = null;
        this.log.debug("Accessing download: " + uri);

        try {
            imageBytes = (byte[]) restTemplate.getForObject(uri, byte[].class, new Object[0]);
        } catch (Exception ex) {
            this.log.error("Error:", ex);
        }

        return imageBytes;
    }

}
