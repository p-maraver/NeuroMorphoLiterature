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

package org.neuromorpho.literature.reconstructions.status.expiration.communication;

import java.util.Map;

public class Article {

    private String id;
    private Reconstructions reconstructions;
    private Map<String, Object> metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reconstructions getReconstructions() {
        return reconstructions;
    }

    public void setReconstructions(Reconstructions reconstructions) {
        this.reconstructions = reconstructions;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
    
    public Boolean isNegativeIfNoAnswer(){
        return (this.metadata.get("negativeIfNoAnswer") != null &&
                this.metadata.get("negativeIfNoAnswer").equals(Boolean.TRUE));
    }
}
