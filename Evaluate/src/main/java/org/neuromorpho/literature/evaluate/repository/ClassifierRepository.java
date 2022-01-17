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

package org.neuromorpho.literature.evaluate.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.evaluate.exception.DuplicatedException;
import org.neuromorpho.literature.evaluate.model.Classifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


@Repository
public class ClassifierRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private MongoCollection<Classifier> collection;
    private MongoDatabase database;

    @Autowired
    public ClassifierRepository(
            @Value("${mongodb.database}") String DBNAME,
            @Value("${mongodb.collection}") String COLLECTION_NAME) {

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection(COLLECTION_NAME, Classifier.class);


    }

    public List<Classifier> findAll() {
        FindIterable<Classifier> resultList = collection.find();
        return resultList.into(new ArrayList<>());
    }

    public Classifier findCurrent() {
        Bson match = Filters.eq("current", Boolean.TRUE);
        FindIterable<Classifier> resultList = collection.find(match);
        return resultList.first();
    }

    public Classifier save(Classifier classifier) {
        Bson match = Filters.eq("version", classifier.getVersion());
        FindIterable<Classifier> dupList = collection.find(match);
        if (dupList.first() != null){
            throw new DuplicatedException();
        }
        collection.insertOne(classifier);
        return collection.find(match).first();
    }

    public void replace(ObjectId id, Classifier classifier) {
        Bson match = Filters.eq("_id", id);
        collection.replaceOne(match, classifier);
    }

    public void update(
            ObjectId id,
            String field,
            Object object) {

        Bson match = Filters.eq("_id", id);
        collection.updateOne(match, set(field, object));
    }
}
