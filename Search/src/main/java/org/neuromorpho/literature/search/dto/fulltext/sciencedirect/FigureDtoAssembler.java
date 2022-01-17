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


package org.neuromorpho.literature.search.dto.fulltext.sciencedirect;


import org.neuromorpho.literature.search.dto.fulltext.FigureDto;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.Figure;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class FigureDtoAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Integer seconds = 5;
    protected List<FigureDto> createAttachmentListDto(List<Object> objectList,
                                                      List<Figure> figureList, String token) {
        List<FigureDto> figureDtoList = new ArrayList();
        if (figureList != null) {
            for (Figure figure : figureList) {
                for (Object object : objectList) {
                    if (object.getRef().equals(figure.getLocator()) && object.getType().equals("IMAGE-DOWNSAMPLED")) {
                        FigureDto figureDto = this.createAttachmentDto(object, figure, token);
                        figureDtoList.add(figureDto);
                    }
                }
            }
        }

        return figureDtoList;

    }

    private FigureDto createAttachmentDto(Object object, Figure figure, String token) {
        FigureDto figureDto = new FigureDto();
        figureDto.setLabel(figure.getLabel());
        figureDto.setCaption(figure.getCaption());
        figureDto.setImage(this.download(object.getUrl(), token));
        return figureDto;
    }


    public byte[] download(String url, String token) {
        String uri = url.substring(0, url.indexOf("?") + 1) + token;

        RestTemplateTimeout restTemplate = new RestTemplateTimeout();
        byte[] imageBytes = null;
        this.log.debug("Accessing download: " + uri);

        try {
            imageBytes = (byte[]) restTemplate.getForObject(uri, byte[].class, new java.lang.Object[0]);
            log.debug("Seconds to sleep: " + seconds);
            Thread.sleep(seconds * 1000);
            log.debug("......................................");
        } catch (Exception ex) {
            this.log.error("Error:", ex);
        }

        return imageBytes;
    }

}
