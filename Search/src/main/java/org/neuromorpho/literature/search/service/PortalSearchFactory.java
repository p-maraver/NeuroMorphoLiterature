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

package org.neuromorpho.literature.search.service;

import org.neuromorpho.literature.search.service.pubmed.PortalSearchPubMedService;
import org.neuromorpho.literature.search.service.sciencedirect.PortalSearchScienceDirectService;
import org.neuromorpho.literature.search.service.springernature.PortalSearchSpringerNatureService;
import org.neuromorpho.literature.search.service.wiley.PortalSearchWileyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component()
public class PortalSearchFactory {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PortalSearchPubMedService portalSearchPubMedService;

    @Autowired
    private PortalSearchSpringerNatureService portalSearchSpringerNatureService;

    @Autowired
    private PortalSearchScienceDirectService portalSearchScienceDirectService;

    @Autowired
    private PortalSearchWileyService portalSearchWileyService;
    
     @Autowired
    private PortalSearchGoogleScholarService portalSearchGoogleScholarService;

    public IPortalSearch launchPortalSearch(String portalName) throws Exception {
        // sets the property for selenium
        log.debug("Creating the object for portal: " + portalName);
        if (portalName.equalsIgnoreCase("PubMed") || 
                portalName.equalsIgnoreCase("PubMedCentral")) {
            return portalSearchPubMedService;
        } else if (portalName.equalsIgnoreCase("SpringerNature")) {
            return portalSearchSpringerNatureService;
        } else if (portalName.equalsIgnoreCase("ScienceDirect")) {
            return portalSearchScienceDirectService;
        } else if (portalName.equalsIgnoreCase("Wiley")) {
            return portalSearchWileyService;
        } else if (portalName.equalsIgnoreCase("GoogleScholar")) {
            return portalSearchGoogleScholarService;
        } else {
            log.warn("Unsuported Portal: " + portalName);
        }
        return null;
    }
}
