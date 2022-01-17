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

package org.neuromorpho.literature.reconstructions.status.expiration.service;

import java.util.Date;

import org.neuromorpho.literature.reconstructions.status.expiration.communication.Article;
import org.neuromorpho.literature.reconstructions.status.expiration.communication.ArticlePage;
import org.neuromorpho.literature.reconstructions.status.expiration.communication.LiteratureCommunication;
import org.neuromorpho.literature.reconstructions.status.expiration.communication.ReconstructionsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LiteratureCommunication literatureCommunication;

    public void updateStatus(String statusFrom, String statusTo) {

        Integer page = 0;
        ArticlePage articlePage;
        do {
            articlePage = literatureCommunication.getArticles(page, statusFrom, new Date());

            for (Article article : articlePage.getContent()) {
                try {
                    log.debug("Reading article with id " + article.getId());
                    if (article.isNegativeIfNoAnswer()) { //move to negative collection
                        log.debug("Moving article to negative");
                        literatureCommunication.updateArticle2Negative(article.getId());
                    } else {//update to new status
                        for (ReconstructionsStatus status : article.getReconstructions().getReconstructionsList()) {
                            log.debug("Expiration date " + status.getExpirationDate().toString());
                            log.debug("Updating article reconstructions status to " + statusTo + " from "
                                    + status.getStatusDetails());

                            status.setStatusDetails(statusTo);
                        }
                        literatureCommunication.update(article.getId(), article.getReconstructions());

                    }
                } catch (Exception ex){
                    log.error("Error expiring reconstructions: ", ex);
                }
            }
            page++;

        } while (!articlePage.getLast());

    }

}
