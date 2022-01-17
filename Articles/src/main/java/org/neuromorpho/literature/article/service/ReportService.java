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

package org.neuromorpho.literature.article.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.neuromorpho.literature.article.connection.ArticleContent;
import org.neuromorpho.literature.article.connection.FullTextCommunication;
import org.neuromorpho.literature.article.model.article.Article;
import org.neuromorpho.literature.article.model.article.ArticleData;
import org.neuromorpho.literature.article.model.article.Reconstructions;
import org.neuromorpho.literature.article.model.article.ReconstructionsStatus;
import org.neuromorpho.literature.article.repository.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${folder}")
    private String folder;

    @Autowired
    private FullTextCommunication fullTextCommunication;

    @Autowired
    private ArticleRepository repository;


    public void generateArticlesCsv(String status, Boolean text) {

        String file = folder + status + ".csv";
        Integer page = 0;
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", page.toString());
        Page<Article> articlePage = repository.findArticlesByQuery(
                Article.ArticleStatus.getArticleStatus(status), queryParams);

        log.debug("Total elements: " + articlePage.getTotalElements());
        File csvOutputFile = new File(file);

        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            StringBuffer csvHeader = new StringBuffer("");
            String heathers = "";
            if (status.equals("Positive")) {
                heathers = "id,title,publishedDate,doi,pmid,pmcid,journal,usage,nReconstructions,status,textType,portals,keywords,authors,abstract\n";
            } else {
                heathers = "id,title,publishedDate,doi,pmid,pmcid,journal,usage,textType,portals,keywords,authors,abstract\n";
            }
            csvHeader.append(heathers);
            pw.write(csvHeader.toString());

            while (page < articlePage.getTotalPages()) {
                log.debug("Reading page: " + page);
                StringBuffer csvData = new StringBuffer("");

                for (Article article : articlePage.getContent()) {
                    try {
                        Integer iterations = 1;
                        if (status.equals("Positive") && 
                                article.getUsage().contains(ArticleData.DataUsage.DESCRIBING_NEURONS) &&
                                article.getReconstructions() != null) {
                            iterations = article.getReconstructions().getCurrentStatusList().size();
                        }
                        for (Integer i = 0; i < iterations; i++) {
                            csvData = new StringBuffer("");
                            csvData.append(article.getId());
                            csvData.append(",");
                            csvData.append("\"" + article.getTitle().replace('"', '\'') + "\"");
                            csvData.append(",");
                            csvData.append(article.publishedDate());
                            csvData.append(",");
                            csvData.append("\"" + article.getDoi() + "\"");
                            csvData.append(",");
                            csvData.append(article.getPmid());
                            csvData.append(",");
                            csvData.append(article.getPmcid());
                            csvData.append(",");
                            csvData.append("\"" + article.getJournal() + "\"");
                            csvData.append(",");
                            csvData.append("\"" + String.join(";", article.getUsage().toString()) + "\"");
                            if (status.equals("Positive")){
                                if (article.getUsage().contains(ArticleData.DataUsage.DESCRIBING_NEURONS)) {
                                    csvData.append(",");
                                    csvData.append(article.getReconstructions().getCurrentStatusList().get(i).getNReconstructions());
                                    csvData.append(",");
                                    csvData.append(article.getReconstructions().getCurrentStatusList().get(i).getSpecificDetails());
                                } else {
                                    csvData.append(",");
                                    csvData.append(0);
                                    csvData.append(",");
                                    csvData.append(" ");
                                }
                        }
                        csvData.append(",");
                        csvData.append(article.getTextType());
                        csvData.append(",");
                        csvData.append("\"" + String.join(",", article.getPortalNameList()) + "\"");
                        csvData.append(",");
                        csvData.append("\"" + String.join(";", article.getKeyWords()) + "\"");
                        csvData.append(",");
                        csvData.append("\"" + String.join(";", article.getAuthorListStr()) + "\"");
                        csvData.append(",");
                        if (article.hasText() && text) {
                            log.debug("Abstract for article id: " + article.getId());
                            ArticleContent articleContent = fullTextCommunication.getFullText(article.getId().toString());
                            csvData.append("\"" + articleContent.getAbstractContent().replace('"', '\'') + "\"");
                        }
                        csvData.append('\n');
                        pw.write(csvData.toString());
                    }

                } catch(Exception ex){
                    csvData.append(",");
                    csvData.append("WARN");
                    csvData.append('\n');
                    pw.write(csvData.toString());
                    log.error("Exception in article: " + article.getId().toString());
                    log.error("Exception creating report: ", ex);
                }
            }
            page++;
            queryParams.put("page", page.toString());
            articlePage = repository.findArticlesByQuery(
                    Article.ArticleStatus.getArticleStatus(status), queryParams);
        }
        pw.close();
        log.debug("Done creating the file: " + file);
    } catch(
    FileNotFoundException e)

    {
        log.error("Error, file not found: " + file, e);
    }

}

    public void deleteArticlesCsv(String status) {
        String file = folder + status + ".csv";
        try {
            File csvOutputFile = new File(file);
            log.debug("Removing file: " + file);
            FileUtils.forceDelete(csvOutputFile);
        } catch (IOException e) {
            log.error("Error removing file: " + file, e);
        }

    }
}
