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

package org.neuromorpho.literature.search.service.wiley.model.fulltext;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Body {

    @XmlElement(name = "section")
    private List<Section> sectionList;

    @XmlElement(name = "protocol")
    private List<Protocol> protocolList;

    @XmlElement(name = "bibliography")
    private Bibliography bibliography;

    private List<Figure> figureList = new ArrayList<>();

    public List<Section> getSectionList() {
        return this.sectionList;
    }

    public List<Protocol> getProtocolList() {
        return this.protocolList;
    }

    public List<Bib> getReferenceList() {
        return this.bibliography.getBib();
    }

    public String getAcknowledgment() {
        String content = "";
        for (Section section1 : sectionList) {
            if (section1.getTitle() != null
                    &&
                    (section1.getTitle().toLowerCase().contains("acknowledgments") ||
                            section1.getTitle().toLowerCase().contains("acknowledgements"))) {
                for (String para : section1.getParaList()) {
                    content = content + para + "\n";
                }
                sectionList.remove(section1);
                break;
            } else {
                if (section1.getSectionList() != null) {
                    for (Section section2 : section1.getSectionList()) {
                        if (section2.getTitle() != null
                                &&
                                (section2.getTitle().toLowerCase().contains("acknowledgments") ||
                                        section2.getTitle().toLowerCase().contains("acknowledgements"))) {
                            for (String para : section2.getParaList()) {
                                content = content + para + "\n";
                            }
                            sectionList.remove(section2);
                            break;
                        }
                    }
                }
            }
        }
        return content;
    }
    
    public List<Figure> getFigureList() {
        return figureList;
    }

    public List<Figure> fillFigureList() {
        for (Section section : sectionList) {
            section.fillFigureList(figureList);
        }
        for (Protocol protocol : protocolList) {
            protocol.fillFigureList(figureList);
        }
        return figureList;
    }


}


