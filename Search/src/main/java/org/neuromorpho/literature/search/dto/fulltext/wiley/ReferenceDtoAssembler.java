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


import org.neuromorpho.literature.search.dto.fulltext.ReferenceDto;
import org.neuromorpho.literature.search.service.wiley.model.fulltext.Bib;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class ReferenceDtoAssembler {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected List<ReferenceDto> createReferenceListDto(List<Bib> referenceList) {
        List<ReferenceDto> referenceDtoList = new ArrayList();
        try {
            for (Bib reference : referenceList) {
                ReferenceDto referenceDto = this.createReferenceDto(reference);
                referenceDtoList.add(referenceDto);
            }
            return referenceDtoList;
        } catch (NullPointerException ex) {
            return new ArrayList<>();
        }

    }

    private ReferenceDto createReferenceDto(Bib reference) {
        ReferenceDto referenceDto = new ReferenceDto();
        try {
            referenceDto.setAuthorList(reference.getAuthorList());
            referenceDto.setDate(reference.getPubYear());
            referenceDto.setJournal(reference.getJournal());
            referenceDto.setPages(reference.getPageList());
            referenceDto.setTitle(reference.getTitle());
            referenceDto.setVolume(reference.getVolume());
        } catch (NullPointerException ex) {
            log.debug(reference.toString());
            log.error("Error: ", ex);
        }
        return referenceDto;

    }
    

}
