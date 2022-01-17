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

import org.neuromorpho.literature.search.dto.fulltext.ArticleContentDto;
import org.neuromorpho.literature.search.model.KeyWord;
import org.neuromorpho.literature.search.model.Log;
import org.neuromorpho.literature.search.repository.KeyWordRepository;
import org.neuromorpho.literature.search.repository.PortalRepository;
import org.neuromorpho.literature.search.communication.article.LiteratureConnection;
import org.neuromorpho.literature.search.model.Portal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PortalSearchFactory portalSearchFactory;

    @Autowired
    private PortalRepository portalRepository;
    @Autowired
    private KeyWordRepository keyWordRepository;
    @Autowired
    private LiteratureConnection literatureConnection;

    public void launchSearch() throws Exception {
        List<Portal> portalList = portalRepository.findByActive(Boolean.TRUE);
        for (Portal portal : portalList) {
            List<KeyWord> keyWordList = keyWordRepository.find("executedList." + portal.getName(), 0);
            //For each portal update executed keywords
            Log portalLog = new Log();
            portalRepository.update(portal.getId(), "log", portalLog);

            try {
                IPortalSearch portalSearch = portalSearchFactory.launchPortalSearch(portal.getName());

                for (KeyWord keyWord : keyWordList) {
                    try {
                        portalSearch.findArticleList(keyWord, portal);
                        keyWordRepository.update(keyWord.getId(), "executedList." + portal.getName(), 1);
                    } catch (Exception ex) {
                        keyWordRepository.update(keyWord.getId(), "executedList." + portal.getName(), -1);
                    }
                }
                portalLog.setCause("Finished");
            } catch (InterruptedException ex) {
                portalLog.setCause("Interrupted by user");
            } catch (Exception ex) {
                log.error("Unknown error", ex);
                portalLog.setCause("HTTP Connection Error");
                portalRepository.update(portal.getId(), "log", portalLog);
                throw ex;
            }
            portalRepository.update(portal.getId(), "log", portalLog);

        }
        for (Portal portal : portalList) {
            keyWordRepository.updateAll("executedList." + portal.getName(), 0);
        }


    }


    public ArticleContentDto getArticleContent(String id,
                                               String portalName) throws Exception {
        Portal portal = portalRepository.findByName(portalName);
        IPortalSearch portalSearch = portalSearchFactory.launchPortalSearch(portalName);
        ArticleContentDto articleContentDto = portalSearch.getArticleContent(id, portal);
        return articleContentDto;
    }
    

}
