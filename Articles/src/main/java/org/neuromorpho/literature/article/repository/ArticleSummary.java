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

package org.neuromorpho.literature.article.repository;

import org.bson.Document;
import org.neuromorpho.literature.article.model.article.ReconstructionsStatus;

public class ArticleSummary {

    private String specificDetails;
    private String status;
    private Double nArticles;
    private Double nSharedArticles;
    private Double nReconstructions;

    public ArticleSummary() {
    }

    public ArticleSummary(Document document) {
        ReconstructionsStatus.SpecificDetails details = ReconstructionsStatus.SpecificDetails.valueOf(document.getString("_id"));
        this.specificDetails = details.getDetails();
        this.status = details.getStatus();
        this.nArticles = document.getDouble("nArticles");
        this.nSharedArticles = document.getDouble("nSharedArticles");
        this.nReconstructions = document.getDouble("nReconstructions");
    }

    public String getSpecificDetails() {
        return specificDetails;
    }

    public void setSpecificDetails(String specificDetails) {
        this.specificDetails = specificDetails;
    }

    public Double getnArticles() {
        return nArticles;
    }

    public void setnArticles(Double nArticles) {
        this.nArticles = nArticles;
    }

    public Double getnReconstructions() {
        return nReconstructions;
    }

    public void setnReconstructions(Double nReconstructions) {
        this.nReconstructions = nReconstructions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getnSharedArticles() {
        return nSharedArticles;
    }

    public void setnSharedArticles(Double nSharedArticles) {
        this.nSharedArticles = nSharedArticles;
    }
}