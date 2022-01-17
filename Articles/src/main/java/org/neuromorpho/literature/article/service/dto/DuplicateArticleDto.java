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


package org.neuromorpho.literature.article.service.dto;



public class DuplicateArticleDto {
    
    private String title1;
    private String title2;
    private Float distance;

    public DuplicateArticleDto(String title1, String title2, Float distance) {
        this.title1 = title1;
        this.title2 = title2;
        this.distance = distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public Float getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "DuplicateArticleDto{" +
                "title1='" + title1 + '\'' +
                ", title2='" + title2 + '\'' +
                ", distance=" + distance +
                '}';
    }
}
