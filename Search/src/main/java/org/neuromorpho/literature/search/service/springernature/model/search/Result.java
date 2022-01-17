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

public class Result {
    
    @JsonProperty("total")
    private Integer total;

    @JsonProperty("start")
    private Integer start;

    @JsonProperty("pageLength")
    private Integer pageLength;

    @JsonProperty("recordsDisplayed")
    private Integer recordsDisplayed;


    public Integer getTotal() {
        return total;
    }

    public Integer getPageLength() {
        return pageLength;
    }
}
