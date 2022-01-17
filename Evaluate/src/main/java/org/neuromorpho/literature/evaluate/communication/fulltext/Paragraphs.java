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

package org.neuromorpho.literature.evaluate.communication.fulltext;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Paragraphs {

    private List<String> paragraphList = new ArrayList<>();

    public Paragraphs(List<Section> sectionList, List<Figure> figureList) {
        this.createParagraphList(sectionList);
        if (figureList != null){
            this.paragraphList.addAll(figureList.stream().map(figure -> figure.getCaption()).
                    collect(Collectors.toList()));
        }
    }

    public List<String> getParagraphList() {
        return paragraphList;
    }

    public void setParagraphList(List<String> paragraphList) {
        this.paragraphList = paragraphList;
    }

    private void createParagraphList(List<Section> sectionList) {
        for (Section section : sectionList) {
            this.getParagraphs(section);
        }
    }

    private void getParagraphs(Section section) {
        if (section.getTitle() != null) {
            paragraphList.add(section.getTitle());
        }
        if (section.getParagraphList() != null) {
            paragraphList.addAll(section.getParagraphList());
        }
        if (section.getSectionList() != null) {
            this.createParagraphList(section.getSectionList());
        }
    }
}
