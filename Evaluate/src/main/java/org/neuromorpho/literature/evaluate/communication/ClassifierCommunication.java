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

package org.neuromorpho.literature.evaluate.communication;


import org.neuromorpho.literature.evaluate.model.Classifier;
import org.neuromorpho.literature.evaluate.communication.PDF.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ClassifierCommunication {

    @Value("${uriClassifier}")
    private String uri;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public ClassifierEvaluation classify(String version, Text text) {
        String url = uri + "classifier/classify?version=" + version;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        ClassifierEvaluation classifier = restTemplate.postForObject(url, text, ClassifierEvaluation.class);
        return classifier;
    }

    public Classifier train(String version) {
        String url = uri + "classifier/train?version=" + version;
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        Classifier classifier = restTemplate.postForObject(url, null, Classifier.class);
        return classifier;
    }

    public void extractRelevant(String version) {
        String url = uri + "classifier/train?version=" + version;
        log.debug("Extracting relevant info ");

        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(url, null);
    }

    public NLP nlp(Text text) {
        String url = uri + "nlp";
        log.debug("Creating rest connection for URI: " + url);
        RestTemplate restTemplate = new RestTemplate();
        NLP nlp = restTemplate.postForObject(url, text, NLP.class);
        return nlp;
    }


}
