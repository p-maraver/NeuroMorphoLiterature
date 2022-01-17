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

package org.neuromorpho.literature.search.service.springernature.model.search;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Creator {

    @JsonProperty("creator")
    private String creator;


    public String getName() {
        String[] name = creator.split(", ");
        if (name.length == 1){
            String lastName = name[0];
            name = new String[2];
            name[0] = lastName;
            name[1] = lastName;
        }
        if (name.length < 2) {
            name = creator.split(" ");
        }
        return name[1] + " " + name[0];
    }
}
