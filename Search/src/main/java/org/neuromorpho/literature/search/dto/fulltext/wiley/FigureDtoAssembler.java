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

package org.neuromorpho.literature.search.dto.fulltext.wiley;


import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.neuromorpho.literature.search.dto.fulltext.FigureDto;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.Figure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


public class FigureDtoAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    protected List<FigureDto> createFigureListDto(List<Figure> figureList) {
        List<FigureDto> figureDtoList = new ArrayList();
        for (Figure figure : figureList) {
            FigureDto figureDto = this.createFiguretDto(figure);
            figureDtoList.add(figureDto);
        }
        return figureDtoList;
    }

    private FigureDto createFiguretDto(Figure figure) {
        FigureDto figureDto = new FigureDto();
        figureDto.setLabel(figure.getLabel());
        figureDto.setCaption(figure.getCaption());
        for(String link: figure.getLinkList()) {
            figureDto.setImage(this.download(link));
        }
        return figureDto;
    }

    public byte[] download(String url) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "chocolate cookie");
        try {

            byte[] imageBytes = restTemplate.exchange(url,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    byte[].class).getBody();
            return imageBytes;
        } catch (Exception ex) {
            log.error("Error:", ex);
        }

        return null;
    }

}
