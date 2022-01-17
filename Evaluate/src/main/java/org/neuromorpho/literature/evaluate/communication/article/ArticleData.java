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

package org.neuromorpho.literature.evaluate.communication.article;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bson.codecs.pojo.annotations.BsonIgnore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleData {

    private String doi;
    private String pmid;
    private String pmcid;
    private LocalDate publishedDate;
    private String pdfLink;
    private List<Author> authorList;

    public ArticleData() {
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getPmcid() {
        return pmcid;
    }

    public void setPmcid(String pmcid) {
        this.pmcid = pmcid;
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean hasCompleteData() {
        List<Integer> emailIndexList = new ArrayList<>();
        for (Integer i = 0; i < this.authorList.size(); i++) {
            if (this.authorList.get(i).hasEmail()) {
                emailIndexList.add(i);
            }
        }
        if (emailIndexList.size() == 0) {
            return Boolean.FALSE;
        }
        // at least one email
        if (this.authorList.size() == 2) {
            if (emailIndexList.contains(0) && // first Author has email
                    !this.authorList.get(this.authorList.size() - 1).hasCompleteFirstName() ||
                    (emailIndexList.contains(this.authorList.size() - 1) &&
                            !this.authorList.get(0).hasCompleteFirstName())) {
                return Boolean.FALSE;
            }
        } else {
            if (!emailIndexList.contains(this.authorList.size() - 1) &&
                    !this.authorList.get(this.authorList.size() - 1).hasCompleteFirstName()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;

    }

}
