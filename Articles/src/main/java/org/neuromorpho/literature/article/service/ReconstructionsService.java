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

import org.neuromorpho.literature.article.model.article.ReconstructionsStatus;
import org.neuromorpho.literature.article.repository.ArticleRepository;
import org.neuromorpho.literature.article.repository.ArticleSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReconstructionsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
   
    @Autowired
    private ArticleRepository repository;
   
    public Page<ReconstructionsStatus> findReconstructionStatusBySpecificDetails(
            List<ReconstructionsStatus.SpecificDetails> specificDetails, Date expirationDate, Integer page) {
        return null;
       // return reconstructionsRepositoryExtended.findByCurrentStatusListSpecificDetails(
        //         specificDetails, expirationDate, page);
    }

    public List<String> findSpecificDetailsValues() {
        List<String> values = new ArrayList();
        for (ReconstructionsStatus.SpecificDetails details : ReconstructionsStatus.SpecificDetails.values()) {
            values.add(details.getDetails());
        }
        return values;
    }
    

    public List<ArticleSummary> getSummaryReconstructions(Boolean expired) {
        List<ArticleSummary> sharedArticleSummaryList = repository.getSummaryShared();
        List<ArticleSummary> articleSummaryList = repository.getSummary(expired);
        for (ArticleSummary articleSummary: articleSummaryList){
            ArticleSummary sharedArticleSummary = sharedArticleSummaryList.stream().filter(o -> o.getSpecificDetails().equals(articleSummary.getSpecificDetails())).findFirst().orElse(null);
            Double sharedArticles = 0D;
            if (sharedArticleSummary != null) {
                sharedArticles = sharedArticleSummary.getnSharedArticles();
            }
            articleSummary.setnSharedArticles(sharedArticles);
        }
        return articleSummaryList;
    }
    
}
