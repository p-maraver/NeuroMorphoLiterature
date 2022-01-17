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

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import org.neuromorpho.literature.release.model.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.neuromorpho.literature.release.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Value;

@Service
public class VersionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${folder}")
    private String folder;

    @Autowired
    private VersionRepository versionRepository;

    public Version find(String type) {
        log.debug("Version for type: " + type);
        return versionRepository.findOneByType(type);
    }

    public void save(Version version) {
        log.debug("Saving or updating version: " + version.toString());
        versionRepository.save(version);
    }

    public List<String> findAll() {
        File file = new File(folder);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return Arrays.asList(directories);

    }
}
