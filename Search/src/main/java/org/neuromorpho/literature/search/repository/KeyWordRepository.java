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
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.search.model.KeyWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.springframework.data.domain.PageRequest.of;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class KeyWordRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private MongoDatabase database;
    private MongoCollection<KeyWord> collection;
    @Value("${spring.data.mongodb.collection.keyword}")
    private String COLLECTION_NAME;

    private final Integer size = 50;

    @Autowired
    public KeyWordRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME,
            @Value("${spring.data.mongodb.collection.keyword}") String COLLECTION_NAME) {

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection(COLLECTION_NAME, KeyWord.class);

    }
    

    public List<KeyWord> find(String field, Object value){
        Bson match = Filters.eq(field, value);
        FindIterable<KeyWord> resultList = collection.find(match);
        return resultList.into(new ArrayList<>());
    }

    public void update(ObjectId id, String field, Object value){
        Bson match = Filters.eq("_id", id);
        collection.updateOne(match, Updates.set(field, value));
    }

    public Page<KeyWord> findAll(Integer page){
        MongoCollection<KeyWordAggregation> collection = database.getCollection(COLLECTION_NAME, KeyWordAggregation.class);

        // Filter
        List<Bson> aggregatesList = new ArrayList();
       
        // Pages
        Facet facetArray[] = new Facet[2];
        facetArray[0] = new Facet("paginatedResults", Aggregates.skip(size * page), Aggregates.limit(size));
        facetArray[1] = new Facet("count", Aggregates.count());

        aggregatesList.add(Aggregates.facet(facetArray));


        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);

        AggregateIterable<KeyWordAggregation> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray));
        MongoCursor<KeyWordAggregation> iterator = resultList.iterator();

        KeyWordAggregation aggregation = iterator.next();

        return new PageImpl(aggregation.getPaginatedResults(), of(page, size), aggregation.getSize());

    }

    public void save(KeyWord keyWord) {
        collection.insertOne(keyWord);
    }

    public void delete(String id) {
        Bson match = Filters.eq("_id", new ObjectId(id));
        collection.deleteOne(match);
    }

    public void updateAll(String field, Object value){
        collection.updateMany(new Document(), Updates.set(field, value));
    }


}
