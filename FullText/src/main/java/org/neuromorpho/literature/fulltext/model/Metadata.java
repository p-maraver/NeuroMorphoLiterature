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

package org.neuromorpho.literature.fulltext.model;


import java.util.HashSet;
import java.util.Set;

public class Metadata {
    
    private String name;
    private Set<String> valueList;

    public Metadata() {
    }

    public Metadata(String name, Set<String> valueList) {
        if (this.name == null){
            this.name = name;
            this.valueList = new HashSet<>();
        }
        this.valueList.addAll(valueList);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getValueList() {
        return valueList;
    }

    public void setValueList(Set<String> valueList) {
        this.valueList = valueList;
    }
}
