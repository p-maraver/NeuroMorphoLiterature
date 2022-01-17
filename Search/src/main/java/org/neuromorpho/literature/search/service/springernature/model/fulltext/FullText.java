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

package org.neuromorpho.literature.search.service.springernature.model.fulltext;


import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.search.service.jats.model.fulltext.Article;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class FullText {

    @XmlElement(name = "records")
    private Records records;

    @XmlElement(name = "result")
    private Result result;
    
    public Article getArticle(){
        if (this.result.getTotal() == 0){
            throw new FullTextNotAvailableException("Fulltext not open source");
        }
        return this.records.getArticle();
    }
    
}


