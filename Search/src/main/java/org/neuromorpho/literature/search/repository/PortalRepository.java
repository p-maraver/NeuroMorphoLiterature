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

package org.neuromorpho.literature.search.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.search.model.Portal;
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
public class PortalRepository {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    MongoCollection<Portal> collection;

    @Autowired
    public PortalRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME,
            @Value("${spring.data.mongodb.collection.portal}") String COLLECTION_NAME) {

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection(COLLECTION_NAME, Portal.class);

    }


    public List<Portal> findByActive(Boolean active){
        Bson match = Filters.eq("active", active);
        FindIterable<Portal> resultList = collection.find(match);
        return resultList.into(new ArrayList<>());
    }

    public Portal findByName(String name){
        Bson match = Filters.eq("name", name);
        FindIterable<Portal> resultList = collection.find(match);
        return resultList.first();
    }

    public void update(ObjectId id, String field, Object value){
        Bson match = Filters.eq("_id", id);
        collection.updateOne(match, Updates.set(field, value));
    }

    public void save(Portal portal){
        Bson match = Filters.eq("_id", portal.getId());
        collection.findOneAndReplace(match, portal);
    }

    public void updateAll(String field, Object value){
        collection.updateMany(new Document(), Updates.set(field, value));
    }

    public List<Portal> findAll(){
        FindIterable<Portal> resultList = collection.find(new Document());
        return resultList.into(new ArrayList<>());
    }




}
