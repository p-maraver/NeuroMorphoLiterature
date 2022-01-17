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

package org.neuromorpho.literature.search.service.portal;


import org.neuromorpho.literature.search.exception.MissingDataException;
import org.neuromorpho.literature.search.repository.KeyWordRepository;
import org.neuromorpho.literature.search.model.KeyWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class KeyWordService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KeyWordRepository repository;

    public Page<KeyWord> findAll(Integer page) {
        log.debug("Retrieving all keywords from DB");
        return repository.findAll(page);
    }

    public void save(
            KeyWord keyWord) {
        if (keyWord.isEmpty()){
            throw new MissingDataException("keyWord empty");
        }
        log.debug("saving new keyword: " + keyWord.toString());
        repository.save(keyWord);
    }
    

    public void delete(
            String id) {
        repository.delete(id);
    }
    
}
