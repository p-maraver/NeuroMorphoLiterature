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

package org.neuromorpho.literature.article.model.article;

import java.util.*;

public class StopWords {

    private String[] defaultStopWords = {"i", "a", "about", "an", "and",
            "are", "as", "at", "be", "by", "com", "for", "from", "how",
            "in", "is", "it", "of", "on", "or", "that", "the", "this",
            "to", "was", "what", "when", "where", "who", "will", "with", 
            "…", "(", ")", ":", ",", "/", "'"};

    private static HashSet stopWords = new HashSet();

    public StopWords() {
        stopWords.addAll(Arrays.asList(defaultStopWords));
    }

    public List<String> removeStopWords(String string) {
        StringTokenizer tokens = new StringTokenizer(string.toLowerCase());
        List<String> result = new ArrayList<>();
        while (tokens.hasMoreElements()) {
            String word = tokens.nextToken();
            if (!stopWords.contains(word)) {
                result.add(word);
            }
        }
        return result;
    }
}
