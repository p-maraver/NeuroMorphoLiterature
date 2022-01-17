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

package org.neuromorpho.literature.fulltext.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.fulltext.model.ArticleContent;
import org.neuromorpho.literature.fulltext.model.Category;
import org.neuromorpho.literature.fulltext.model.ArticleContentAggregation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.springframework.data.domain.PageRequest.of;

@Repository
public class ArticleDataRepository {

    private Integer size = 2;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MongoDatabase database;
    @Value("${spring.data.mongodb.collection.original}")
    private String ORIGINAL_COLLECTION;

    @Autowired
    public ArticleDataRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();

        this.database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);
    }


    public ArticleContent findById(String id) {
        MongoCollection<ArticleContent> collection = database.getCollection(ORIGINAL_COLLECTION, ArticleContent.class);
        return collection.find(Filters.eq("_id", new ObjectId(id))).first();
    }
    
    public void save(String id, ArticleContent article) {
        MongoCollection<ArticleContent> collection = database.getCollection(ORIGINAL_COLLECTION, ArticleContent.class);
        article.setId(new ObjectId(id));
        collection.insertOne(article);
        log.info("____________________________________");
        log.info("Saving new article fulltext with id: " + id);
        log.info("____________________________________");

    }

    public void replace(String id, ArticleContent article) {
        MongoCollection<ArticleContent> collection = database.getCollection(ORIGINAL_COLLECTION, ArticleContent.class);
        collection.replaceOne(Filters.eq("_id", new ObjectId(id)), article);
        log.debug("Saving new article fulltext with id: " + id);
    }
    
    public ArticleContent.ContentType exists(String id) {
        MongoCollection<ArticleContent> collection = database.getCollection(ORIGINAL_COLLECTION, ArticleContent.class);
        ArticleContent.ContentType result = ArticleContent.ContentType.EMPTY;
        // contains sectionList
        Long count = collection.countDocuments(Filters.and(
                Filters.eq("_id", new ObjectId(id)),
                Filters.exists("sectionList")));
        if (count > 0) {
            result = ArticleContent.ContentType.FULLTEXT;
        } else {
            count = collection.countDocuments(Filters.and(
                    Filters.eq("_id", new ObjectId(id)),
                    Filters.exists("rawText")));
            if (count > 0) {
                result = ArticleContent.ContentType.RAWTEXT;
            } else {
                count = collection.countDocuments(Filters.eq("_id", new ObjectId(id)));
                if (count > 0) {
                    result = ArticleContent.ContentType.ABSTRACT;
                }
            }
        }
        log.debug("Fulltext article count: " + result + " for id: " + id);
        return result;
    }

    public Page<ArticleContent> findArticles(Integer page) {

        log.debug("Find articleContent list from collection: " + ORIGINAL_COLLECTION + " page: " + page);

        Integer count = 0;
        MongoCollection<ArticleContentAggregation> collection = database.getCollection(ORIGINAL_COLLECTION, ArticleContentAggregation.class);
        List<Bson> aggregatesList = new ArrayList();
        
        // Order and Pagination
        Facet facetArray[] = new Facet[2];
        facetArray[0] = new Facet("paginatedResults", Aggregates.skip(size * page), Aggregates.limit(size));
        facetArray[1] = new Facet("count", Aggregates.count());

        aggregatesList.add(Aggregates.facet(facetArray));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);

        AggregateIterable<ArticleContentAggregation> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray));
        MongoCursor<ArticleContentAggregation> iterator = resultList.iterator();

        ArticleContentAggregation articlesAggregation = iterator.next();
        if (articlesAggregation.getCount().size() != 0) {
            count = count + articlesAggregation.getCount().get(0).getCount();
        }
        Pageable pageable = of(page, size);

        return new PageImpl(articlesAggregation.getPaginatedResults(), pageable, count);

    }

    public void updateFigure(String id, String field, String label, Category category) {
        log.debug("Updating figure category with id : " + id + " figure field: " + field + " figure label: " + label + " and category: " + category.toString());
        MongoCollection<ArticleContent> collection = database.getCollection(ORIGINAL_COLLECTION, ArticleContent.class);
        Bson match = Filters.and(Filters.eq("_id", new ObjectId(id)),
                Filters.eq(field + ".label", label));
        UpdateResult result = collection.updateOne(match, set(field + ".$.category", category));
        log.debug(result.toString());
    }
    

}
