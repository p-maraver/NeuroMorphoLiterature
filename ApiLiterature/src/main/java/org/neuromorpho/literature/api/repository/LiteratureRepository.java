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

package org.neuromorpho.literature.api.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.neuromorpho.literature.api.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Literature Repository, queries to DB. Spring MVC Pattern
 */
@Repository
public class LiteratureRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private MongoDatabase database;

    Integer size = 50;

    @Autowired
    public LiteratureRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME) {

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();

        database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);

    }


    /**
     * Count articles by Global Status
     */
    public HashMap<String, Integer> countGlobalStatusArticles() {
        HashMap<String, Integer> result = new HashMap();

        List<Bson> aggregatesList = new ArrayList();

        aggregatesList.add(Aggregates.match(Filters.or(
                Filters.eq("data.dataUsage", "DESCRIBING_NEURONS"),
                Filters.eq("data.dataUsage", "SHARING"))));
        aggregatesList.add(Aggregates.group("$reconstructions.globalStatus",
                Accumulators.sum("nArticles", 1)));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);
        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.POSITIVE.getCollection());

        AggregateIterable<Document> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray));
        MongoCursor<Document> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            log.debug("Document: " + document);
            String globalStatus = document.getString("_id");
            if (globalStatus != null) {
                result.put(globalStatus, document.getInteger("nArticles"));
            }
        }
        return result;

    }

    /**
     * Count reconstructions by Global Status
     */
    public HashMap<String, Double> countGlobalStatusReconstructions() {
        HashMap<String, Double> result = new HashMap();

        List<Bson> aggregatesList = new ArrayList();
        aggregatesList.add(Aggregates.match(Filters.or(
                Filters.eq("data.dataUsage", "DESCRIBING_NEURONS"),
                Filters.eq("data.dataUsage", "SHARING"))));
        aggregatesList.add(Aggregates.project(Projections.fields(
                Projections.include("reconstructions.currentStatusList.specificDetails", "reconstructions.currentStatusList.nReconstructions"),
                Projections.computed("nStatus", new Document("$size", new Document("$ifNull", Arrays.asList("$reconstructions.currentStatusList", new ArrayList())))))));
        aggregatesList.add(Aggregates.unwind("$reconstructions.currentStatusList"));
        aggregatesList.add(Aggregates.group("$reconstructions.currentStatusList.specificDetails",
                Accumulators.sum("nReconstructions", "$reconstructions.currentStatusList.nReconstructions")));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);

        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.POSITIVE.getCollection());
        AggregateIterable<Document> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray));
        MongoCursor<Document> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            log.debug("Document: " + document);
            result.put(document.getString("_id"), document.getDouble("nReconstructions"));
        }
        return result;

    }

    /**
     * Count negative articles
     */
    public Long countNegatives() {
        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.NEGATIVE.getCollection());
        return collection.countDocuments();
    }

    /**
     * Return years for which there are publications
     */
    public Set<Integer> getPublishedYears() {
        Set<Integer> result = new HashSet();

        List<Bson> aggregatesList = new ArrayList();
        Bson match = Filters.eq("data.dataUsage", "DESCRIBING_NEURONS");

        aggregatesList.add(Aggregates.match(match));
        aggregatesList.add(Aggregates.project(
                Projections.fields(
                        Projections.computed(
                                "year",
                                new Document("$year", "$data.publishedDate")))));
        aggregatesList.add(Aggregates.group("$year", Accumulators.sum("nArticles", 1.0)));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);
        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.POSITIVE.getCollection());

        AggregateIterable<Document> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray));
        MongoCursor<Document> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            //log.debug("Document: " + document);
            if (document.getInteger("_id") != null) {
                result.add(document.getInteger("_id"));
            }

        }
        return result;
    }

    /**
     * Return the articles page by query
     */
    public Page<Article> findPublicationsByQuery(Integer page, HashMap<String, String> query) {
        List<Bson> aggregatesList = new ArrayList();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            Bson match;
            if (entry.getKey().contains("publishedDate")) {
                try {
                    aggregatesList.add(Aggregates.addFields(new Field("year", new Document("year", "$data.publishedDate"))));

                    Date from = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue() + "-01-01");
                    Date to = new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue() + "-12-31");
                    match = Filters.gte(entry.getKey(), from);
                    aggregatesList.add(Aggregates.match(match));
                    match = Filters.lte(entry.getKey(), to);
                    aggregatesList.add(Aggregates.match(match));
                } catch (ParseException e) {
                    log.error("Exception parsing date", e);
                }
            } else if (query.containsValue("DESCRIBING_NEURONS")) {
                match = Filters.or(
                        Filters.eq("data.dataUsage", "DESCRIBING_NEURONS"),
                        Filters.eq("data.dataUsage", "SHARING"));
                aggregatesList.add(Aggregates.match(match));
            } else {
                match = Filters.eq(entry.getKey(), entry.getValue());
                aggregatesList.add(Aggregates.match(match));
            }
        }


        ArticlesAggregation articlesAggregation = this.searchCollection(page, Article.ArticleStatus.POSITIVE, aggregatesList);

        if (articlesAggregation.getCount().size() == 0) {
            articlesAggregation = this.searchCollection(page, Article.ArticleStatus.NEGATIVE, aggregatesList);
        }
        Integer count = 0;
        List<Article> articleList = new ArrayList();

        if (articlesAggregation.getCount().size() != 0) {
            count = count + articlesAggregation.getCount().get(0).getCount();
            articleList.addAll(articlesAggregation.getPaginatedResults());
        }


        Pageable pageable = new PageRequest(page, size);

        return new PageImpl<>(articleList, pageable, count);

    }

    /**
     * Search in a specific collection for articles by query
     */
    private ArticlesAggregation searchCollection(Integer page, Article.ArticleStatus searchCollection, List<Bson> aggregatesList) {

        aggregatesList.add(Aggregates.addFields(new Field("status", searchCollection.toString())));
        aggregatesList.add(Aggregates.lookup(Article.ArticleStatus.POSITIVE.getCollection(),
                "sharedList.sharedId", "_id", "sharedInfoList"));

        Facet facetArray[] = new Facet[2];
        facetArray[0] = new Facet("paginatedResults", Aggregates.skip(size * page), Aggregates.limit(size));
        facetArray[1] = new Facet("count", Aggregates.count());
        aggregatesList.add(Aggregates.facet(facetArray));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);


        MongoCollection<ArticlesAggregation> collection = database.getCollection(searchCollection.getCollection(), ArticlesAggregation.class);
        AggregateIterable<ArticlesAggregation> resultList = collection.aggregate(Arrays.asList(aggregatesArray));
        MongoCursor<ArticlesAggregation> iterator = resultList.iterator();
        ArticlesAggregation articlesAggregation = iterator.next();
        return articlesAggregation;

    }


}
