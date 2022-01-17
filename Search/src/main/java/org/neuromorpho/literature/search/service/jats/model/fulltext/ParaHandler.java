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

package org.neuromorpho.literature.search.service.jats.model.fulltext;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
public class ParaHandler extends XmlAdapter<Object, Para> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Factory for building DOM documents.
     */
    private final DocumentBuilderFactory docBuilderFactory;
    /**
     * Factory for building transformers.
     */
    private final TransformerFactory transformerFactory;

    public ParaHandler() {
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        transformerFactory = TransformerFactory.newInstance();
    }

    @Override
    public Para unmarshal(Object o) throws Exception {
        // The provided Objects is a DOM Element
        Element titleElement = (Element) o;
        Para para = new Para();

        NodeList figNodeList = titleElement.getElementsByTagName("fig");
        if (figNodeList.getLength() > 0) { // if the paragraph contains a figure
            NodeList nodeList = titleElement.getChildNodes();
            ArrayList<Figure> figureList = new ArrayList<>();
            String paragraph = "";
            for (Integer i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeName().equals("fig")){
                    if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element elem = (Element) nodeList.item(i);
                        if (elem.getNodeName().equals("fig")) {
                            Figure figure = this.unmarshalDisplay(elem);
                            figureList.add(figure);
                        }
                    }
                } else {
                    paragraph = paragraph + nodeList.item(i).getTextContent();
                }
            }
            para.setFigureList(figureList);
            para.setParagraph(paragraph);

        } else {
            para.setParagraph(titleElement.getTextContent());
        }


        return para;
        
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
    public Object marshal(Para s) throws Exception {
        return null;
    }

    private Figure unmarshalDisplay(Object o) throws Exception {
        // The provided Objects is a DOM Element
        try {
            Element titleElement = (Element) o;
            String label = titleElement.getElementsByTagName("label").getLength()>0?
                    titleElement.getElementsByTagName("label").item(0).getTextContent():null;
            String caption =titleElement.getElementsByTagName("caption").getLength()>0?
                    titleElement.getElementsByTagName("caption").item(0).getTextContent():null;
            Element link = (Element) titleElement.getElementsByTagName("graphic").item(0);
            Element media = (Element) titleElement.getElementsByTagName("media").item(0);

            Graphic graphic;
            if (link != null){
                graphic = new Graphic(link.getAttributeNode("xlink:href").getValue());
            } else {
                graphic = new Graphic(media.getAttributeNode("xlink:href").getValue());
            }
            Figure figure = new Figure(label, caption, graphic);
            return figure;
        } catch (Exception e) {
            log.error("Error", e);
        }
        return null;
    }
}


