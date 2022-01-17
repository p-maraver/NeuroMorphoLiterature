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

package org.neuromorpho.literature.pdf.model;


import org.neuromorpho.literature.pdf.exception.ParsingPdfException;
import org.neuromorpho.literature.pdf.exception.PdfNotFoundException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


public class Pdf {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private String link;
    private String text;
    private InputStream file;

    public Pdf(String link) {
        this.link = link;
    }

    public Pdf(InputStream file) {
        this.file = file;
    }

    public String getText() {
        if (this.file == null) {
            this.file = this.download(this.link);
        }
        this.text = this.extractText();
        return this.text;
    }

    private String extractText() {
        try {

            log.debug("Creating stream");

            ParseContext pcontext = new ParseContext();
            StringWriter any = new StringWriter();

            BodyContentHandler handler = new BodyContentHandler(any);
            log.debug("Parsing the document");

            //parsing the document using PDF parser
            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(this.file, handler, new Metadata(), pcontext);

            // log.debug("Contents of the PDF :" + handler.toString());
            String rawText = handler.toString();
            return rawText;
        } catch (IOException | SAXException | TikaException e) {
            log.error("Error", e);
            throw new ParsingPdfException("");
        }
    }

    private InputStream download(String pdfLink) {
        try {
            log.debug("Downloading pdf from link: " + pdfLink);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .build();

            CloseableHttpClient httpClient =
                    HttpClients.custom()
                            .setSSLHostnameVerifier(new NoopHostnameVerifier())
                            .setDefaultRequestConfig(requestConfig)
                            .build();
            HttpComponentsClientHttpRequestFactory requestFactory = new
                    HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);

            RestTemplate restTemplate = new RestTemplate(requestFactory);

            restTemplate.getMessageConverters().add(
                    new ByteArrayHttpMessageConverter());

            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            List<MediaType> mediaTypeList = new ArrayList<>();
            mediaTypeList.add(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<Object> entity = new HttpEntity<>(headers);

            ResponseEntity response = restTemplate.exchange(
                    pdfLink, HttpMethod.GET, entity, byte[].class);

            log.debug("Content type for the url: "
                    + response.getHeaders().getContentType()
                    + " location :" + response.getHeaders().getLocation());

            while (response.getStatusCode() == HttpStatus.SEE_OTHER
                    || response.getStatusCode() == HttpStatus.FOUND
                    || response.getStatusCode() == HttpStatus.MOVED_PERMANENTLY) {

                log.debug("Content type fo the url: "
                        + response.getHeaders().getContentType()
                        + " location :" + response.getHeaders().getLocation());
                try {
                    response = restTemplate.exchange(
                            response.getHeaders().getLocation(),
                            HttpMethod.GET, entity, byte[].class);
                } catch (HttpClientErrorException ex) {
                    log.debug("Error: ", ex);
                    throw new PdfNotFoundException("PDF not accessible for link: " + pdfLink);
                }
            }

            if (response.getHeaders().getContentType().toString()
                    .contains(MediaType.APPLICATION_PDF_VALUE)
                    || response.getHeaders().getContentType().toString()
                    .contains(MediaType.APPLICATION_OCTET_STREAM.toString())) {
                log.debug("Downloading file");

                if (response.getStatusCode() == HttpStatus.OK) {
                    byte[] content = (byte[]) response.getBody();
                    InputStream pdf = new ByteArrayInputStream(content);
                    return pdf;
                }
            }
        } catch (HttpClientErrorException ex){
            log.debug("Forbidden");
        }
        throw new PdfNotFoundException("PDF not accessible for link: " + pdfLink);

    }


}
    

