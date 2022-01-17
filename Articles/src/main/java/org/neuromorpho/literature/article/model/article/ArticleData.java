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

package org.neuromorpho.literature.article.model.article;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.apache.lucene.search.spell.JaroWinklerDistance;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class ArticleData {
    @BsonIgnore
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String pmid;
    private String pmcid;
    private String doi;
    private String link;
    private String pdfLink;
    private String journal;
    private String title;
    private List<Author> authorList;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate publishedDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate ocDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate evaluatedDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate approvedDate;
    private List<DataUsage> dataUsage;
    private String fulltext;
    
    @JsonIgnore
    private Boolean notDoi;

    @BsonIgnore
    @JsonIgnore
    public Boolean isDescribingNeurons() {
        return this.dataUsage.contains(DataUsage.DESCRIBING_NEURONS);
    }

    public ArticleData() {
    }

    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        if (isValidValue(pmid)) {
            this.pmid = pmid.trim();
        }
    }

    public String getPmcid() {
        return pmcid;
    }

    public void setPmcid(String pmcid) {
        if (isValidValue(pmcid)) {
            this.pmcid = pmcid.trim();
            ;
        }
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        if (isValidValue(doi)) {
            this.doi = doi.trim();
            ;
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        if (isValidValue(link)) {
            this.link = link.trim();
            ;
        }
    }

    public String getJournal() {
        return journal;

    }

    public void setJournal(String journal) {
        if (isValidValue(journal)) {
            this.journal = journal.trim();
            ;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (isValidValue(title)) {
            this.title = title.trim();
            ;
        }
    }

    public LocalDate getOcDate() {
        return ocDate;
    }

    public void setOcDate(LocalDate ocDate) {
        if (isValidValue(ocDate)) {
            this.ocDate = ocDate;
        }
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        if (isValidValue(publishedDate)) {
            this.publishedDate = publishedDate;
        }
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public void setAuthorList(List<Author> authorList) {
        if (!authorList.isEmpty()) {
            this.authorList = authorList;
        }
    }

    public LocalDate getEvaluatedDate() {
        return evaluatedDate;
    }

    public void setEvaluatedDate(LocalDate evaluatedDate) {
        if (isValidValue(evaluatedDate)) {
            this.evaluatedDate = evaluatedDate;
        }
    }

    public String getPdfLink() {
        return pdfLink;
    }

    public void setPdfLink(String pdfLink) {
        this.pdfLink = pdfLink;
    }

    public LocalDate getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDate approvedDate) {
        if (isValidValue(approvedDate)) {
            this.approvedDate = approvedDate;
        }
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public List<DataUsage> getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(List<DataUsage> dataUsage) {
        this.dataUsage = dataUsage;
    }

    public Boolean isValidValue(Object value) {
        return value != null && !value.equals("");
    }

    public Boolean getNotDoi() {
        return notDoi;
    }

    public void setNotDoi(Boolean notDoi) {
        this.notDoi = notDoi;
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean hasDoi() {
        return this.doi != null;
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean hasPmid() {
        return this.pmid != null;
    }

    @BsonIgnore
    @JsonIgnore
    protected Boolean hasPmcid() {
        return this.pmcid != null;
    }

    @BsonIgnore
    @JsonIgnore
    protected Boolean hasPublishedDate() {
        return this.publishedDate != null;
    }

    @BsonIgnore
    @JsonIgnore
    protected Boolean hasJournal() {
        return this.journal != null && !this.journal.endsWith("…");
    }

    @BsonIgnore
    @JsonIgnore
    protected Boolean isRxivJournal(String journal) {
        return journal != null && 
                (journal.toLowerCase().equals("biorxiv") || 
                        journal.toLowerCase().equals("arxiv") ||
                        journal.toLowerCase().equals("research square"));
    }

    @BsonIgnore
    @JsonIgnore
    protected Boolean hasTitle() {
        return !this.title.endsWith("…");
    }

    @BsonIgnore
    @JsonIgnore
    protected Long countEmailList() {
        Long count = this.authorList.stream().filter(author -> author.getContactId() != null).count();
        log.debug("Emails count: " + count);
        return count;
    }

    @BsonIgnore
    @JsonIgnore
    protected Integer countAuthorList() {
        Integer count = this.authorList.size();
        return count;
    }

    @BsonIgnore
    @JsonIgnore
    protected Boolean similarJournal(String journal) {
        return this.journal.equals(journal) || this.journal.endsWith("…") || journal.endsWith("…") 
                || this.isRxivJournal(this.journal) || this.isRxivJournal(journal);
    }

    @BsonIgnore
    @JsonIgnore
    private Float distanceString(String string1, String string2) {
        JaroWinklerDistance jwDistance = new JaroWinklerDistance();
        Float distance = jwDistance.getDistance(string1.toLowerCase(), string2.toLowerCase());
        log.debug("String1=" + string1);
        log.debug("String2=" + string2);
        log.debug("Jaro distance=" + distance);
        return distance;
    }

    @BsonIgnore
    @JsonIgnore
    private Boolean containsString(String string1, String string2) {
        String result1 = string1.replace("…", "").toLowerCase();
        String result2 = string2.replace("…", "").toLowerCase();
        return result1.contains(result2) || result1.contains(result2) ||
                result1.replaceAll("\\s+","").contains(result2.replaceAll("\\s+",""));
    }

    @BsonIgnore
    @JsonIgnore
    private Boolean similarDate(LocalDate date1, LocalDate date2, Integer days) {
        try {
            long diff = Math.abs(DAYS.between(date1, date2));
            log.debug("Period=" + diff);

            log.debug("Date1=" + date1.toString());
            log.debug("Date2=" + date2.toString());
            log.debug("Same=" + (date1.getYear() == date2.getYear() || diff < days));
            return date1.getYear() == date2.getYear() || diff < days;
        } catch (NullPointerException ex) {
            return Boolean.FALSE;
        }
    }

    @BsonIgnore
    @JsonIgnore
    private Boolean sameValue(String value1, String value2) {
        Boolean result = Boolean.FALSE;
        if (this.isValidValue(value1) && this.isValidValue(value2) && value1.equals(value2)) {
            result = Boolean.TRUE;
        }
        return result;
    }

    @BsonIgnore
    @JsonIgnore
    private Boolean differentValue(String value1, String value2) {
        Boolean result = Boolean.FALSE;
        if (this.isValidValue(value1) && this.isValidValue(value2) && !value1.equals(value2)) {
            result = Boolean.TRUE;
        }
        return result;
    }

    @BsonIgnore
    @JsonIgnore
    private Boolean similarAuthorList(List<Author> authorList1, List<Author> authorList2) {
        Boolean result = Boolean.TRUE;
        List<Author> largerList;
        List<Author> smallerList;
        if (authorList1.size() >= authorList2.size()) {
            largerList = authorList1;
            smallerList = authorList2;
        } else {
            largerList = authorList2;
            smallerList = authorList1;
        }
        for (Integer j = 0; j < smallerList.size(); j++) {
            Integer i = 0;
            Boolean found = Boolean.FALSE;

            while (!found && i < largerList.size()) {
                found = smallerList.get(j).sameLastName(largerList.get(i));
                i++;
            }
            if (!found) {
                return result;
            }
        }


        log.debug("AuthorList1=" + authorList1.toString());
        log.debug("AuthorList2=" + authorList2.toString());
        log.debug("Same=" + result);

        return result;
    }

    @BsonIgnore
    @JsonIgnore
    public Integer getTitleCountTokens() {
        StopWords stopWords = new StopWords();
        List<String> titleNoStopWords = stopWords.removeStopWords(this.title);
        return titleNoStopWords.size();
    }

    @BsonIgnore
    @JsonIgnore
    public boolean similarData(ArticleData data, Double score) {
        Integer wordCount = this.getTitleCountTokens();
        Double maxScore = (wordCount * 0.5) + 0.5;
        Double match = score / maxScore;
        log.debug("Title1=" + this.title);
        log.debug("Title2=" + data.title);
        log.debug("Mongo {match=" + match + ", distance=" + score + ", max score=" + maxScore + "}");
        Float distance = this.distanceString(this.title, data.title);
        if (this.sameValue(pmid, data.pmid) ||
                this.sameValue(pmcid, data.pmcid) ||
                this.sameValue(doi, data.doi)){
            return Boolean.TRUE;
        }
        if (this.differentValue(pmid, data.pmid) ||
                this.differentValue(pmcid, data.pmcid)){
            return Boolean.FALSE;
        }
        if ((match > 0.99
                        && !this.isValidValue(pmid)
                        && !this.isValidValue(pmcid)
                        && !this.isValidValue(doi))

                        ||
                        (match > 0.99
                                && !this.isValidValue(data.pmid)
                                && !this.isValidValue(data.pmcid)
                                && !this.isValidValue(data.doi))


                        ||
                        (distance > 0.99
                                && !this.isValidValue(pmid)
                                && !this.isValidValue(pmcid)
                                && !this.isValidValue(doi))

                        ||
                        (distance > 0.99
                                && !this.isValidValue(data.pmid)
                                && !this.isValidValue(data.pmcid)
                                && !this.isValidValue(data.doi))

                        || (match > 0.87 //Same article
                        && this.similarDate(this.publishedDate, data.publishedDate, 730)
                        && this.similarAuthorList(this.authorList, data.authorList)
                        && this.similarJournal(data.journal))


                        || ((distance > 0.85 || match > 0.85) //Incomplete article subset
                        && this.similarDate(this.publishedDate, data.publishedDate, 390)
                        && (this.containsString(this.title, data.title) || distance > 0.95)
                        && this.similarAuthorList(this.authorList, data.authorList))) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @BsonIgnore
    @JsonIgnore
    public Boolean isEmptyEvaluatedDate() {
        return this.evaluatedDate == null;
    }

    @Override
    public String toString() {
        return "ArticleData{" +
                "pmid='" + pmid + '\'' +
                ", pmcid='" + pmcid + '\'' +
                ", doi='" + doi + '\'' +
                ", link='" + link + '\'' +
                ", journal='" + journal + '\'' +
                ", title='" + title + '\'' +
                ", authorList=" + authorList +
                ", publishedDate=" + publishedDate +
                ", ocDate=" + ocDate +
                ", evaluatedDate=" + evaluatedDate +
                ", approvedDate=" + approvedDate +
                ", dataUsage=" + dataUsage +
                '}';
    }

    @BsonIgnore
    @JsonIgnore
    public void createDataUsage(DataUsage usage) {
        if (this.dataUsage == null) {
            this.dataUsage = new ArrayList<>();
        }
        this.dataUsage.add(usage);
    }


    public enum DataUsage {
        USING("Using"),
        DESCRIBING_NEURONS("Describing"),
        CITING("Citing"),
        ABOUT("About"),
        SHARING("Sharing"),
        DUPLICATE("Duplicate");


        private final String usage;

        private DataUsage(String usage) {
            this.usage = usage;
        }

        public static DataUsage getUsage(String value) {
            for (DataUsage v : values()) {
                if (v.getUsage().equalsIgnoreCase(value)) {
                    return v;
                }
            }
            throw new IllegalArgumentException(value);
        }

        public String getUsage() {
            return usage;
        }

        public Boolean isDescribingNeurons() {
            return this.equals(DataUsage.DESCRIBING_NEURONS);
        }
    }
    
    
    @BsonIgnore
    public Boolean hasText(){
        return this.fulltext!= null &&
                (this.fulltext.equals("ABSTRACT")||
                        this.fulltext.equals("FULLTEXT"));
    }
}
