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

package org.neuromorpho.literature.api.repository;

import org.neuromorpho.literature.api.model.Version;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

/**
 *  Version Repository, queries to DB. Spring MVC Pattern
 */
@Repository
public class VersionRepository {
    
    private final MongoCollection<Document> collection;

    @Autowired
    public VersionRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME,
            @Value("${spring.data.mongodb.collection.version}") String COLLECTION_VERSION) {
        MongoClient mongoClient = new MongoClient();

        MongoDatabase database = mongoClient.getDatabase(DBNAME);
        this.collection = database.getCollection(COLLECTION_VERSION);

    }
    
    

    public Version findOneByType(String type){
        Document result = collection.find(Filters.eq("type", type)).first();
        return new Version(result.getString("version"), result.getString("type"), result.getDate("date"));
    }

    
    
}
