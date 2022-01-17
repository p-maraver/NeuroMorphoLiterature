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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;

@XmlAccessorType(XmlAccessType.FIELD)
public class ContentHandler extends XmlAdapter<java.lang.Object, String> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Factory for building DOM documents.
     */
    private final DocumentBuilderFactory docBuilderFactory;
    /**
     * Factory for building transformers.
     */
    private final TransformerFactory transformerFactory;

    public ContentHandler() {
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        transformerFactory = TransformerFactory.newInstance();
    }

    @Override
    public String unmarshal(java.lang.Object o) throws Exception {
        // The provided Objects is a DOM Element
        Element titleElement = (Element) o;
        return titleElement.getTextContent();
       /* // Getting the "a" child elements
        String a = titleElement.getFirstChild().getNodeValue();
        NodeList anchorElements = titleElement.getElementsByTagName("ce:cross-refs");
        // If there's none or multiple, return empty string
        for (Integer i = 0; i < anchorElements.getLength(); i++) {
            Element anchor = (Element) anchorElements.item(i);
            // Creating a DOMSource as input for the transformer
            DOMSource source = new DOMSource(anchor);
            // Default transformer: identity tranformer (doesn't alter input)
            Transformer transformer = transformerFactory.newTransformer();
            // This is necessary to avoid the <?xml ...?> prolog
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            // Transform to a StringWriter
            StringWriter stringWriter = new StringWriter();
            StreamResult result = new StreamResult(stringWriter);
            transformer.transform(source, result);
            // Returning result as string
            tagResult = tagResult + stringWriter.toString();
        }
       return tagResult;*/
    }

    @Override
    public java.lang.Object marshal(String s) throws Exception {
        return null;
    }
}


