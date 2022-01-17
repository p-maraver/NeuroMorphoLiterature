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

package org.neuromorpho.literature.search.service;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.neuromorpho.literature.search.communication.agenda.AgendaConnection;
import org.neuromorpho.literature.search.communication.agenda.Contact;
import org.neuromorpho.literature.search.crossref.service.CrossRefService;
import org.neuromorpho.literature.search.dto.article.Article;
import org.neuromorpho.literature.search.dto.article.Author;
import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.exception.FullTextNotAvailableException;
import org.neuromorpho.literature.search.exception.MissingDataException;
import org.neuromorpho.literature.search.pubmed.model.Identifiers;
import org.neuromorpho.literature.search.pubmed.service.PubMedService;
import org.neuromorpho.literature.search.communication.article.ArticleResponse;
import org.neuromorpho.literature.search.communication.article.LiteratureConnection;
import org.neuromorpho.literature.search.model.KeyWord;
import org.neuromorpho.literature.search.model.Portal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public abstract class PortalSearch implements IPortalSearch {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected Document searchDoc;
    protected Portal portal;
    protected String keyWord;
    protected String collection;
    protected Article article;
    protected Integer seconds = 5;

    @Autowired
    protected LiteratureConnection literatureConnection;
    @Autowired
    protected AgendaConnection agendaConnection;
    @Autowired
    protected PubMedService pubMedService;
    @Autowired
    protected CrossRefService crossRefService;

    @Override
    public void findArticleList(KeyWord keyWord, Portal portal) throws InterruptedException {
        try {
            this.portal = portal;
            this.keyWord = keyWord.getName();
            this.collection = keyWord.getCollection();

            log.debug("Executing portal " + portal.getName() + " for keyword " + keyWord.getName());
            if (this.portal.hasSearchAPI()) {
                log.debug("Seconds to sleep: " + seconds);
                log.debug("......................................");
                Thread.sleep(seconds * 1000);
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                this.searchForTitlesApi();
            } else {
                this.searchPage();
                this.searchForTitles();
            }
        } catch (HttpStatusException ex) { // if jsoup returns this exception, the page was empty
            log.error("Error", ex);
            //throw ex;

        } catch (IOException ex) { // if jsour returns this exception, the page was empty
            log.debug("Articles found 0 ");
            log.error("Error", ex);
        }

    }

    @Override
    public abstract ArticleContentDto getArticleContent(String doi, Portal portal);

    //to be override by the sons
    protected abstract Elements findArticleList();

    protected abstract void searchPage() throws InterruptedException, IOException;

    protected abstract Boolean loadNextPage() throws InterruptedException, IOException;

    protected abstract String fillTitle(Element articleData);

    protected abstract void fillPublishedDate(Element articleData, Element articlePage);

    protected abstract void fillJournal(Element articleData, Element articlePage);

    protected abstract void fillAuthorList(Element articleData, Element articlePage);

    protected abstract void fillDoi(Element articleData, Element articlePage);

    protected abstract void fillLinks(Element articleData, Element articlePage);


    protected void searchForTitles() throws InterruptedException, IOException {

        Elements articleList = this.findArticleList();
        for (Element articleElement : articleList) {
            this.createArticle(articleElement);
        }
        Boolean existsNextPage = this.loadNextPage();
        if (existsNextPage) {
            searchForTitles();
        }

    }

    protected void createArticle(Element articleData) throws InterruptedException {
        this.article = new Article();
        Integer i = 0;
        Boolean read = Boolean.FALSE;
        try {
            while (i < 10 && !read) {
                String articleLink = this.fillTitle(articleData);
                if (!this.article.getTitle().isEmpty()
                        && !containsHanScript(this.article.getTitle())) {
                    log.debug("Reading article: " + articleLink);
                    Element articlePage = null;
                    log.debug("Article title: " + this.article.getTitle());
                    log.debug("Article link: " + articleLink);
                    /*if (articleLink != null && !this.portal.getName().equals("GoogleScholar")) {
                        articleDoc = Jsoup.connect(articleLink)
                                .timeout(30 * 1000)
                                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36").get();
                        articlePage = articleDoc.select("html").first();
                    }*/

                    this.fillJournal(articleData, articlePage);
                    this.fillAuthorList(articleData, articlePage);
                    this.fillPublishedDate(articleData, articlePage);
                    this.fillDoi(articleData, articlePage);
                    this.fillLinks(articleData, articlePage);
                    if (this.article.getDoi() != null) {
                        String pdfLink = this.crossRefService.retrievePdfLink(this.article.getDoi(), article.getPmcid());
                        //check if it is a pdf
                        this.article.setPdfLink(pdfLink);
                    }
                    this.saveArticle();
                }
                read = Boolean.TRUE;
            }
        } catch (MissingDataException | NullPointerException ex) {
            log.debug(ex.getMessage());
        } catch (FullTextNotAvailableException ex) {
            log.debug(ex.getMessage());
        }
    }

    /*
     * Contains Asian characters
     */
    private static Boolean containsHanScript(String s) {
        for (int i = 0; i < s.length(); ) {
            int codepoint = s.codePointAt(i);
            i += Character.charCount(codepoint);
            if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    protected abstract void searchForTitlesApi() throws InterruptedException;

    protected void saveArticle() throws InterruptedException {
        if (article.getTitle() != null &&
                article.getAuthorList() != null &&
                !article.getAuthorList().isEmpty()) {
            if (!this.portal.getName().equals("PubMed") && !this.portal.getName().equals("PubMedCentral")) {
                Identifiers identifiers = pubMedService.retrieveIdentifiersFromTitleDB(this.article.getTitle(), null, Article.DB.PUBMED);
                if (identifiers != null) {
                    this.article.setPmid(identifiers.getPmid());
                    this.article.setPmcid(identifiers.getPmcid());
                }
            }
            if (this.article.getPmid() != null) {
                this.article = pubMedService.retrievePubMedArticleData(this.article.getPmid(), Article.DB.PUBMED);
            }
            log.debug(this.article.toString());
            this.updateContactList();
            ArticleResponse response = literatureConnection.saveArticle(this.article, this.collection);
            if (response.getId() != null) {
                literatureConnection.saveSearchPortal(response.getId(), this.portal.getName(), this.keyWord);
            }

            log.debug("Seconds to sleep: " + seconds);
            log.debug("......................................");
            Thread.sleep(seconds * 1000);
            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }
        }
    }
    
    private void updateContactList(){
        for (Author author: article.getAuthorList()){
            if (author.getEmail() != null){
                // retrieve from agenda and update or save
                Contact contact = agendaConnection.findContact(author.getEmail()); 
                if (contact == null){
                    contact = new Contact(author.getFirstName(), author.getLastName(), author.getEmail());
                    contact = agendaConnection.saveContact(contact);
                } else {
                    if (contact.getFirstName().length() > author.getFirstName().length()){
                        author.setName(contact.getFirstName() + " " + contact.getLastName());
                    } else if (contact.getFirstName().length() < author.getFirstName().length()){
                        contact.setFirstName(author.getFirstName());
                        log.debug("Updating contact: " + contact.toString());
                        agendaConnection.updateContact(contact);
                    }
                }
                author.setContactId(contact.getId());
            }
            
        }
    }


}
