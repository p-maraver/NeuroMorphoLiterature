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

package org.neuromorpho.literature.release.repository;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import java.util.Arrays;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleRepositoryMongo {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String dbName = "nmotest";
    private static final ArticleRepositoryMongo soleInstance = new ArticleRepositoryMongo();
    MongoDatabase database;

    @Autowired
    public ArticleRepositoryMongo() {
        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase(dbName);

    }
    
    public void dropEmails(String collection) {
        MongoCollection<org.bson.Document> collectionEmails = database.getCollection(collection);
        collectionEmails.drop();
    }
    
    public void findEmails(String collectionName, String outCollection) {

        MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);

        collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$data.authorList"),
                        Aggregates.project(new Document("_id", 0)
                                .append("pmid", "$data.pmid")
                                .append("doi", "$data.doi")
                                .append("usage", "$data.dataUsage")
                                .append("name", "$data.authorList.name")
                                .append("email", "$data.authorList.email")
                                .append("state", collectionName)),
                        Aggregates.match(Filters.exists("email")),
                        Aggregates.out(outCollection)
                )).forEach(printBlock);

    }

    Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

}
