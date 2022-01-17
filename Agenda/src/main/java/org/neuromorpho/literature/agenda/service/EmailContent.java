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


package org.neuromorpho.literature.agenda.service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.neuromorpho.literature.agenda.communication.Metadata;
import org.neuromorpho.literature.agenda.communication.Article;

public class EmailContent {

    private String type;
    private String tracingSystem;
    private String tracingSystemFormat;
    private String waitingDate;
    private String title;
    private String dears;
    private String journal;
    private String publication;
    private String publicationType;
    private String glia;
    private String glial;
    private String subjectType;
    private String publishedYear;
    private String authors;

    
    public EmailContent(Article article, List<Metadata> tracingSystemList, String type, String subject) {
        this.type = type;

        //title
        this.title = article.getTitle();

        //tracingSystem & tracingSystemFormat
        try {
            List<String> nameList = tracingSystemList.stream().map(t -> t.getName()).collect(Collectors.toList());
            List<String> tempList = tracingSystemList.stream().map(t -> t.getFormat()).collect(Collectors.toList());
            Set<String> formatList = new HashSet<>();
            for (String format : tempList) {
                String[] temp = format.split(", ");
                formatList.addAll(Arrays.asList(temp));
            }
            formatList.add(".swc");

            this.tracingSystem = this.createConnectorList(nameList, "or");
            this.tracingSystemFormat = this.createConnectorList(new ArrayList<>(formatList), "or");
        } catch (Exception ex) {
            this.tracingSystem = "***UNKNOWN TracingSystem***";
            this.tracingSystemFormat = "***UNKNOWN TracingSystemFormat***";
        }
        

        //WaitingDate
        LocalDate today = LocalDate.now();
        LocalDate nextDate = today.plusDays(14);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, uuuu");
        this.waitingDate = nextDate.format(formatter);

        //Dears
        List<String> nameList = article.getDearsList();
        if (nameList.size() == 0) {
            this.dears = "Dear Dr. ???";
        } else {
            this.dears = this.createConnectorList(nameList, "and");
            if (nameList.size() == 1) {
                this.dears = "Dear Dr. " + this.dears;
            } else {
                this.dears = "Dear Drs. " + this.dears;
            }
        }
        //Journal
        this.journal = article.getJournal();


        //Glia
        this.glia = "";
        this.glial = "";

        if (type.equals("glia")) {
            this.glia = "glia ";
            this.glial = "and glial";
        }
        //SubjectType
        this.subjectType = "neuronal";
        if (type.equals("glia")) {
            this.subjectType = "glial";
        }
        //PublishedYear
        this.publishedYear = article.getPublishedYear();


        this.publication = article.getPublication();
        this.publicationType = article.getPublicationType();
        this.authors = article.getAuthors();
    }


    public String getTracingSystem() {
        return tracingSystem;
    }

    public void setTracingSystem(String tracingSystem) {
        this.tracingSystem = tracingSystem;
    }

    public String getTracingSystemFormat() {
        return tracingSystemFormat;
    }

    public void setTracingSystemFormat(String tracingSystemFormat) {
        this.tracingSystemFormat = tracingSystemFormat;
    }

    public String getWaitingDate() {
        return waitingDate;
    }

    public void setWaitingDate(String waitingDate) {
        this.waitingDate = waitingDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDears() {
        return dears;
    }

    public void setDears(String dears) {
        this.dears = dears;
    }
    
    public String getJournal() {
        return " " + journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getPublication() {
        return publication;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public String getGlia() {
        return glia;
    }

    public void setGlia(String glia) {
        this.glia = glia;
    }

    public String getGlial() {
        return glial;
    }

    public void setGlial(String glial) {
        this.glial = glial;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getPublishedYear() {
        return " " + publishedYear;
    }

    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getAuthors() {
        return " " + authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublicationType() {
        return publicationType;
    }

    public void setPublicationType(String publicationType) {
        this.publicationType = publicationType;
    }

    private String createConnectorList(List<String> list, String connector) {
        String result = "";
        if (list.size() == 1) {
            result = list.get(0);
        } else if (list.size() == 2) {
            result = list.get(0) + " " + connector + " " + list.get(1);
        } else if (list.size() > 2) {
            for (Integer i = 0; i <= list.size() - 2; i++) {
                result = result + list.get(i) + ", ";
            }
            result = result + " " + connector + " " + list.get(list.size() - 1);
        }
        return result;
    }

}
