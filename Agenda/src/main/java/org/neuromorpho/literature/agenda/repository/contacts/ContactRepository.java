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

package org.neuromorpho.literature.agenda.repository.contacts;

import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.agenda.model.Contact;
import org.neuromorpho.literature.agenda.model.ContactEmail;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.springframework.data.domain.PageRequest.of;

@Repository
public class ContactRepository {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private MongoDatabase database;
    @Value("${spring.data.mongodb.collection.contact}")
    private String COLLECTION_NAME;

    @Autowired
    public ContactRepository(
            @Value("${spring.data.mongodb.database}") String DBNAME) {

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = new MongoClient();
        database = mongoClient.getDatabase(DBNAME).withCodecRegistry(pojoCodecRegistry);

    }

//    public void updateContact(Author author) {
//        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
//        List<Bson> matchOrList = new ArrayList<>();
//        if (author.getEmailList() != null) {
//
//            // check if the email is valid
//            FindIterable<Contact> resultList = collection.find(Filters.in("emailList", author.getEmailList()));
//            MongoCursor<Contact> iterator = resultList.iterator();
//            Contact contact;
//            if (iterator.hasNext()) {
//                log.debug("Updating contact " + author.toString());
//                //update contact
//                contact = iterator.next();
//                contact.update(author);
//                collection.findOneAndReplace(Filters.eq("_id", contact.getId()), contact);
//            } else { //insert contact
//                log.debug("Saving contact " + author.toString());
//                contact = new Contact(author);
//                collection.insertOne(contact);
//            }
//
//        }
//    }

    public Page<Contact> findContactList(Integer pageIndex, Integer pageSize, String text) {
        MongoCollection<ContactAggregation> collection = database.getCollection(COLLECTION_NAME, ContactAggregation.class);
        List<Bson> aggregatesList = new ArrayList();

        if (text != null) {
            Bson match = Filters.or(
                    Filters.regex("emailList.email", "^" + text + "$", "i"),
                    Filters.regex("firstName", "^" + text + "$", "i"),
                    Filters.regex("lastName", "^" + text + "$", "i"));
            aggregatesList.add(Aggregates.match(match));
        }
        Bson sort = Sorts.ascending("emailList.email");
        aggregatesList.add(Aggregates.sort(sort));

        // Order and Pagination
        Facet facetArray[] = new Facet[2];
        facetArray[0] = new Facet("paginatedResults", Aggregates.skip(pageSize * pageIndex), Aggregates.limit(pageSize));
        facetArray[1] = new Facet("count", Aggregates.count());

        aggregatesList.add(Aggregates.facet(facetArray));

        Bson[] aggregatesArray = new Bson[aggregatesList.size()];
        aggregatesArray = aggregatesList.toArray(aggregatesArray);

        AggregateIterable<ContactAggregation> resultList = collection.aggregate(Arrays.asList(aggregatesArray));
        MongoCursor<ContactAggregation> iterator = resultList.iterator();

        ContactAggregation contactAggregation = iterator.next();
        Pageable pageable = of(pageIndex, pageSize);
        if (contactAggregation.getCount().size() == 0) {
            return new PageImpl(contactAggregation.getPaginatedResults(), pageable, 0);
        }
        return new PageImpl(contactAggregation.getPaginatedResults(), pageable, contactAggregation.getCount().get(0).getCount());
    }

    public void updateContact(String id,
                              Contact contact) {
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        collection.replaceOne(Filters.eq("_id", new ObjectId(id)), contact);
       
    }

    public Contact findContact(ObjectId id) {
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        return collection.find(Filters.eq("_id", id)).first();
    }

    public List<Contact> findContactList(Contact contact){
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        FindIterable<Contact> resultList = collection.find(Filters.in("emailList.email", contact.getEmailSet()));
        List<Contact> contactList = StreamSupport.stream(resultList.spliterator(), false).collect(Collectors.toList());
        return contactList;
    }
    public Contact addContact(Contact contact) {
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        collection.insertOne(contact);
        return this.findContactList(contact).get(0);
    }
    public void delete(List<ObjectId> contactIdList) {
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        collection.deleteMany(Filters.in("_id", contactIdList));
    }
    
    public void update(ObjectId id, String field, Object value) {
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        collection.updateOne(Filters.eq("_id", id),
                Updates.set(field, value));
    }

    public Contact update2Bounced(String email) {
        log.debug("Updating to bounced contact with email: " + email);
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        Contact contact = collection.findOneAndUpdate(
                Filters.and(Filters.eq("emailList.email", email), Filters.eq("emailList.email", email)),
                set("emailList.$.bounced", true),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));

        return contact;
    }

    public void addEmail(String id, String email) {
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        Contact contact = collection.find(Filters.and(
                Filters.ne("_id", new ObjectId(id)),
                Filters.eq("emailList.email", email))).first();
        if (contact == null) { // merge
            ContactEmail contactEmail = new ContactEmail(email, false);
            collection.updateOne(Filters.eq("_id", new ObjectId(id)),
                    Updates.push("emailList", contactEmail));
        }
    }

    public List<ObjectId> deleteByEmail(String id, List<String> emailList) {
        MongoCollection<Contact> collection = database.getCollection(COLLECTION_NAME, Contact.class);
        List<ObjectId> removeIdList = new ArrayList<>();
        for (String email: emailList){
            Contact contact = collection.findOneAndDelete(Filters.and(
                    Filters.ne("_id", new ObjectId(id)),
                    Filters.eq("emailList.email", email)));
            if (contact != null){
                removeIdList.add(contact.getId());
            }
        }
        log.debug("Removed merging: " + removeIdList.toString());
        return removeIdList;
    }
   
}
