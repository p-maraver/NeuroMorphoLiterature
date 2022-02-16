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

package org.neuromorpho.literature.agenda.communication;

import org.bson.types.ObjectId;
import org.neuromorpho.literature.agenda.controller.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Article {

    private ObjectId id;
    private ArticleData data;

    private Metadata metadata;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Article() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ArticleData getData() {
        return data;
    }

    public void setData(ArticleData data) {
        this.data = data;
    }

    public org.neuromorpho.literature.agenda.controller.Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<String> getTracingSystemList() {
        return metadata.getTracingSystem();
    }

    public String getTitle() {
        return this.data.getTitle();
    }
    
    public String getPublishedYear() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu");
        return data.getPublishedDate().format(formatter);
    }

    public String getJournal() {
        String journal = "***UNKNOWN journal***";
        if (data.getJournal() != null) {
            journal = data.getJournal().toLowerCase();
            journal = journal.substring(0, 1).toUpperCase() + journal.substring(1);

            Integer spaceIndex = data.getJournal().indexOf(":");
            if (spaceIndex != -1) {
                journal = journal.substring(0, spaceIndex);
            }
            journal = journal.startsWith("The ") ? journal.substring(4) : journal;
            journal = journal.split("\\(")[0];
            journal = journal.trim();
        }
        return journal;
    }

    public String getAuthors() {
        String authors = "";
        switch (data.getAuthorList().size()) {
            case 1: // 1 author
                if (!data.firstAuthorHasEmail()) {
                    authors = "********NO CONTACT EMAIL************";
                } else {
                    authors = "your";
                }
                break;
            case 2: //2 authors
                if (!data.firstAuthorHasEmail()
                        && !data.lastAuthorHasEmail()) {
                    authors = "********NO CONTACT EMAIL************";
                } else {
                    authors = "your";
                }
                break;
            default: //more than 2 authors
                log.debug("More than 2 authors: " + data.getAuthorList().size());
                if (data.firstAuthorHasEmail()
                        && data.lastAuthorHasEmail()) {
                    authors = "your";
                    //We have identified your Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest

                } else if (data.firstAuthorHasEmail()
                        && data.middleAuthorHasEmail()) {
                    authors = "your";
                    //We have identified your Scientific data 2018 publication with Giorgio Ascoli An open repository for single-cell reconstructions of the brain forest
                } else if (data.middleAuthorHasEmail()
                        && data.lastAuthorHasEmail()) {
                    authors = "your " + data.getAuthorName(0, Boolean.FALSE, Boolean.FALSE) + " et al. ";
                    //We have identified your Akram et al. Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest
                } else if (data.firstAuthorHasEmail()) { //only first author with email
                    authors = "your";
                    //We have identified your Scientific data 2018 publication with Giorgio Ascoli An open repository for single-cell reconstructions of the brain forest
                } else if (data.middleAuthorHasEmail()) { //only middle author with email
                    authors = "your " + data.getAuthorName(0, Boolean.FALSE, Boolean.FALSE) + " et al. ";
                    //We have identified your Akram et al. Scientific data 2018 publication with Giorgio Ascoli An open repository for single-cell reconstructions of the brain forest
                } else if (data.lastAuthorHasEmail()) { //only last author with email
                    log.debug("Last author has email: " + data.getAuthorName(0, Boolean.FALSE, Boolean.FALSE));
                    authors = "your " + data.getAuthorName(0, Boolean.FALSE, Boolean.FALSE) + " et al.";
                    //We have identified your Akram et al. Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest

                } else { // no email
                    authors = "********NO CONTACT EMAIL************";
                }
                break;
        }
        return authors;
        //only initials when we say with

    }

    public String getPublicationType() {
        String result;
        if (data.isPrePrint()) {
            result = "preprint";
        } else if (data.isDissertation()){
            if (!data.firstAuthorHasEmail()) {
                result = "student's dissertation";
            } else {
                result = "dissertation";
            }
        } else {
            result = "publication";
        }
        return result;
    }

    public String getPublication() {
        String publication;
        if (data.isPrePrint()) {
            publication = "preprint";
        } else if (data.isDissertation()){
            if (!data.firstAuthorHasEmail()) {
                publication =  "your student " + data.getAuthorName(0, Boolean.FALSE, Boolean.TRUE) + "'s";
            } else {
                publication = "your";
            }
            return publication;
        } else {
            publication = " publication";
        }
        switch (data.getAuthorList().size()) {
            case 1: // 1 author
                //if (data.firstAuthorHasEmail()) {
                //We have identified your Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest
                break;
            case 2: //2 authors
                //if (data.firstAuthorHasEmail() && data.lastAuthorHasEmail()) {
                //We have identified your Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest
                if (data.firstAuthorHasEmail() && !data.lastAuthorHasEmail()) {
                    publication = publication + " with " + data.getAuthorName(data.getAuthorList().size() - 1, Boolean.FALSE, Boolean.TRUE);
                    //We have identified your Scientific data 2018 publication with Giorgio Ascoli An open repository for single-cell reconstructions of the brain forest
                } else if (data.lastAuthorHasEmail() && !data.firstAuthorHasEmail()) {
                    publication = publication + " with " + data.getAuthorName(0, Boolean.FALSE, Boolean.TRUE);
                    //We have identified your Scientific data 2018 publication with Masood Akram An open repository for single-cell reconstructions of the brain forest
                }
                break;
            default: //more than 2 authors
                //if (data.firstAuthorHasEmail() && data.lastAuthorHasEmail()) {
                //We have identified your Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest
                //if (data.middleAuthorHasEmail() && data.lastAuthorHasEmail()) {
                //We have identified your Akram et al. Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest
                // if (data.lastAuthorHasEmail()) { //only last author with email
                //We have identified your Akram et al. Scientific data 2018 publication An open repository for single-cell reconstructions of the brain forest
                if (data.firstAuthorHasEmail() && data.middleAuthorHasEmail() && !data.lastAuthorHasEmail()) {
                    publication = publication + " with " + data.getAuthorName(data.getAuthorList().size() - 1, Boolean.FALSE, Boolean.TRUE);
                    //We have identified your Scientific data 2018 publication with Giorgio Ascoli An open repository for single-cell reconstructions of the brain forest
                } else if (data.firstAuthorHasEmail() && !data.middleAuthorHasEmail() && !data.lastAuthorHasEmail()) { //only last author with email
                    publication = publication + " with " + data.getAuthorName(data.getAuthorList().size() - 1, Boolean.FALSE, Boolean.TRUE);
                    //We have identified your Scientific data 2018 publication with Giorgio Ascoli An open repository for single-cell reconstructions of the brain forest
                } else if (!data.firstAuthorHasEmail() && data.middleAuthorHasEmail() && !data.lastAuthorHasEmail()) { //only last author with email
                    publication = publication + " with " + data.getAuthorName(data.getAuthorList().size() - 1, Boolean.FALSE, Boolean.TRUE);
                    //We have identified your Akram et al. Scientific data 2018 publication with Giorgio Ascoli An open repository for single-cell reconstructions of the brain forest
                }
                break;
        }
        return publication;
        //only initials when we say with

    }
    public List<ObjectId> getContactListId() {
        return this.data.getContactListId();
    }
    

    public List<String> getDearsList() {
        return this.data.getDearsList();
    }

    public Boolean isDissertation(){
        return this.data.isDissertation();
    }
    
    public void removeBouncedAuthor(ObjectId contactId){
        this.data.removeBouncedAuthor(contactId);
    }
}
