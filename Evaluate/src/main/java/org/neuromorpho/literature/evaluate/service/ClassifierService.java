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


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.evaluate.exception.FileNotFoundException;
import org.neuromorpho.literature.evaluate.communication.ClassifierCommunication;
import org.neuromorpho.literature.evaluate.communication.PDF.Text;
import org.neuromorpho.literature.evaluate.communication.article.Article;
import org.neuromorpho.literature.evaluate.communication.article.ArticleCommunication;
import org.neuromorpho.literature.evaluate.communication.article.ArticlePage;
import org.neuromorpho.literature.evaluate.communication.fulltext.ArticleContent;
import org.neuromorpho.literature.evaluate.communication.fulltext.FullTextCommunication;
import org.neuromorpho.literature.evaluate.model.Classifier;
import org.neuromorpho.literature.evaluate.repository.ClassifierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@Service
public class ClassifierService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ArticleCommunication articleCommunication;
    @Autowired
    private ClassifierCommunication classifierCommunication;
    @Autowired
    private FullTextCommunication fullTextCommunication;
    @Autowired
    private ClassifierRepository repository;
    @Value("${folderData}")
    private String FOLDER_DATA;
    @Value("${folderKeywords}")
    private String FOLDER_KEYWORDS;
    
    public List<Classifier> findAll() {
        return this.repository.findAll();
    }

    public void trainClassifier(String version) {
        Classifier classifier = new Classifier(version);
        classifier = this.repository.save(classifier);
        try {
            Integer nFile = this.dumpTextArticleList(version, "Positive", "data.fulltext=FULLTEXT&data.dataUsage=Describing&reconstructions=Available", 0);
            this.dumpTextArticleList(version, "Negative", "data.fulltext=FULLTEXT&data.dataUsage=Describing&data.evaluatedDate=gte:2017-01-01&metadata.negativeIfNoAnswer=false", nFile);
            this.repository.update(classifier.getId(), "status", Classifier.Status.EXTRACTING_RELEVANT_TEXT.toString());
            this.classifierCommunication.extractRelevant(version);
            this.repository.update(classifier.getId(), "status", Classifier.Status.TRAINING.toString());
            Classifier classifierTrained = classifierCommunication.train(version);
            classifier.updateData(classifierTrained);
            this.repository.replace(classifier.getId(), classifier);
        } catch (Exception ex) {
            this.repository.update(classifier.getId(), "status", Classifier.Status.ERROR.toString());
            log.error("Error training classifier for version: " + version, ex);
        }

    }

    public void update(String id, String field, Object object) {
        this.repository.update(new ObjectId(id), field, object);
    }


    public Integer dumpTextArticleList(String version, String status, String query, Integer iFileMax) {
        String folder = this.FOLDER_DATA + version;
        File dir = new File(folder);
        Integer iFile = 1;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = folder + "/" + status + "_" + iFile + ".json";
        File f = new File(fileName);
        if (!f.exists()) {

            ArticlePage articlePage = articleCommunication.getArticles(status, 0, query);
            Integer page = articlePage.getTotalPages() - 1;
            Integer iTextArticle = 0;
            ObjectMapper mapper = new ObjectMapper();
            List<List<String>> textList = new ArrayList<>();

            do {
                articlePage = articleCommunication.getArticles(status, page--, query);
                for (Article article : articlePage.getContent()) {
                    try {
                        log.debug("Downloading fulltext for article with id " + article.getId());
                        ArticleContent articleContent = fullTextCommunication.getFullText(article);

                        if (articleContent.hasText()) {
                            Text text = new Text(articleContent.getParagraphs());
                            textList.add(text.getText());
//                        pr.println(Arrays.deepToString(text.prepareParagraphs()));
                            iTextArticle++;
                            if (iTextArticle >= 500) {
                                iTextArticle = 0;
                                fileName = folder + "/" + status + "_" + iFile + ".json";
                                log.debug("Creating file: " + fileName);
                                mapper.writeValue(new File(fileName), textList);
                                textList = new ArrayList<>();
                                iFile++;
                            }
                        }
                    } catch (Exception ex) {
                        log.error("Error getting text for article: " + article.getId(), ex);
                    }
                }
            } while (page > 0 || iFileMax == 0 || iFileMax >= iFile);
            if (iTextArticle > 0) { //dump last file if < 500 samples
                fileName = folder + "/" + status + "_" + iFile + ".json";
                log.debug("Creating file: " + fileName);
                try {
                    mapper.writeValue(new File(fileName), textList);
                } catch (IOException ex) {
                    log.error("Error writing file: " + fileName, ex);
                }
                iFile++;
            }
        }
        return iFile;

    }

    public String readFile(String name) {
        try {
            return new String(Files.readAllBytes(Paths.get(this.FOLDER_KEYWORDS + "/" + name)));
        } catch (Exception e){
            log.error("Error reading file", e);
            throw new org.neuromorpho.literature.evaluate.exception.FileNotFoundException(name);
        }
    }
    
    public void updateFile(String name, String content) {
        BufferedWriter writer = null;
        try {
            File file = new File(this.FOLDER_KEYWORDS + "/" + name);

            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
        } catch (Exception e) {
            log.error("Error writing file", e);
            throw new FileNotFoundException(name);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }

    }

}
    

