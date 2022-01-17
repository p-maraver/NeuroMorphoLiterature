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


import com.fasterxml.jackson.annotation.JsonInclude;
import org.neuromorpho.literature.search.dto.fulltext.FigureDto;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SupplementaryMaterialDto {

    private List<FigureDto> attachmentList;
    private String paragraph;
  

    public SupplementaryMaterialDto() {
    }

    public List<FigureDto> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<FigureDto> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }
}
