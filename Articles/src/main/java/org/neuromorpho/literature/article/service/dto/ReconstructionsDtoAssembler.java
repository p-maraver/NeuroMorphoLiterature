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


package org.neuromorpho.literature.article.service.dto;


import org.neuromorpho.literature.article.model.article.Reconstructions;
import org.neuromorpho.literature.article.model.article.ReconstructionsStatus;

import java.util.ArrayList;
import java.util.List;

public class ReconstructionsDtoAssembler {

    protected ReconstructionsDto createReconstructionsDto(
            Reconstructions reconstructions) {
        if (reconstructions != null) {
            ReconstructionsDto reconstructionsDto = new ReconstructionsDto();

            if (reconstructions.getCurrentStatusList() != null) {
                List<ReconstructionsStatusDto> statusDtoList = new ArrayList();

                for (ReconstructionsStatus status : reconstructions.getCurrentStatusList()) {
                    ReconstructionsStatusDto statusDto = this.createReconstructionsStatusDto(status);
                    statusDtoList.add(statusDto);
                }
                reconstructionsDto.setReconstructionsList(statusDtoList);
                Double totalReconstructions = statusDtoList.stream().mapToDouble(o -> o.getnReconstructions()).sum();
                reconstructionsDto.setTotalReconstructions(totalReconstructions);
                reconstructionsDto.setGlobalStatus(reconstructionsDto.getGlobalStatus());
            }
            return reconstructionsDto;
        }
        return null;
    }
    
    

    private ReconstructionsStatusDto createReconstructionsStatusDto(ReconstructionsStatus status) {
        ReconstructionsStatusDto statusDto = null;
        if (status != null) {
            statusDto = new ReconstructionsStatusDto();
            String specificDetails = status.getSpecificDetails().getDetails();
            statusDto.setStatusDetails(specificDetails);
            statusDto.setExpirationDate(status.getExpirationDate());
            statusDto.setnReconstructions(status.getNReconstructions());
            statusDto.setDate(status.getDate());
            statusDto.setId(status.getIndex());
        }
        return statusDto;
    }

    protected List<ReconstructionsStatus> createCurrentStatusList(ReconstructionsDto reconstructionsDto) {
        
        List<ReconstructionsStatus> currentStatusList = new ArrayList();
        for (ReconstructionsStatusDto reconstructionStatusDto : reconstructionsDto.getReconstructionsList()) {
            currentStatusList.add(this.createReconstructionsStatus(reconstructionStatusDto));
        }
        return currentStatusList;
    }


    private ReconstructionsStatus createReconstructionsStatus(ReconstructionsStatusDto statusDto) {
        ReconstructionsStatus status = null;
        if (statusDto != null) {
            status = new ReconstructionsStatus();
            ReconstructionsStatus.SpecificDetails details = ReconstructionsStatus.SpecificDetails.getSpecificDetails(statusDto.getStatusDetails());
            status.setSpecificDetails(details);
            status.setExpirationDate(statusDto.getExpirationDate());
            status.setNReconstructions(statusDto.getnReconstructions());
            status.setIndex(statusDto.getId());
            status.setDate(statusDto.getDate());

        }
        return status;
    }
    private ReconstructionsStatus createReconstructions(ReconstructionsStatusDto recostructionStatusDto) {
        ReconstructionsStatus statusDetails = new ReconstructionsStatus(recostructionStatusDto.getStatusDetails(),
                recostructionStatusDto.getExpirationDate(), recostructionStatusDto.getnReconstructions());
        statusDetails.setIndex(recostructionStatusDto.getId());
        return statusDetails;
    }

}
