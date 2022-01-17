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

package org.neuromorpho.literature.search.dto.fulltext.jats;


import org.neuromorpho.literature.search.dto.fulltext.FigureDto;
import org.neuromorpho.literature.search.dto.fulltext.jats.pubmed.FigureDownloadDtoAssembler;
import org.neuromorpho.literature.search.model.Portal;
import org.neuromorpho.literature.search.service.jats.model.fulltext.Figure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class FigureDtoAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private FigureDownloadDtoAssembler pubmedDownload =
            new FigureDownloadDtoAssembler();
    private org.neuromorpho.literature.search.dto.fulltext.jats.springernature.FigureDownloadDtoAssembler springerNatureDownload =
            new org.neuromorpho.literature.search.dto.fulltext.jats.springernature.FigureDownloadDtoAssembler();


    protected List<FigureDto> createFigureListDto(List<Figure> figureList, Portal portal, String value) {
        //PubMedCentral
        if (portal.getName().equals("PubMedCentral") && !figureList.isEmpty()) {
            pubmedDownload.download(value);
        }
        List<FigureDto> figureDtoList = new ArrayList();
        for (Figure figure : figureList) {
            FigureDto figureDto = this.createFiguretDto(figure, portal, value);
            figureDtoList.add(figureDto);
        }
        return figureDtoList;
    }

    private FigureDto createFiguretDto(Figure figure, Portal portal, String doi) {
        FigureDto figureDto = new FigureDto();
        figureDto.setLabel(figure.getLabel());
        figureDto.setCaption(figure.getCaption());
        if (portal.getName().equals("PubMedCentral")) {
            figureDto.setImage(pubmedDownload.readFigure(figure.getReference()));
        } else if (portal.getName().equals("SpringerNature")) {
            figureDto.setImage(springerNatureDownload.download(figure.getReference(), doi));
        }
        return figureDto;
    }

}
