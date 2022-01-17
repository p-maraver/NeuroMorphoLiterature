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


package org.neuromorpho.literature.search.dto.fulltext.jats.pubmed;


import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class FigureDownloadDtoAssembler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    
    public void download(String url) {
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream("./temp.tar.gz");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            log.error("Error", e);
        }

    }

    public byte[] readFigure(String reference) {
        try {

            TarArchiveInputStream tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream("./temp.tar.gz")));
            TarArchiveEntry currentEntry = tarInput.getNextTarEntry();
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            while (currentEntry != null) {
                int i = currentEntry.getName().lastIndexOf('.');
                if (i != -1) {
                    String[] entry = {currentEntry.getName().substring(0, i),
                            currentEntry.getName().substring(i + 1, currentEntry.getName().length())};
                    if (entry[1].equals("jpg")) {
                        log.debug("Reading file from tar = " + currentEntry.getName());
                        if (entry[0].split("/")[1].equals(reference)) {
                            return IOUtils.toByteArray(tarInput);
                        }
                    }
                }
                currentEntry = tarInput.getNextTarEntry(); // You forgot to iterate to the next file
            }
        } catch (IOException e) {
            log.error("Error", e);
        }
        return null;
    }
    

}
