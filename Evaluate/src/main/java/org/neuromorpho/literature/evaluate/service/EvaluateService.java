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

package org.neuromorpho.literature.evaluate.service;


import org.neuromorpho.literature.evaluate.communication.ClassifierEvaluation;
import org.neuromorpho.literature.evaluate.communication.ClassifierCommunication;
import org.neuromorpho.literature.evaluate.communication.FileAcquisitionCommunication;
import org.neuromorpho.literature.evaluate.communication.NLP;
import org.neuromorpho.literature.evaluate.communication.PDF.PdfCommunication;
import org.neuromorpho.literature.evaluate.communication.PDF.Text;
import org.neuromorpho.literature.evaluate.communication.article.Article;
import org.neuromorpho.literature.evaluate.communication.article.ArticleCommunication;
import org.neuromorpho.literature.evaluate.communication.article.ArticlePage;
import org.neuromorpho.literature.evaluate.communication.contact.AgendaConnection;
import org.neuromorpho.literature.evaluate.communication.contact.Contact;
import org.neuromorpho.literature.evaluate.communication.fulltext.ArticleContent;
import org.neuromorpho.literature.evaluate.communication.fulltext.Author;
import org.neuromorpho.literature.evaluate.communication.fulltext.FullTextCommunication;
import org.neuromorpho.literature.evaluate.model.Classifier;
import org.neuromorpho.literature.evaluate.repository.ClassifierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class EvaluateService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArticleCommunication articleCommunication;
    @Autowired
    private FullTextCommunication fullTextCommunication;
    @Autowired
    private PdfCommunication pdfCommunication;
    @Autowired
    private FileAcquisitionCommunication fileAcquisitionCommunication;
    @Autowired
    private ClassifierCommunication classifierCommunication;
    @Autowired
    private AgendaConnection agendaConnection;
    @Autowired
    private ClassifierRepository repository;


    public void evaluateText() {
        List<String> statusList = articleCommunication.getStatusList();
        statusList.remove("All");
        for (String status : statusList) {
            this.evaluateText(status, "false");
        }
    }

    public void evaluateTextById(String id, Boolean replace) {
        Article article = articleCommunication.getArticle(id);
        Classifier classifier = repository.findCurrent();
        this.evaluateArticle(classifier, article, article.getStatus(), replace);
    }


    public void evaluateText(String status, String classified) {

        ArticlePage articlePage = articleCommunication.getArticles(status, 0, "identifiers=true&classifier=" + classified);

        Integer page = articlePage.getTotalPages() - 1;
        Classifier classifier = repository.findCurrent();

        do {

            articlePage = articleCommunication.getArticles(status, page--, "identifiers=true&classifier=" + classified);
            for (Article article : articlePage.getContent()) {
                this.evaluateArticle(classifier, article, status, Boolean.FALSE);
            }


        } while (page >= 0);
        log.debug("Finished evaluating articles");

    }

    private void evaluateArticle(Classifier classifier, Article article, String status, Boolean replace) {
        try {
            // Download text
            log.debug("Downloading fulltext for article with id " + article.getId());
            ArticleContent articleContent = fullTextCommunication.getFullText(article);

            if (!articleContent.hasText()) {
                String text = this.extractPDFContent(article);
                if (text != null && text.length() > 10) {
                    articleContent = new ArticleContent(text);
                    fullTextCommunication.saveRawText(article.getId(), articleContent);
                }
            }
            articleCommunication.updateArticle(article.getId(), "data.fulltext", articleContent.getContentType());
            if (articleContent.hasText()) {
                this.updateAccessible(status, article);
                if (articleContent.getAuthorList() != null &&
                        article.getAuthorList() != null &&
                        article.getAuthorList().stream().allMatch(author -> author.getContactId() == null) &&
                        articleContent.getAuthorList().stream().anyMatch(author -> author.getEmail() != null)) {
                    List<Map> authorList = this.updateContactList(articleContent.getAuthorList());
                    if (authorList.stream().anyMatch(author -> author.get("contactId") != null)) {
                        articleCommunication.updateArticle(article.getId(), "data.authorList", authorList);
                    }
                }
                // Classify text
                Text text = new Text(articleContent.getParagraphs());

                ClassifierEvaluation classifierEvaluation = classifierCommunication.classify(classifier.getVersion(), text);
                NLP nlp = classifierCommunication.nlp(text);
                classifierEvaluation.createClassifierData(nlp, classifier);
                articleCommunication.updateArticle(article.getId(), "classifier", classifierEvaluation);
                log.debug(classifierEvaluation.toString());
                if (status.equals("Pending evaluation") || status.equals("Inaccessible") || replace) {
                    if (classifierEvaluation.isNegativeHighConfidence()) {
                        Map<String, String> metadata = new HashMap<>();
                        metadata.put("comment", "Negative article with high confidence");
                        metadata.put("articleStatus", "Negative");
                        articleCommunication.updateArticle(article.getId(), "metadata", metadata);
                        articleCommunication.updateCollection(article.getId(), "Negative");
                    } if (classifierEvaluation.isPositiveHighConfidence()
                    && article.hasCompleteData()) {
                        Map<String, Object> metadata = classifierEvaluation.getMetadata();
                        metadata.put("comment", "Positive article with high confidence");
                        metadata.put("articleStatus", "Positive");
                        articleCommunication.updateArticle(article.getId(), "metadata", metadata);
                        articleCommunication.updateCollection(article.getId(), "Evaluated");
                    } else {
                        articleCommunication.updateArticle(article.getId(), "metadata", classifierEvaluation.getMetadata());
                        if (classifierEvaluation.containsNeuroMorpho()) {
                            log.debug("Article contains keyword neuromorpho");
                            articleCommunication.updateCollection(article.getId(), "Neuromorpho");
                        }
                    }
                    
                }

            } else { // No text
                log.debug("Inaccessible article");
                if (status.equals("Pending evaluation")) {
                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("comment", "No text/PDF found for the article (Automated process)");
                    if (article.getPdfLink() != null) {
                        metadata.put("comment", "PDF link not working correctly please check the link " +
                                "or upload the PDF instead (Automated process)");
                    }
                    metadata.put("articleStatus", "Inaccessible");
                    articleCommunication.updateArticle(article.getId(), "metadata", metadata);

                    ClassifierEvaluation classifierEvaluation = new ClassifierEvaluation();
                    classifierEvaluation.setArticleStatus("Inaccessible");
                    articleCommunication.updateArticle(article.getId(), "classifier", classifierEvaluation);
                    if (article.containsKeyWord("neuromorph")) {
                        log.debug("Article contains keyword neuromorpho");
                        articleCommunication.updateCollection(article.getId(), "Neuromorpho");
                    }
                }
            }
            log.debug("Seconds to sleep: " + 2);
            Thread.sleep(2 * 1000);
            log.debug("......................................");
        } catch (
                Exception ex) {
            log.error("Error evaluating article: " + article.getId(), ex);
        }

    }

    private String extractLink(Article article) {
        String link = null;

        try {
            String linkToExtract = null;
            if (article.getPmcid() != null) { //sleep 5 min
                log.debug("Seconds to sleep: " + 60);
                Thread.sleep(60 * 1000);
                link = "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC" + article.getPmcid() + "/pdf/main.pdf";
            } else if (article.getDoi() != null) {
                linkToExtract = "http://dx.doi.org/" + article.getDoi();
            }
            if (linkToExtract != null) {
                List<String> pdfLinkList = fileAcquisitionCommunication.extractFileLinks(linkToExtract);
                if (pdfLinkList.size() > 0) { //and .pdf
                    List<String> pdfPossibleList = pdfLinkList.stream().filter(l -> (l.contains(article.getDoi()) || l.contains("article")) && l.endsWith(".pdf")).collect(Collectors.toList());
                    log.debug("LinkList: " + pdfLinkList);
                    log.debug("Likely list: " + pdfPossibleList);
                    if (pdfPossibleList.size() > 0) {
                        link = pdfPossibleList.get(0);
                    }
                }
            }
        } catch (InterruptedException ex) {
        }
        return link;
    }

    private void updateAccessible(String status, Article article) {
        log.debug("Accessible article");
        if (status.equals("Inaccessible")) {
            articleCommunication.updateCollection(article.getId(), "Pending evaluation");
            Map<String, String> metadata = new HashMap<>();
            articleCommunication.updateArticle(article.getId(), "metadata", metadata);
        } else if (status.equals("Pending evaluation") && article.getMetadataStatus().equals("Inaccessible")) {
            Map<String, String> metadata = new HashMap<>();
            metadata.put("articleStatus", "Pending evaluation");
            articleCommunication.updateArticle(article.getId(), "metadata", metadata);
        }
    }

    private List<Map> updateContactList(List<Author> authorListDto) {
        List<Map> authorList = new ArrayList<>();
        for (Author authorDto : authorListDto) {
            Map author = new HashMap();
            if (authorDto.getEmail() != null) {
                // retrieve from agenda and update or save
                Contact contact = agendaConnection.findContact(authorDto.getEmail());
                if (contact == null) {
                    contact = new Contact(authorDto.getGivenName(), authorDto.getSurname(), authorDto.getEmail());
                    contact = agendaConnection.saveContact(contact);
                } else {
                    if (contact.getFirstName().length() < authorDto.getGivenName().length()) { // Update agenda with fullName
                        agendaConnection.updateContact(contact);
                    }
                }
                author.put("name", contact.getFirstName() + " " + contact.getLastName());
                author.put("contactId", contact.getId().toString());
            } else {
                author.put("name", authorDto.getGivenName() + " " + authorDto.getSurname());
            }
            authorList.add(author);
        }
        return authorList;
    }

    public String extractPDFContent(Article article) {
        String text = null;
        if (article.getPdfLink() != null) {
            text = pdfCommunication.extractPdfContent(article.getPdfLink());
        }
        if (text == null || text.length() < 10) {
            String link = null;
            if (article.getPortalList().stream().anyMatch(p -> p.equals("Wiley"))) {
                link = "https://onlinelibrary.wiley.com/doi/pdfdirect/" + article.getDoi();
            } else {
                link = this.extractLink(article);
            }
            if (link != null) {
                text = pdfCommunication.extractPdfContent(link);
            }
        }
        return text;
    }


}
    

