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

package org.neuromorpho.literature.search.service.sciencedirect.model.fulltext;



import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.stream.Collectors;

@XmlAccessorType(XmlAccessType.FIELD)
public class Editors {
    
    @XmlElement(name = "editor", namespace="http://www.elsevier.com/xml/common/struct-bib/dtd")
    private List<Author> editorList;

    public List<Author> getEditorList() {
        return editorList;
    }

    public void setEditorList(List<Author> editorList) {
        this.editorList = editorList;
    }

    public List<String> getEditorListStr() {
        return this.editorList.stream().map(x -> x.getGivenName() + " " + x.getSurname()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Editors{" +
                "editorList=" + editorList +
                '}';
    }
}


