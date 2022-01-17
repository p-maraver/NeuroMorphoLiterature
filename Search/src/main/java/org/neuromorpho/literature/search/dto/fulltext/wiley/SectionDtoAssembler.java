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


import org.neuromorpho.literature.search.dto.fulltext.SectionDto;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.Protocol;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.ProtocolMaterials;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.ProtocolSection;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.Section;

import java.util.ArrayList;
import java.util.List;


public class SectionDtoAssembler {

    protected List<SectionDto> createSectionListDto(List<Section> sectionList) {
        List<SectionDto> sectionDtoList = new ArrayList();
        if (sectionList != null) {
            for (Section section : sectionList) {
                SectionDto sectionDto = this.createSectionDto(section);
                sectionDtoList.add(sectionDto);
            }
        }
        return sectionDtoList;

    }

    protected List<SectionDto> createSectionListDtoFromProtocol(List<Protocol> protocolList) {
        List<SectionDto> sectionDtoList = new ArrayList();
        if (protocolList != null) {
            for (Protocol protocol : protocolList) {
                SectionDto sectionDto = this.createSectionDto(protocol);
                sectionDtoList.add(sectionDto);
            }
        }
        return sectionDtoList;

    }

    private SectionDto createSectionDto(Section section) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setTitle(section.getTitle());
        sectionDto.setParagraphList(section.getParaList());
        sectionDto.setSectionList(this.createSectionListDto(section.getSectionList()));
        return sectionDto;
    }

    private SectionDto createSectionDto(Protocol protocol) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setTitle(protocol.getTitle());
        sectionDto.setParagraphList(protocol.getParaList());
        
        List<SectionDto> sectionDtoList = new ArrayList<>();
        if (protocol.getProtocolMaterials() != null) {
            SectionDto materials = this.createSectionDto(protocol.getProtocolMaterials());
            sectionDtoList.add(materials);
        }
        for (ProtocolSection protocolSection: protocol.getProtocolSectionList()) {
            sectionDtoList.add(this.createSectionDto(protocolSection));
        }
        sectionDto.setSectionList(sectionDtoList);
        return sectionDto;
    }

    private SectionDto createSectionDto(ProtocolMaterials protocol) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setTitle(protocol.getTitle());
        sectionDto.setParagraphList(protocol.getParaList());
        return sectionDto;
    }

    private SectionDto createSectionDto(ProtocolSection protocol) {
        SectionDto sectionDto = new SectionDto();
        sectionDto.setTitle(protocol.getTitle());
        sectionDto.setParagraphList(protocol.getParaList());
        return sectionDto;
    }



}
