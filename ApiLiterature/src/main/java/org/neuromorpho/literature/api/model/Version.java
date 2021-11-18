/*
 * Copyright (c) 2015-2021, Patricia Maraver
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

/**
 *  Model. Spring MVC Pattern
 */
package org.neuromorpho.literature.api.model;

import java.util.Date;


/**
 *  Version Model. Spring MVC Pattern
 */
public class Version {

    private String id;
    private String version;
    private String type;
    private Date date;

    public Version() {
    }

    public Version(String version, String type, Date date) {
        this.version = version;
        this.type = type;
        this.date = date;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Version{" + "id=" + id + ", version=" + version + ", type=" + type 
                + ", date=" + date + '}';
    }

   

}
