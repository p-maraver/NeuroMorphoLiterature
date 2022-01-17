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

package org.neuromorpho.literature.release.service;

import java.io.IOException;

import org.neuromorpho.literature.release.repository.ArticleRepositoryMongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReleaseService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final static String COLLECTION_NEGATIVES = "article.negatives";
    private final static String COLLECTION_POSITIVES = "article.positives";
    private final static String OUT_NEGATIVES = "emailing_negatives";
    private final static String OUT_POSITIVES = "emailing_positives";
    @Autowired
    private ArticleRepositoryMongo repository;
    
    public void dumpLiteratureDB(String version) throws IOException, InterruptedException {
        log.debug("Dumping DB:" + version);

        String[] cmd = {"./dumpDB.sh", version};
        Process p = Runtime.getRuntime().exec(cmd);

        p.waitFor();
        log.debug("DONE Dumping DB");

    }

    public void restoreLiteratureDB(String version, String type) throws IOException, InterruptedException {
        log.debug("Restoring DB in remote server cng.gmu.edu for version: " + version);
        if (type.equals("main")) {
            String[] cmd = {"./importDBRemoteMain.sh", version};
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

        } else {
            String[] cmd = {"./importDBRemoteReview.sh", version};
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

        }

        log.debug("DONE Restoring DB in remote server cng.gmu.edu");

    }

    public void scpLiteratureDB(String version) throws IOException, InterruptedException {
        log.debug("Copying DB to remote server cng.gmu.edu");
        String[] cmd = {"./copy2Remote.sh", version};
        Process p = Runtime.getRuntime().exec(cmd);
        p.waitFor();
        log.debug("DONE Copying DB to remote server cng.gmu.edu");

    }

    public void scpFromRemote() throws IOException, InterruptedException {
        log.debug("Copying Reports to local server");
        Process p = Runtime.getRuntime().exec("./copyFromRemote.sh");
        p.waitFor();
        log.debug("DONE Copying Reports to local server");

    }

}
