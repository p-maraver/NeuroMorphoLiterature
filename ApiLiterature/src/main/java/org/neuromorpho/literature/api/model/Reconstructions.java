/*
 * Copyright (c) 2015-2021, Patricia Maraver
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

package org.neuromorpho.literature.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  List of current and past reconstructions statuses Model. Spring MVC Pattern
 */
public class Reconstructions {

    private List<ReconstructionsStatus> currentStatusList;
    private List<ReconstructionsStatus> pastStatusList;
    private String globalStatus;

    public Reconstructions() {
    }
    public Reconstructions initializeReconstructionsStatus(Double nReconstructions){
        this.currentStatusList = new ArrayList<>();
        ReconstructionsStatus reconstructionsStatus = new ReconstructionsStatus(nReconstructions);
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

    public void updateCurrentStatusList(List<ReconstructionsStatus> currentStatusList){
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

    public void setGlobalStatus(String globalStatus) {
        this.globalStatus = globalStatus;
    }

    public String getGlobalStatus() {
        return globalStatus;
    }
/* @BsonIgnore
    public String getGlobalStatus(){
        String globalStatus = "Not available";
        for (ReconstructionsStatus status: this.getCurrentStatusList()){
            if (status.getSpecificDetails().getStatus().equals("Available")){
                return "Available";
            } else  if (status.getSpecificDetails().getStatus().equals("Determining availability")){
                globalStatus = "Determining availability";
            }
        }
        return globalStatus;
        
    }*/

    @Override
    public String toString() {
        return "Reconstructions{" +
                ", currentStatusList=" + currentStatusList +
                ", pastStatusList=" + pastStatusList +
                '}';
    }
}
