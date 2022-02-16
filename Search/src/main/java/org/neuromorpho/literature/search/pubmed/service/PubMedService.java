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

package org.neuromorpho.literature.search.pubmed.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.neuromorpho.literature.search.communication.article.ArticleAssembler;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.dto.article.Author;
import org.neuromorpho.literature.search.exception.PubMedException;
import org.neuromorpho.literature.search.pubmed.model.Identifiers;
import org.neuromorpho.literature.search.pubmed.model.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PubMedService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // update to read from DB
    private String uri = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils";
    private String uri2 = "https://www.ncbi.nlm.nih.gov/pmc/utils";
    private String apiKey = "ddf998847f334d4b89bdff0cfd26117f1808";
    RestTemplate restTemplate = new RestTemplate();
    private ArticleAssembler assembler = new ArticleAssembler();

    
    public Article retrievePubMedArticleData(String pmid, Article.DB db) {

        String url = uri
                + "/esummary.fcgi?"
                + "db=" + db.getDB()
                + "&api_key=" + apiKey
                + "&retmode=json"
                + "&id=" + pmid;
        log.debug("Accesing pubmed using url: " + url);
        Map<String, Object> articleMap = restTemplate.getForObject(url, Map.class);
        Article article = new Article();
        Map result = (HashMap) articleMap.get("result");
        if (result == null) {
            List<String> error = (List) articleMap.get("esummaryresult");
            throw new PubMedException(error.get(0));
        }
        ArrayList<String> uids = (ArrayList) result.get("uids");
        if (uids.isEmpty()) {
            throw new PubMedException("Unknown pmid not found in " + db + " id: " + pmid);
        }
        Map articleValues = (HashMap) result.get(uids.get(0));
        String title = (String) articleValues.get("title");
        article.setTitle(this.getCorrectedName(title));
        Identifiers record = this.retrieveIdentifiers(pmid, db);

        if (db.equals(Article.DB.PUBMED)) {
            article.setPmid(pmid);
            article.setPmcid(record.getPmcid());
        } else {
            article.setPmcid(pmid);
            article.setPmid(record.getPmid());
        }
        
        article.setJournal(this.getCorrectedName((String) articleValues.get("fulljournalname")));
        ArrayList<Map> articleIds = (ArrayList) articleValues.get("articleids");
        for (Map articleId : articleIds) {
            if (articleId.get("idtype").equals("doi")) {
                article.setDoi((String) articleId.get("value"));
                break;
            }
        }
        String sortDateStr = (String) articleValues.get("epubdate");
        if (sortDateStr.isEmpty()){
            sortDateStr = (String) articleValues.get("pubdate");
        }
        LocalDate publishedDate = assembler.tryParseDate(sortDateStr);
        if (publishedDate == null){
            sortDateStr = ((String) articleValues.get("sortpubdate")).split(" ")[0];
        }
        publishedDate = assembler.tryParseDate(sortDateStr);
        article.setPublishedDate(publishedDate);

        List<Author> authorList = new ArrayList();

        if (db.equals(Article.DB.PUBMED)) {
            url = uri
                    + "/efetch.fcgi?"
                    + "db=" + db.getDB()
                    + "&api_key=" + apiKey
                    + "&id=" + pmid
                    + "&retmode=xml";
            log.debug("Accesing pubmed authors information & affiliation using url: " + url);
            
            String xml = restTemplate.getForObject(url, String.class);
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            for (Element a : doc.select("Author")) {
                Element e = a.select("Affiliation").first();
                String email = null;
                if (e != null && e.text().contains("@")) {
                    email = e.text().substring(e.text().lastIndexOf(" ") + 1, e.text().length());
                    if (email.endsWith(".")) {
                        email = email.substring(0, email.length() - 1);
                        log.debug("Email fo author: " + email);
                    }

                }
                Element fn = a.select("ForeName").first();
                Element ln = a.select("LastName").first();
                if (fn != null && ln != null) {
                  /*  if (email != null && !email.contains(ln.text())) {
                        email = null;
                    }*/
                    Author author = new Author(fn.text() + " " + ln.text(), email);
                    authorList.add(author);
                }
            }
        } else {
            ArrayList<Map> authors = (ArrayList) articleValues.get("authors");
            for (Map authorStr : authors) {
                if (authorStr.get("authtype").equals("Author")) {
                    String name = (String) authorStr.get("name");
                    String finalName = name;
                    if (name.contains(" ")) {
                        String[] nameArray = name.split(" ");
                        finalName = nameArray[1] + " " + nameArray[0];
                    }
                    Author author = new Author(finalName, null);

                    authorList.add(author);
                }
            }

        }
        article.setAuthorList(authorList);
        return article;
    }

    public Identifiers retrieveIdentifiers(String id, Article.DB db) {

        if (db.equals(Article.DB.PUBMEDCENTRAL)) {
            id = "PMC" + id;
        }
        String url = uri2 + "/idconv/v1.0/?"
                + "ids=" + id
                + "&api_key=" + apiKey
                + "&format=json";
        log.debug("Accessing " + url);

        Record record = restTemplate.getForObject(
                url,
                Record.class);
        return record.getIdentifiersList().get(0);

    }


    public Identifiers retrieveIdentifiersFromTitleDB(String title, String id, Article.DB db) {
        if (id == null && title != null){
            String url = uri + "/esearch.fcgi?"
                    + "db=" + db.getDB()
                    + "&api_key=" + apiKey
                    + "&retmode=json"
                    + "&term=" + "\"" + title + "\""
                    + "&field=title" + "\"";
            log.debug("Accessing " + url);
            Map<String, Object> pmidMap = restTemplate.getForObject(
                    url,
                    Map.class);
            Map result = (HashMap) pmidMap.get("esearchresult");
            ArrayList<String> uidList = (ArrayList) result.get("idlist");

            if (uidList.size() == 1){
                id = uidList.get(0);
            }
        }
        if (id != null) {
            return this.retrieveIdentifiers(id, db);
        }
        return null;
    }

    private String getTitle(String xml) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        InputSource source = new InputSource(new StringReader(xml));
        return xpath.evaluate("//Title", source);
    }

    private String getCorrectedName(String name) {
        if (name != null && name.endsWith(".")) {
            name = name.substring(0, name.length() - 1);
        }
        return name.replaceAll("&amp;", "&").replaceAll("<[^>]+>", "");
    }

//    protected LocalDate tryParseDate(String dateStr) {
//
//        String[] formatStrings = {"yyyy MMM d", "yyyy MMM dd", "yyyy MMM", "yyyy"};
//        for (String formatString : formatStrings) {
//            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//                    .appendPattern(formatString)
//                    .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
//                    .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
//                    .toFormatter(Locale.ENGLISH);
//            try {
//                LocalDate localDate = LocalDate.parse(dateStr.trim(), formatter);
//                return localDate;
//            } catch (DateTimeParseException ex) {
//                log.error("Error parsing date: ", ex);
//            }
//        }
//        return null;
//    }
    
}
