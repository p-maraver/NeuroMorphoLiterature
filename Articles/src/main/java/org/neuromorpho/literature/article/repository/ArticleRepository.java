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

package org.neuromorpho.literature.article.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.article.exceptions.DuplicatedException;
import org.neuromorpho.literature.article.model.article.Article;
import org.neuromorpho.literature.article.model.article.ArticleData;
import org.neuromorpho.literature.article.model.article.Contact;
import org.neuromorpho.literature.article.model.article.ReconstructionsStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.springframework.data.domain.PageRequest.of;

@Repository
public class ArticleRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MongoDatabase database;
    private final Integer size = 50;

    @Autowired
    public ArticleRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();

        database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);
    }


    //Searches article by Id in all the collections
    public Article findById(ObjectId id) {
        List<Bson> aggregatesList = new ArrayList();
        aggregatesList.add(Aggregates.match(Filters.eq("_id", id)));

        //search among collections
        for (Article.ArticleStatus status : Article.ArticleStatus.values()) {
            MongoCollection<Article> collection = database.getCollection(status.getCollection(), Article.class);
            Article article = collection.aggregate(aggregatesList).first();
            if (article != null) {
                article.setStatus(status);
                return article;
            }
        }
        return null;
    }

//    public Article findByIdAndEmail(ObjectId id, String email) {
//        List<Bson> aggregatesList = new ArrayList();
//        aggregatesList.add(Aggregates.match(Filters.and(
//                Filters.eq("_id", id),
//                Filters.eq("data.authorList.emailList", email))));
//
//        //search among collections
//        for (Article.ArticleStatus status : Article.ArticleStatus.values()) {
//            MongoCollection<Article> collection = database.getCollection(status.getCollection(), Article.class);
//            Article article = collection.aggregate(aggregatesList).first();
//            if (article != null) {
//                article.setStatus(status);
//                return article;
//            }
//        }
//        return null;
//    }


    public Map<String, Long> getSummary(String dateStr) {

        Map<String, Long> articlesNumbers = new HashMap();
        if (dateStr != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.EVALUATED.getCollection(), Document.class);
            Bson match = Filters.lt("data.publishedDate", date);
            Long articles = collection.countDocuments(match);
            articlesNumbers.put("Evaluated.old", articles);

            match = Filters.gte("data.publishedDate", date);
            articles = collection.countDocuments(match);
            articlesNumbers.put("Evaluated.new", articles);
            collection = database.getCollection(Article.ArticleStatus.TO_EVALUATE.getCollection(), Document.class);
            match = Filters.lt("data.publishedDate", date);
            articles = collection.countDocuments(match);
            articlesNumbers.put("Pending evaluation.old", articles);

            match = Filters.gte("data.publishedDate", date);
            articles = collection.countDocuments(match);
            articlesNumbers.put("Pending evaluation.new", articles);
        }
        for (Article.ArticleStatus status : Article.ArticleStatus.values()) {
            MongoCollection<Document> collection = database.getCollection(status.getCollection(), Document.class);
            Long articles = collection.countDocuments();
            articlesNumbers.put(status.getStatus(), articles);
        }

        // get distinct values for dataUsage
        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.POSITIVE.getCollection(), Document.class);
        for (ArticleData.DataUsage usage : ArticleData.DataUsage.values()) {
            Bson match = Filters.eq("data.dataUsage", usage.toString());
            Long articles = collection.countDocuments(match);
            articlesNumbers.put(usage.getUsage(), articles);
        }
        return articlesNumbers;
    }

    public List<Object> getSummaryByDataUsage() {
        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.POSITIVE.getCollection());

        List<Bson> aggregatesList = new ArrayList();
        aggregatesList.add(Aggregates.group("$data.dataUsage",
                Accumulators.sum("count", 1)));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);

        AggregateIterable<Document> result = collection.aggregate(
                Arrays.asList(aggregatesArray));
        return StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());
    }

    /*
     * Updates the collection
     */
    public void updateCollection(Article article, Article.ArticleStatus status) {

        try {
            MongoCollection<Article> destCollection = database.getCollection(status.getCollection(), Article.class);
            destCollection.insertOne(article);

            MongoCollection<Article> originCollection = database.getCollection(article.getStatus().getCollection(), Article.class);
            originCollection.deleteOne(Filters.eq("_id", article.getId()));
        } catch (Exception ex) {
            log.error("Error saving article :" + article.toString(), ex);
            throw ex;
        }

    }

    /*
     * Update the given fields of an article
     */
    public void updateArticle(String collectionName, ObjectId id, String field, Object object) {

        try {
            log.debug("Updating article id: " + id + " field: " + field + ": " + object);

            if (field.equals("data")) { //check for a duplicate
                Article article = new Article(id, (ArticleData) object);
                Article duplicate = this.findArticle(article);
                if (duplicate != null) {
                    throw new DuplicatedException(duplicate.getStatus(), duplicate.getId());
                }
            }
            Bson match = Filters.eq("_id", id);
            MongoCollection<Article> collection = database.getCollection(collectionName, Article.class);
            collection.updateOne(match, set(field, object));

        } catch (Exception ex) {
            log.error("Error: ", ex);
            throw new RuntimeException(ex);
        }
    }

    public void updateContacts(Contact contact) {
        Article.ArticleStatus[] collectionList = Article.ArticleStatus.values();
        for (Article.ArticleStatus articleStatus : collectionList) {
            this.updateContacts(articleStatus.getCollection(), contact);
        }
    }

    private void updateContacts(String collectionName, Contact contact) {

        MongoCollection<Article> collection = database.getCollection(collectionName, Article.class);
        collection.updateMany(
                Filters.in("data.authorList.contactId", contact.getReplacedListId()),
                combine(
                        set("data.authorList.$[element].contactId", contact.getId()),
                        set("data.authorList.$[element].name", contact.getFirstName() + ' ' + contact.getLastName())),
                new UpdateOptions()
                        .arrayFilters(
                                Arrays.asList(Filters.in("element.contactId", contact.getReplacedListId()))));
    }

    /*
     * Removes article
     */
    public void delete(ObjectId id) {
        Article article = this.findById(id);
        log.debug("Removing article: " + id.toString() + " from collection: " + article.getStatus().getCollection());
        MongoCollection<Document> collection = database.getCollection(article.getStatus().getCollection());
        collection.deleteOne(Filters.eq("_id", id));
    }


    /*
     * Reconstructions summary
     */
    public List<ArticleSummary> getSummary(Boolean expired) {
        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.POSITIVE.getCollection());

        List<Bson> aggregatesList = new ArrayList();
        Bson match = Filters.eq("data.dataUsage", ArticleData.DataUsage.DESCRIBING_NEURONS.toString());
        aggregatesList.add(Aggregates.match(match));

        if (expired) {
            match = Filters.lt("reconstructions.currentStatusList.expirationDate", new Date());
            aggregatesList.add(Aggregates.match(match));
        }
        aggregatesList.add(Aggregates.project(Projections.fields(
                Projections.include("reconstructions.currentStatusList.specificDetails", "reconstructions.currentStatusList.nReconstructions"),
                Projections.computed("nStatus", new Document("$size", new Document("$ifNull", Arrays.asList("$reconstructions.currentStatusList", new ArrayList())))))));
        aggregatesList.add(Aggregates.unwind("$reconstructions.currentStatusList"));
        aggregatesList.add(Aggregates.group("$reconstructions.currentStatusList.specificDetails",
                Accumulators.sum("nReconstructions", "$reconstructions.currentStatusList.nReconstructions"),
                Accumulators.sum("nArticles", new Document("$divide", Arrays.asList(1, "$nStatus")))));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);

        AggregateIterable<Document> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray));
        List<ArticleSummary> summaryList = new ArrayList();
        MongoCursor<Document> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            ArticleSummary summary = new ArticleSummary(document);
            summaryList.add(summary);
        }
        return summaryList;

    }


    public List<ArticleSummary> getSummaryShared() {


        MongoCollection<Document> collection = database.getCollection(Article.ArticleStatus.POSITIVE.getCollection());

        List<Bson> aggregatesList = new ArrayList();
        Bson match = Filters.and(Filters.eq("data.dataUsage", ArticleData.DataUsage.SHARING.toString()),
                Filters.ne("data.dataUsage", ArticleData.DataUsage.DESCRIBING_NEURONS.toString()));
        aggregatesList.add(Aggregates.match(match));

        aggregatesList.add(Aggregates.lookup(Article.ArticleStatus.POSITIVE.getCollection(),
                "sharedList.sharedId", "_id", "sharedInfo"));

        aggregatesList.add(Aggregates.project(Projections.fields(
                Projections.computed("status", "$sharedInfo.reconstructions.currentStatusList.specificDetails"))));

        aggregatesList.add(Aggregates.project(Projections.fields(
                Projections.include("status"),
                Projections.computed("nStatus1", new Document("$size", new Document("$ifNull", Arrays.asList("$status", new ArrayList())))))));

        aggregatesList.add(Aggregates.unwind("$status"));

        aggregatesList.add(Aggregates.project(Projections.fields(
                Projections.include("status"),
                Projections.computed("nStatus", new Document("$sum",
                        Arrays.asList("$nStatus1", -1, new Document("$size",
                                new Document("$ifNull", Arrays.asList("$status", new ArrayList())))))))));

        aggregatesList.add(Aggregates.unwind("$status"));

        aggregatesList.add(Aggregates.group("$status",
                Accumulators.sum("nSharedArticles", new Document("$divide", Arrays.asList(1, "$nStatus")))));


        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);

        AggregateIterable<Document> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray));
        List<ArticleSummary> summaryList = new ArrayList();
        MongoCursor<Document> iterator = resultList.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            ArticleSummary summary = new ArticleSummary(document);
            summaryList.add(summary);
        }
        return summaryList;

    }


    public Page<Article> findArticlesByQuery(Article.ArticleStatus status, Map<String, String> query) {
        Integer size = 50;
        if (query.get("size") != null) {
            size = Integer.parseInt(query.get("size"));
            query.remove("size");
        }
        Integer page = 0;
        if (query.get("page") != null) {
            page = Integer.parseInt(query.get("page"));
            query.remove("page");
        }

        log.debug("Find article list from collection: " + status + " page: " + page);

        ArticlesAggregation articlesAggregation;
        Integer count = 0;
        List<Article> articleList = new ArrayList();
        if (status.equals(Article.ArticleStatus.ALL)) {
            for (Article.ArticleStatus articleStatus : Article.ArticleStatus.values()) {
                articlesAggregation = this.findAggregationByQuery(articleStatus, size / 6, page, query);
                if (articlesAggregation.getCount().size() != 0) {
                    count = count + articlesAggregation.getCount().get(0).getCount();
                    articleList.addAll(articlesAggregation.getPaginatedResults());
                }
            }
        } else {
            articlesAggregation = this.findAggregationByQuery(status, size, page, query);
            if (articlesAggregation.getCount().size() != 0) {
                count = count + articlesAggregation.getCount().get(0).getCount();
                articleList.addAll(articlesAggregation.getPaginatedResults());
            }
        }

        Pageable pageable = of(page, size);

        return new PageImpl(articleList, pageable, count);

    }


    private ArticlesAggregation findAggregationByQuery(Article.ArticleStatus status, Integer size, Integer
            page, Map<String, String> query) {

        MongoCollection<ArticlesAggregation> collection = database.getCollection(status.getCollection(), ArticlesAggregation.class);
        List<Bson> aggregatesList = new ArrayList();

        if (query.get("sortProperty") != null) {
            Bson sort = Sorts.ascending(query.get("sortProperty"));
            if (query.get("sortDirection").equals("DESC")) {
                sort = Sorts.descending(query.get("sortProperty"));
            }
            aggregatesList.add(Aggregates.sort(sort));
            query.remove("sortProperty");
            query.remove("sortDirection");

        }

        for (Map.Entry<String, String> entry : query.entrySet()) {
            Bson match;
            if (entry.getKey().equals("text")) {
                if (!entry.getValue().equals("")) {
                    List<String> valueList = new ArrayList<>();
                    if (entry.getValue().contains("OR")) {
                        String[] splited = entry.getValue().split(" OR ");
                        valueList = Arrays.asList(splited);
                    } else {
                        valueList.add(entry.getValue());
                    }
                    List<Pattern> valueListCaseInsensitive = valueList.stream().map(s -> Pattern.compile(s, Pattern.CASE_INSENSITIVE)).collect(Collectors.toList());
                    List<Pattern> valueListStartEndCaseInsensitive = valueList.stream().map(s -> Pattern.compile("^" + s + "$", Pattern.CASE_INSENSITIVE)).collect(Collectors.toList());

                    match = Filters.or(
                            Filters.in("data.pmid", valueList),
                            Filters.in("data.pmcid", valueList),
                            Filters.in("data.doi", valueListStartEndCaseInsensitive),
//                            Filters.in("data.authorList.emailList", valueListStartEndCaseInsensitive),
                            Filters.in("data.title", valueListCaseInsensitive),
                            Filters.in("data.authorList.name", valueListCaseInsensitive));
                    aggregatesList.add(Aggregates.match(match));
                }
            } else if (entry.getKey().toLowerCase().contains("date")) {
                String[] entries = ((String) entry.getValue()).split(":");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
                LocalDate date1 = LocalDate.parse(entries[1], formatter);
                if (entries[0].equals("lt")) {
                    match = Filters.lt(entry.getKey(), date1);
                } else {
                    match = Filters.gte(entry.getKey(), date1);
                }
                aggregatesList.add(Aggregates.match(match));
            } else if (entry.getKey().toLowerCase().contains("classifier")) {
                if (entry.getValue().equals("false")) {
                    match = Filters.or(
                            Filters.exists(entry.getKey(), Boolean.FALSE),
                            Filters.eq("metadata.articleStatus", "Pending evaluation"));
                    aggregatesList.add(Aggregates.match(match));
                } else if (entry.getValue().equals("true")) {
                    match = Filters.exists(entry.getKey());
                    aggregatesList.add(Aggregates.match(match));
                } else if (entry.getValue().equals("Review")) {
                    match = Filters.or(
                            Filters.regex(entry.getKey(), "^Positive L"),
                            Filters.regex(entry.getKey(), "^Negative L"),
                            Filters.regex(entry.getKey(), "^Pending evaluation"));
                    aggregatesList.add(Aggregates.match(match));
                } else {
                    match = Filters.regex(entry.getKey(), "^" + entry.getValue());
                    aggregatesList.add(Aggregates.match(match));
                }
            } else if (entry.getKey().toLowerCase().contains("identifiers")) {
                if (entry.getValue().equals("false")) {
                    match = Filters.not(Filters.exists("data.pmcid"));
                    aggregatesList.add(Aggregates.match(match));
                    match = Filters.not(Filters.exists("data.doi"));
                    aggregatesList.add(Aggregates.match(match));
                    match = Filters.not(Filters.exists("data.pdfLink"));
                    aggregatesList.add(Aggregates.match(match));
                    match = Filters.ne("data.fulltext", "FULLTEXT");
                    aggregatesList.add(Aggregates.match(match));
                    match = Filters.ne("data.fulltext", "RAWTEXT");
                    aggregatesList.add(Aggregates.match(match));
                } else {
                    match = Filters.or(Filters.exists("data.pmcid"),
                            Filters.exists("data.doi"),
                            Filters.exists("data.pdfLink"),
                            Filters.eq("data.fulltext", "FULLTEXT"),
                            Filters.eq("data.fulltext", "RAWTEXT"));
                    aggregatesList.add(Aggregates.match(match));
                }
            } else if (entry.getKey().contains("fulltext")) {
                if (entry.getValue().equals("false")) {
                    match = Filters.and(Filters.not(Filters.eq("data.fulltext", "FULLTEXT")),
                            Filters.not(Filters.eq("data.fulltext", "RAWTEXT")));
                } else if (entry.getValue().equals("true")) {
                    match = Filters.or(
                            Filters.eq("data.fulltext", "FULLTEXT"),
                            Filters.eq("data.fulltext", "RAWTEXT"));
                } else {
                    match = Filters.eq("data.fulltext", "FULLTEXT");
                }
                aggregatesList.add(Aggregates.match(match));
            } else if (entry.getKey().toLowerCase().contains("contactid")) {
                match = Filters.eq(entry.getKey(), new ObjectId(entry.getValue()));
                aggregatesList.add(Aggregates.match(match));
            } else if (entry.getKey().toLowerCase().contains("metadata.negativeifnoanswer")) {
                match = Filters.or(
                        Filters.eq(entry.getKey(), Boolean.parseBoolean(entry.getValue())),
                        Filters.exists(entry.getKey(), Boolean.FALSE));
                aggregatesList.add(Aggregates.match(match));
            } else if (entry.getKey().equals("reconstructions") && entry.getValue().equals("Available")) {
                match = Filters.or(
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "BOUNCED"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "NO_CONTACT_INFORMATION"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "NO_RESPONSE"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "REQUEST_DECLINED"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "LOST_DATA"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "COMMUNICATION_STOPPED"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "IN_PROCESSING_PIPELINE"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "IN_RELEASE"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "IN_REPOSITORY"),
                        Filters.eq("reconstructions.currentStatusList.specificDetails", "ON_HOLD")
                );
                aggregatesList.add(Aggregates.match(match));
            } else if (entry.getKey().contains("Id")) {
                match = Filters.eq(entry.getKey(), new ObjectId(entry.getValue()));
                aggregatesList.add(Aggregates.match(match));
            } else {
                if (entry.getKey().contains("specificDetails")) {
                    entry.setValue(ReconstructionsStatus.SpecificDetails.getSpecificDetails((String) entry.getValue()).toString());
                }
                match = Filters.eq(entry.getKey(), entry.getValue());
                aggregatesList.add(Aggregates.match(match));
            }

        }

        aggregatesList.add(Aggregates.addFields(new Field("status", status.toString())));

        aggregatesList.add(Aggregates.lookup(Article.ArticleStatus.POSITIVE.getCollection(),
                "sharedList.sharedId", "_id", "sharedInfo"));

        // Order and Pagination
        Facet facetArray[] = new Facet[2];
        facetArray[0] = new Facet("paginatedResults", Aggregates.skip(size * page), Aggregates.limit(size));
        facetArray[1] = new Facet("count", Aggregates.count());
        Sort.Direction direction = Sort.Direction.ASC;


        aggregatesList.add(Aggregates.facet(facetArray));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);


        AggregateIterable<ArticlesAggregation> resultList = collection.aggregate(
                Arrays.asList(aggregatesArray)).allowDiskUse(true);
        MongoCursor<ArticlesAggregation> iterator = resultList.iterator();

        ArticlesAggregation articlesAggregation = iterator.next();
        return articlesAggregation;

    }


    public Article findArticle(Article article) {

        List<Bson> matchOrList = new ArrayList<>();

        //PMID and PMCID are unique
        if (article.getData().getPmid() != null || article.getData().getPmcid() != null) {
            List<Bson> aggregatesList = new ArrayList();

            if (article.getData().getPmid() != null) {
                if (article.getId() != null) { //not myself
                    matchOrList.add(Filters.and(Filters.eq("data.pmid", article.getData().getPmid()),
                            Filters.ne("_id", article.getId())));
                } else {
                    matchOrList.add(Filters.eq("data.pmid", article.getData().getPmid()));
                }
            }
            if (article.getData().getPmcid() != null) {
                if (article.getId() != null) { //not myself
                    matchOrList.add(Filters.and(Filters.eq("data.pmcid", article.getData().getPmcid()),
                            Filters.ne("_id", article.getId())));
                } else {
                    matchOrList.add(Filters.eq("data.pmcid", article.getData().getPmcid()));
                }
            }
            Bson match;
            Iterable<Bson> iterableOr = matchOrList;
            match = Filters.or(iterableOr);
            aggregatesList.add(Aggregates.match(match));


            Bson[] aggregatesArray = new Bson[aggregatesList.size()];
            aggregatesArray = aggregatesList.toArray(aggregatesArray);

            for (Article.ArticleStatus status : Article.ArticleStatus.values()) {
                MongoCollection<Article> collection = database.getCollection(status.getCollection(), Article.class);

                AggregateIterable<Article> resultList = collection.aggregate(
                        Arrays.asList(aggregatesArray));
                MongoCursor<Article> iterator = resultList.iterator();
                if (iterator.hasNext()) {
                    Article duplicatedArticle = iterator.next();
                    duplicatedArticle.setStatus(status);
                    return duplicatedArticle;
                }
            }
        }
        //DOI not always unique
        if (article.getData().getDoi() != null) {
            List<Bson> aggregatesList = new ArrayList();
            Bson match;
            Pattern regex = Pattern.compile("^" + article.getData().getDoi() + "$", Pattern.CASE_INSENSITIVE);
            if (article.getId() != null) { //not myself
                match = Filters.and(Filters.eq("data.doi", regex),
                        Filters.ne("_id", article.getId()));
            } else {
                match = Filters.eq("data.doi", regex);
            }
            aggregatesList.add(Aggregates.match(match));
            Bson[] aggregatesArray = new Bson[aggregatesList.size()];
            aggregatesArray = aggregatesList.toArray(aggregatesArray);

            for (Article.ArticleStatus status : Article.ArticleStatus.values()) {
                MongoCollection<Article> collection = database.getCollection(status.getCollection(), Article.class);

                AggregateIterable<Article> resultList = collection.aggregate(
                        Arrays.asList(aggregatesArray));
                MongoCursor<Article> iterator = resultList.iterator();
                if (iterator.hasNext()) {
                    Article duplicatedArticle = iterator.next();
                    duplicatedArticle.setStatus(status);
                    return duplicatedArticle;
                }
            }


        }
        for (Article.ArticleStatus status : Article.ArticleStatus.values()) {
            MongoCollection<Article> collection = database.getCollection(status.getCollection(), Article.class);

            // If not exact matching, approximate matching & return the 2 most similar
            collection.createIndex(Indexes.text("data.title"));
            FindIterable<Article> resultList;
            if (article.getId() != null) { //not myself
                resultList = collection.find(
                                Filters.and(Filters.ne("_id", article.getId()),
                                        Filters.text(article.getData().getTitle())))
                        .projection(Projections.metaTextScore("score"))
                        .sort(Sorts.metaTextScore("score"));
            } else {
                resultList = collection.find(
                                Filters.text(article.getData().getTitle()))
                        .projection(Projections.metaTextScore("score"))
                        .sort(Sorts.metaTextScore("score"));
            }
            MongoCursor<Article> iterator = resultList.iterator();
            if (iterator.hasNext()) {
                Article duplicatedArticle = iterator.next();
                if (article.similarData(duplicatedArticle)) {
                    duplicatedArticle.setStatus(status);
                    return duplicatedArticle;
                }
            }
        }
        return null;

    }

    public void save(Article article, Article.ArticleStatus status) {
        log.debug("Saving new article: " + status.getCollection());
        Article oldArticle = this.findArticle(article);
        if (oldArticle != null) {
            log.debug("Duplicated article found in collection: " + oldArticle.getStatus().getCollection());
            log.debug("Duplicated article:" + oldArticle.toString());
            throw new DuplicatedException(oldArticle.getStatus(), oldArticle.getId());
        }
        MongoCollection<Article> collection = database.getCollection(status.getCollection(), Article.class);
        collection.insertOne(article);
        log.debug("Article saved ");
    }

    public ObjectId saveOrUpdate(Article article, Article.ArticleStatus status) {
        Article oldArticle = this.findArticle(article);
        ObjectId id;
        if (oldArticle != null) {
            id = oldArticle.getId();
            Article newArticle = article;
            List<Bson> updatesList = new ArrayList();
            if ((newArticle.hasDoi() && !oldArticle.hasDoi())
                    || (oldArticle.isRxivJournal() && !newArticle.isRxivJournal())) {
                log.debug("Updating doi to: " + newArticle.getData().getDoi());
                updatesList.add(set("data.doi", newArticle.getData().getDoi()));
            }
            if (oldArticle.isRxivJournal() && !newArticle.isRxivJournal()) {
                log.debug("Updating title to: " + newArticle.getData().getTitle());
                updatesList.add(set("data.title", newArticle.getData().getTitle()));
            }
            if (newArticle.hasPmid() && !oldArticle.hasPmid()) {
                log.debug("Updating pmid to: " + newArticle.getData().getPmid());
                updatesList.add(set("data.pmid", newArticle.getData().getPmid()));
            }
            if (newArticle.hasPmcid() && !oldArticle.hasPmcid()) {
                log.debug("Updating pmcid to: " + newArticle.getData().getPmcid());
                updatesList.add(set("data.pmcid", newArticle.getData().getPmcid()));
            }
            if ((newArticle.hasPublishedDate() && !oldArticle.hasPublishedDate())
                    || (oldArticle.isRxivJournal() && !newArticle.isRxivJournal())) {
                log.debug("Updating publishedDate to: " + newArticle.getData().getPublishedDate());
                updatesList.add(set("data.publishedDate", newArticle.getData().getPublishedDate()));
            }
            if ((newArticle.hasJournal() && !oldArticle.hasJournal())
                    || (oldArticle.isRxivJournal() && !newArticle.isRxivJournal())) {
                log.debug("Updating journal to: " + newArticle.getData().getJournal());
                updatesList.add(set("data.journal", newArticle.getData().getJournal()));
            }
            if ((oldArticle.countEmailList() == 0)
                    || (oldArticle.isRxivJournal() && !newArticle.isRxivJournal())) {
                log.debug("Updating authorList to: " + newArticle.getData().getAuthorList().toString());
                updatesList.add(set("data.authorList", newArticle.getAuthorList()));
            }
            if (!updatesList.isEmpty()) {
                log.debug("Updating article with id: " + id);
                MongoCollection<Article> collection = database.getCollection(oldArticle.getStatus().getCollection(), Article.class);
                Bson match = Filters.eq("_id", id);
                collection.updateOne(match, Updates.combine(updatesList));
            }

        } else {
            log.debug("Saving new article: " + article.toString());

            article.setOcDate();
            article.setId(new ObjectId());
            MongoCollection<Article> collection = database.getCollection(status.getCollection(), Article.class);
            collection.insertOne(article);
            id = article.getId();
            log.debug("Saved new article with id: " + id);
        }
        return id;
    }

    public List<String> getMetadataDistinctValues(String key) {
        MongoCollection collection = database.getCollection(
                Article.ArticleStatus.POSITIVE.getCollection());
        DistinctIterable<String> result = collection.distinct("metadata." + key, String.class);

        return StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());
    }

    public void updateMetadataValue(
            String key,
            String oldValue,
            Set<String> newValue) {
        log.debug("Updating metadata key : " + key + " to value: " + newValue);

        MongoCollection collection = database.getCollection(
                Article.ArticleStatus.POSITIVE.getCollection());

        Bson match = Filters.eq("metadata." + key, oldValue);
        collection.updateMany(match, set("metadata." + key, newValue));
    }

}
