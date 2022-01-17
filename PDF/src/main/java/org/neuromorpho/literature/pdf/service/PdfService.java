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

package org.neuromorpho.literature.pdf.service;


import org.neuromorpho.literature.pdf.exception.ParsingPdfException;
import org.neuromorpho.literature.pdf.model.Pdf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class PdfService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public String extractPdfContentFromFile(MultipartFile file) {
        try {
            log.debug("Extracting pdf content");
            Pdf pdf = new Pdf(file.getInputStream());
            return pdf.getText();
        } catch (IOException e) {
            throw new ParsingPdfException("Exception extracting stream from MultipartFile");
        }
    }

    public String extractPdfContentFromLink(String link) {
            log.debug("Extracting pdf content");
            Pdf pdf = new Pdf(link);
            return pdf.getText();
    }

}
    

