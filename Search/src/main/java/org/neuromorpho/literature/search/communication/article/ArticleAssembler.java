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


package org.neuromorpho.literature.search.communication.article;


import org.neuromorpho.literature.search.crossref.model.CrossRefArticle;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.dto.article.Author;
import org.neuromorpho.literature.search.service.sciencedirect.model.search.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.*;

public class ArticleAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // ScienceDirect conversion
    public Article createArticle(Entry data) {
        log.debug("Converting article: " + data.toString());
        Article article = new Article();
        article.setTitle(data.getTitle().replaceAll("<[^>]+>", ""));
        article.setDoi(data.getDoi());
        article.setJournal(data.getPublicationName());
        article.setPublishedDate(this.tryParseDate(data.getCoverDate()));
        List<Author> authorList = new ArrayList();
        if (data.getAuthors() != null) {
            for (String authorStr : data.getAuthors().getAuthorList()) {
                Author author = new Author(authorStr, null);
                authorList.add(author);
            }
            article.setAuthorList(authorList);
        }
        article.setLink(data.getUrl());
        return article;

    }

    // CrossRef conversion
    public Article createArticle(CrossRefArticle data) {
        log.debug("Converting article: " + data.toString());
        Article article = new Article();
        article.setTitle(data.getTitle());
        article.setDoi(data.getDoi());
        article.setJournal(data.getJournal());
        article.setPublishedDate(this.tryParseDate(data.getPublishedDate()));
        List<Author> authorList = new ArrayList();
        if (data.getAuthorList() != null) {
            for (String authorStr : data.getAuthorList()) {
                Author author = new Author(authorStr, null);
                authorList.add(author);
            }
            article.setAuthorList(authorList);
        }
        article.setLink(data.getPdfLink());
        article.setLink(data.getHtmlLink());

        return article;

    }

    public LocalDate tryParseDate(String dateStr) {

        String[] formatStrings = {"dd MMM uuuu", 
                "dd MMMMM uuuu", 
                "MMMMM uuuu", 
                "uuuu", 
                "uuuu/MM/dd", 
                "uuuu-MM-dd", 
                "MMM uuuu", 
                "uuuu-MM", 
                "uuuu MMM dd",
                "uuuu MMM d",
                "uuuu MMM",
                "uuuu-MM-dd'T'HH:mm:ss'Z'"};
       
        for (String formatString : formatStrings) {
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .appendPattern(formatString)
                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                    .toFormatter();
            try {
                LocalDate localDate = LocalDate.parse(dateStr.trim(), formatter);
                return localDate;
            } catch (DateTimeParseException ex) {
            }
        }
        log.error("Error parsing date=" + dateStr);
        return null;
    }
}
