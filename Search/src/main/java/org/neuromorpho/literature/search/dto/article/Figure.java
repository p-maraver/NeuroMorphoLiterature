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

package org.neuromorpho.literature.search.dto.article;

import com.mongodb.gridfs.GridFSDBFile;


public class Figure {
    
    private String legend;
    private GridFSDBFile image;

    public Figure() {
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public GridFSDBFile getImage() {
        return image;
    }

    public void setImage(GridFSDBFile image) {
        this.image = image;
    }
}