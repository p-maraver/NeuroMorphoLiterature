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


import org.neuromorpho.literature.search.dto.fulltext.ReferenceDto;
import org.neuromorpho.literature.search.service.jats.model.fulltext.Reference;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.BibReference;
import org.neuromorpho.literature.search.service.sciencedirect.model.fulltext.BibliographySection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ReferenceDtoAssembler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected List<ReferenceDto> createReferenceListDto(BibliographySection referenceList) {
        try {
            List<ReferenceDto> referenceDtoList = new ArrayList();
            for (BibReference reference : referenceList.getBibReference()) {
                ReferenceDto referenceDto = this.createReferenceDto(reference);
                referenceDtoList.add(referenceDto);
            }
            return referenceDtoList;
        } catch (NullPointerException ex) {
            return new ArrayList<>();
        }

    }

    private ReferenceDto createReferenceDto(BibReference reference) {
        ReferenceDto referenceDto = new ReferenceDto();
        try {
            referenceDto.setAuthorList(reference.getAuthorList());
            referenceDto.setDate(reference.getDate());
            referenceDto.setJournal(reference.getJournal());
            referenceDto.setPages(reference.getPageList());
            referenceDto.setTitle(reference.getTitle());
            referenceDto.setVolume(reference.getVolume());
            referenceDto.setPublisherLocation(reference.getPublisherLocation());
            referenceDto.setPublisherName(reference.getPublisherName());
            referenceDto.setEditorList(reference.getEditorList());
        } catch (NullPointerException ex) {
            log.debug(reference.toString());
            log.error("Error: ", ex);
        }
        return referenceDto;

    }

    protected List<ReferenceDto> createReferenceListDto(List<Reference> referenceList) {
        try {
            List<ReferenceDto> referenceDtoList = new ArrayList();
            for (Reference reference : referenceList) {
                ReferenceDto referenceDto = this.createReferenceDto(reference);
                referenceDtoList.add(referenceDto);
            }
            return referenceDtoList;
        } catch (NullPointerException ex) {
            return new ArrayList<>();
        }

    }

    private ReferenceDto createReferenceDto(Reference reference) {
        ReferenceDto referenceDto = new ReferenceDto();
        try {
            referenceDto.setAuthorList(reference.getAuthorList());
            referenceDto.setDate(reference.getDate());
            referenceDto.setJournal(reference.getJournal());
            referenceDto.setPages(reference.getPageList());
            referenceDto.setTitle(reference.getTitle());
            referenceDto.setVolume(reference.getVolume());
//            referenceDto.setPublisherLocation(reference.getPublisherLocation());
//            referenceDto.setPublisherName(reference.getPublisherName());
//            referenceDto.setEditorList(reference.getEditorList());
        } catch (NullPointerException ex) {
            log.debug(reference.toString());
            log.error("Error: ", ex);
        }
        return referenceDto;

    }


}
