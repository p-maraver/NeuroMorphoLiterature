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

package org.neuromorpho.literature.pdf.controller;

import org.neuromorpho.literature.pdf.service.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping()
public class PdfController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PdfService service;

    /**
     * @apiVersion 1.0.0
     * @api {post} /file?file= Extract Text from PDF file
     * @apiParam {MultipartFile} file The Multipart PDF file
     *
     * @apiDescription Returns the text extracted from a PDF file
     * @apiName PostExtractPDFContentFromFile
     * @apiGroup  PDF
     *
     * @apiSuccess {String} text Text extracted from PDF link
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "text": "\nRadiation Dosimetry and Biodistribution in\nMonkey and Man of 11C-PBR28: 
     *      A PET\nRadioligand to Image Inflammation\n\nAmira K. Brown1, Masahiro Fujita1, 
     *      Yota Fujimura1, Jeih-San Liow1, Michael Stabin2, Yong H. Ryu1,3, Masao Imaizumi1,
     *      \nJinsoo Hong1, Victor W. Pike1, and  ..."
     * }
     */
    @RequestMapping(value = "file", method = RequestMethod.POST)
    public Map<String, String> extractPdfContentFromFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> result = new HashMap<>();
        String text = service.extractPdfContentFromFile(file);
        result.put("text", text);
        return result;
    }

    /**
     * @apiVersion 1.0.0
     * @api {get} /file?link= Extract Text from PDF link
     * @apiParam {String} link The PDF file URL linked
     *
     * @apiDescription Returns the text extracted from a PDF URL linked file
     * @apiName PostExtractPDFContentFromLink
     * @apiGroup  PDF
     *
     * @apiSuccess {String} text Text extracted from PDF link
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "text": "\nRadiation Dosimetry and Biodistribution in\nMonkey and Man of 11C-PBR28: 
     *      A PET\nRadioligand to Image Inflammation\n\nAmira K. Brown1, Masahiro Fujita1, 
     *      Yota Fujimura1, Jeih-San Liow1, Michael Stabin2, Yong H. Ryu1,3, Masao Imaizumi1,
     *      \nJinsoo Hong1, Victor W. Pike1, and  ..."
     * }
     */
     @RequestMapping(method = RequestMethod.GET)
    public Map<String, String> extractPdfContentFromLink(@RequestParam("link") String link) {
        Map<String, String> result = new HashMap<>();
        String text = service.extractPdfContentFromLink(link);
        result.put("text", text);
        return result;
    }
    
    
}


