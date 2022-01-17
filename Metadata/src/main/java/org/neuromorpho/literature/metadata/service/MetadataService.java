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

package org.neuromorpho.literature.metadata.service;


import org.neuromorpho.literature.metadata.model.Metadata;
import org.neuromorpho.literature.metadata.repository.MetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MetadataService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MetadataRepository repository;
    

    public List<String> getReviewedByType(String type) {
        log.debug("Getting classifier values for field: " + type);
        List<Metadata> metadataList = repository.findReviewedByType(type);
        return metadataList.stream().map(p -> p.getName()).collect(Collectors.toList());

    }
    

}
