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
import org.neuromorpho.literature.agenda.controller.Author;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArticleData {

    private String title;
    private String journal;
    private LocalDate publishedDate;
    private List<Author> authorList;

    public ArticleData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }


    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    public Boolean firstAuthorHasEmail() {
        return this.authorList.get(0).getContactId() != null;

    }

    public Boolean lastAuthorHasEmail() {
        return this.authorList.get(this.authorList.size() - 1).getContactId() != null;

    }

    public Boolean middleAuthorHasEmail() {
        Boolean hasEmail = Boolean.FALSE;
        for (Integer i = 1; i < this.authorList.size() - 1; i++) { //nor first nor last
            if (this.authorList.get(i).getContactId() != null) {
                hasEmail = Boolean.TRUE;
            }
        }
        return hasEmail;

    }

    public String getAuthorName(Integer position, Boolean initials, Boolean firstName) {
        String[] authorArr = this.authorList.get(position).getName().split(" ");
        String name = "";
        if (authorArr.length == 2) {
            name = name + authorArr[0].charAt(0) + ". ";
        } else if (authorArr.length > 2) {
            if (authorArr[0].length() > 1 && !authorArr[0].contains(".")) {
                name = name + authorArr[0] + " ";
            } else {
                name = name + authorArr[0].charAt(0) + authorArr[1].charAt(0) + " ";
            }
        }
        String author = authorArr[authorArr.length - 1];
        if (firstName) {
            author = authorArr[0] + " " + author;
        }
        if (initials) {
            author = name + author;
        }
        return author;
    }
    
    
    public List<String> getDearsList(){
        List<String> nameList = new ArrayList<>();
        for (Author author : this.authorList) {
            if (author.getContactId() != null) {
                author.setName(author.getName().trim());
                Integer pos = author.getName().lastIndexOf(" ") + 1;
                String name;
                if (pos != -1) {
                    name = author.getName().substring(pos, author.getName().length());
                } else {
                    pos = author.getName().lastIndexOf("\u00A0");
                    name = author.getName().substring(pos, author.getName().length());
                }
                nameList.add(name);
            }
        }
        return nameList;
    }

    public List<ObjectId> getContactListId() {
        return this.authorList.stream()
                .filter(author -> Objects.nonNull(author.getContactId()))
                .map(author -> author.getContactId())
                .collect(Collectors.toList());
    }

    public Boolean isPrePrint() {
        return this.journal.toLowerCase().equals("biorxiv") ||
                this.journal.toLowerCase().equals("arxiv") ||
                this.journal.toLowerCase().equals("research square");
    }

    public Boolean isDissertation() {
        return this.journal.toLowerCase().equals("dissertation");
    }


}
