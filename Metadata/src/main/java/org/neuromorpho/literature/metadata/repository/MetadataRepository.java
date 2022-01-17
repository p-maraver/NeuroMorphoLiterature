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

package org.neuromorpho.literature.metadata.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.neuromorpho.literature.metadata.model.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


@Repository
public class MetadataRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private MongoCollection<Metadata> collection;
    private MongoDatabase database;

    @Autowired
    public MetadataRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME,
            @Value("${spring.data.mongodb.collection}") String COLLECTION_NAME) {

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection(COLLECTION_NAME, Metadata.class);
        
    }
    

    public List<Metadata> findReviewedByType(String type) {

        Bson match = Filters.and(Filters.eq("type", type),
                Filters.eq("reviewed", Boolean.TRUE));

        FindIterable<Metadata> resultList = collection.find(match);

        return resultList.into(new ArrayList<>());

    }
    

}
