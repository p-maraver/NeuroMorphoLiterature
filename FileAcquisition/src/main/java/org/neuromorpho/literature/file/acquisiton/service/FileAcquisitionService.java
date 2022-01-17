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

package org.neuromorpho.literature.file.acquisiton.service;


import org.neuromorpho.literature.file.acquisiton.exception.LinkNotFoundException;
import org.neuromorpho.literature.file.acquisiton.model.File;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class FileAcquisitionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Set<String> extractFileLinks(File file) {
        try {
            log.debug("Extracting: " + file.toString());
            Connection.Response response = Jsoup.connect(file.getLink())
                    .method(Connection.Method.GET)
                    .followRedirects(true)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36")
                    .execute();
            
            Map<String, String> cookies = response.cookies();
            
            Document doc = Jsoup.connect(file.getLink())
                    .timeout(120 * 1000)
                    .followRedirects(true)
                    .cookies(cookies)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36").get();

            Elements elementList = doc.select("a[href*=" + file.getTypeString() + "]");
            Set<String> pdfList = elementList.stream().
                    map(elem -> elem.attr("abs:href")).
                    collect(Collectors.toSet());

            return pdfList;
        } catch (IOException e) {
            log.error("Error:", e);
            throw new LinkNotFoundException(file.getLink());
        }

    }

}
    

