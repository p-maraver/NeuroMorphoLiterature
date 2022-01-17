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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class Issue {
    
    @XmlElement(name = "series", namespace="http://www.elsevier.com/xml/common/struct-bib/dtd")
    private Series series;
    @XmlElement(name = "date", namespace="http://www.elsevier.com/xml/common/struct-bib/dtd")
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class) 
    private LocalDate date;

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVolume() {
        return this.series.getVolume();
    }

    public String getJournal() {
        return this.series.getJournal();
    }

    @Override
    public String toString() {
        return "Issue{" +
                "series=" + series +
                ", date=" + date +
                '}';
    }
}


