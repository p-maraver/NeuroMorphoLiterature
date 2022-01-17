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


import org.neuromorpho.literature.search.service.ContentHandler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class ProtocolSection {

    @XmlElement(name = "title")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private String title;

    @XmlElement(name = "p")
    @XmlJavaTypeAdapter(value = ContentHandler.class)
    private List<String> paraList;

    @XmlElement(name = "protocolStep")
    private List<ProtocolStep> protocolStepList;

    @XmlElement(name = "figure")
    private List<Figure> figureList;

    public String getTitle() {
        return title;
    }

    public List<ProtocolStep> getProtocolStepList() {
        return protocolStepList;
    }

    public List<Figure> getFigureList() {
        return figureList;
    }

    public void fillFigureList(List<Figure> figureList) {
        if (this.figureList != null) {
            figureList.addAll(this.figureList);
        }
        if (this.protocolStepList != null) {
            for (ProtocolStep step : this.protocolStepList) {
                if (step.getFigureList() != null) {
                    figureList.addAll(step.getFigureList());
                }
            }
        }
    }

    public List<String> getParaList() {
        List<String> paraList = new ArrayList<>();
        Integer stepNumber = 1;
        if (this.paraList != null) {
            paraList.addAll(this.paraList);
        }
        if (this.protocolStepList != null) {
            for (ProtocolStep step : this.protocolStepList) {
                step.getParaList().set(0, stepNumber + ". " + step.getParaList().get(0));
                paraList.addAll(step.getParaList());
                stepNumber++;
            }
        }
        return paraList;
    }


}


