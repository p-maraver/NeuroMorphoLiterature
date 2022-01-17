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


import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.neuromorpho.literature.article.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reconstructions {

    private List<ReconstructionsStatus> currentStatusList;
    private List<ReconstructionsStatus> pastStatusList;

    public Reconstructions() {
    }

    public Reconstructions initializeReconstructionsStatus(Double nReconstructions, ReconstructionsStatus.SpecificDetails specificDetails) {
        this.currentStatusList = new ArrayList<>();
        ReconstructionsStatus reconstructionsStatus = new ReconstructionsStatus(nReconstructions, specificDetails);
        this.currentStatusList.add(reconstructionsStatus);
        return this;
    }

    public Reconstructions(List<ReconstructionsStatus> currentStatusList) {
        this.currentStatusList = currentStatusList;
    }

    public List<ReconstructionsStatus> getCurrentStatusList() {
        return currentStatusList;
    }

    public List<ReconstructionsStatus> getPastStatusList() {
        return pastStatusList;
    }

    public void setPastStatusList(List<ReconstructionsStatus> pastStatusList) {
        this.pastStatusList = pastStatusList;
    }

    public void setCurrentStatusList(List<ReconstructionsStatus> currentStatusList) {
        this.currentStatusList = currentStatusList;
    }

    public void updateCurrentStatusList(List<ReconstructionsStatus> currentStatusList) {
        if (currentStatusList != null && this.currentStatusList != null) {
            if (this.pastStatusList == null) {
                this.pastStatusList = new ArrayList();
            }
            //compare all the elements ands save the ones that had changed
            for (ReconstructionsStatus statusOld : this.currentStatusList) {
                this.pastStatusList.add(statusOld);
            }
        }
        this.currentStatusList = currentStatusList;
    }


    @BsonIgnore
    public String getGlobalStatus() {
        String globalStatus = "Not available";
        for (ReconstructionsStatus status : this.getCurrentStatusList()) {
            if (status.getSpecificDetails().getStatus().equals("Available")) {
                return "Available";
            } else if (status.getSpecificDetails().getStatus().equals("Determining availability")) {
                globalStatus = "Determining availability";
            }
        }
        return globalStatus;

    }

    @BsonIgnore
    public void update2Next(String reconstructionsStatus, Boolean isNegativeIfNoAnswer) {
        try {
            if (reconstructionsStatus.equalsIgnoreCase(ReconstructionsStatus.SpecificDetails.BOUNCED.getDetails())) {
                this.currentStatusList.stream().forEach(r ->
                {
                    if (r.getSpecificDetails().getStatus().equals("Determining availability") ||
                            r.getSpecificDetails().equals(ReconstructionsStatus.SpecificDetails.NO_RESPONSE)) {
                        this.pastStatusList.add(new ReconstructionsStatus(r));
                        if (isNegativeIfNoAnswer) {
                            r.setSpecificDetails(ReconstructionsStatus.SpecificDetails.BOUNCED_NEGATIVE);
                           
                        } else {
                            r.setSpecificDetails(ReconstructionsStatus.SpecificDetails.BOUNCED);
                        }
                        r.setExpirationDate(LocalDate.now());
                        r.setDate(LocalDate.now());
                    }
                });
            } else {
                ReconstructionsStatus matchingObject = this.currentStatusList.stream().
                        filter(p -> p.getSpecificDetails().equals(
                                ReconstructionsStatus.SpecificDetails.getSpecificDetails(reconstructionsStatus))).
                        findFirst().orElse(null);
                if (this.pastStatusList == null) {
                    this.pastStatusList = new ArrayList<>();
                }
                if (matchingObject == null) {
                    matchingObject = this.currentStatusList.stream().
                            filter(p -> p.getSpecificDetails().equals(
                                    ReconstructionsStatus.SpecificDetails.BOUNCED)).
                            findFirst().orElse(null);
                    if (matchingObject == null){
                        matchingObject = this.currentStatusList.stream().
                                filter(p -> p.getSpecificDetails().equals(
                                        ReconstructionsStatus.SpecificDetails.BOUNCED_NEGATIVE)).
                                findFirst().orElse(null);
                    }
                }
                if (matchingObject != null){
                    this.pastStatusList.add(new ReconstructionsStatus(matchingObject));
                    matchingObject.next();
                }
               
            }
        } catch (NullPointerException ex) {
            throw new NotFoundException("Status reconstructions not found: " + reconstructionsStatus);
        }
    }

    @BsonIgnore
    public Boolean exists() {
        return this.getCurrentStatusList() != null && this.getCurrentStatusList().size() > 0;
    }

    @Override
    public String toString() {
        return "Reconstructions{" +
                ", currentStatusList=" + currentStatusList +
                ", pastStatusList=" + pastStatusList +
                '}';
    }
}
