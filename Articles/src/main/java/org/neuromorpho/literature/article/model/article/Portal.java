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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Portal {

    private String name;
    private List<String> keyWordList;

    public Portal() {
    }

    public Portal(String name, List<String> keyWordList) {
        this.name = name;
        this.keyWordList = keyWordList;
    }

    public Portal(String name, String keyWord) {
        this.name = name;
        this.keyWordList = new ArrayList();
        this.keyWordList.add(keyWord);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(List<String> keyWordList) {
        this.keyWordList = keyWordList;
    }

    public void updateKeyWordList(List<String> keyWordList) {
        for (String keyword : keyWordList) {
            if (!this.keyWordList.contains(keyword)) {
                this.keyWordList.add(keyword);
            }
        }
    }

    public Set<String> getKeyWordSet() {
        Set<String> keyWordSet = new HashSet<>();
        for (String keyWord : this.keyWordList) {
            keyWordSet.add(keyWord);
        }
        return keyWordSet;
    }

    @Override
    public String toString() {
        String result = "{" + this.name + ": ";
        for (String keyWord : keyWordList) {
            result = result + keyWord + ",";
        }
        result = result.substring(0, result.length() - 1) + "}";
        return result;
    }
}
