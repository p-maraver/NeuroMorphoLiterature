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
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.Object;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.Para;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class SupplementaryMaterialDtoAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private FigureDtoAssembler attachmentAssembler = new FigureDtoAssembler();

    private Para para;

    protected SupplementaryMaterialDto createSupplementaryMaterialDto(List<Section> sectionList,
                                                                      List<Object> objectList,
                                                                      String token) {
        this.findPara(sectionList);
        if (para != null){
            SupplementaryMaterialDto supplementaryMaterialDto = new SupplementaryMaterialDto();
            List<FigureDto> figureDtoList = attachmentAssembler.createAttachmentListDto(objectList, this.para.getFigureList(), token);
            supplementaryMaterialDto.setParagraph(para.getParagraph());
            supplementaryMaterialDto.setAttachmentList(figureDtoList);
            return supplementaryMaterialDto;
        }
        return null;
    }


    protected void findPara(List<Section> sectionList) {
        if (sectionList != null) {
            Integer i = 0;
            while (this.para == null && i < sectionList.size()) {
                if (para == null) {
                    para = sectionList.get(i).getSupplementaryMaterial();
                }
                this.findPara(sectionList.get(i).getSectionList());
                i++;
            }
        }
    }

}
