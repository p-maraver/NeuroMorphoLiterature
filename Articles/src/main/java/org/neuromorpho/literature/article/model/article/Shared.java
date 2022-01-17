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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

public class Shared {
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId sharedId;

    public ObjectId getSharedId() {
        return sharedId;
    }

    public void setSharedId(ObjectId sharedId) {
        this.sharedId = sharedId;
    }

    public Shared() {
    }

    public Shared(String sharedId) {
        this.sharedId = new ObjectId(sharedId);
    }

    @Override
    public String toString() {
        return "SharedReconstructions{" +
                "sharedArticleId=" + sharedId +
                '}';
    }
}
