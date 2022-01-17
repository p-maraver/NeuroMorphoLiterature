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

package org.neuromorpho.literature.agenda.service;


import org.neuromorpho.literature.agenda.communication.MetadataCommunication;
import org.neuromorpho.literature.agenda.repository.EmailRepository;
import org.neuromorpho.literature.agenda.repository.TemplateRepository;
import org.neuromorpho.literature.agenda.repository.contacts.ContactRepository;
import org.bson.types.ObjectId;
import org.neuromorpho.literature.agenda.communication.ArticleCommunication;
import org.neuromorpho.literature.agenda.communication.Metadata;
import org.neuromorpho.literature.agenda.communication.Article;
import org.neuromorpho.literature.agenda.model.Config;
import org.neuromorpho.literature.agenda.model.Contact;
import org.neuromorpho.literature.agenda.model.Email;
import org.neuromorpho.literature.agenda.repository.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EmailsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${emailCC}")
    private String[] cc;
    @Value("${name}")
    private String name;


    @Value("${host_send}")
    private String hostSend;
    @Value("${host_read}")
    private String hostRead;
    @Value("${port_send}")
    private Integer portSend;
    @Value("${port_read}")
    private String portRead;

    @Autowired
    private MetadataCommunication metadataCommunication;

    @Autowired
    private ArticleCommunication articleCommunication;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private ContactRepository contactRepository;
    
    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private ConfigRepository configRepository;


    public Email generateEmail(Article article, String emailType, String type) {
        Email email = new Email();

        log.debug("Preparing email: " + emailType);
        Email template = templateRepository.findOneByType(emailType);
        List<Metadata> tracingSystemList = new ArrayList<>();
        for (String tracingSystem : article.getTracingSystemList()) {
            tracingSystemList.add(metadataCommunication.findMetadataByName(tracingSystem));
        }
        EmailContent emailContent = new EmailContent(article, tracingSystemList, type, template.getSubject());
        email.setIdArticle(article.getId());
        email.setType(emailType);
        email.setIdArticle(article.getId());
        Set<String> emailList = new HashSet<>();
        for (ObjectId objectId: article.getContactListId()){
            Contact contact = contactRepository.findContact(objectId);
            emailList.addAll(contact.getEmailSet());
        }
        email.setTo(new ArrayList<>(emailList));
        email.setCc(Arrays.asList(cc));

        Map<String, String> model = new HashMap();
        if (article.isDissertation()) {
            model.put("authors", "");
            model.put("publishedYear", "");
            model.put("journal", "");
            model.put("publication", emailContent.getPublication()
                    + emailContent.getPublishedYear() + " "
                    + emailContent.getJournal());
        } else {
            model.put("authors", emailContent.getAuthors());
            model.put("publishedYear", emailContent.getPublishedYear());
            model.put("journal", emailContent.getJournal());
            model.put("publication", emailContent.getPublication());

        }

        model.put("type", emailContent.getType());
        model.put("glia", emailContent.getGlia());
        model.put("glial", emailContent.getGlial());

        model.put("dears", emailContent.getDears());
        model.put("title", emailContent.getTitle());
        model.put("date", emailContent.getWaitingDate());
        model.put("tracingSystem", emailContent.getTracingSystem());
        model.put("tracingSystemFormat", emailContent.getTracingSystemFormat());
        model.put("subjectType", emailContent.getSubjectType());
        model.put("publicationType", emailContent.getPublicationType());

        String text = template.getContent();
        for (Map.Entry<String, String> entry : model.entrySet()) {
            text = text.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        email.setContent(text);
        String subject = template.getSubject();
        for (Map.Entry<String, String> entry : model.entrySet()) {
            subject = subject.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        //capitalize first
        subject = subject.substring(0, 1).toUpperCase() + subject.substring(1);
        //cpitaliz after :
        if (subject.contains(":")) {
            Integer indexOfDot = subject.indexOf(":");
            subject = subject.substring(0, indexOfDot + 2) + subject.substring(indexOfDot + 2, indexOfDot + 3).toUpperCase() + subject.substring(indexOfDot + 3);
        }
        email.setSubject(subject);

        return email;
    }

    public List<Email> findEmail(String id) {
        return emailRepository.find(id);
    }

    public void sendEmail(Email email) throws MessagingException, AddressException {

        Config config = configRepository.find();
        log.debug("Sending email: " + email.toString());
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", hostSend);
        props.put("mail.smtp.from", config.getUsername());


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getUsername(), config.getPassword());
                    }
                });
        Message message = new MimeMessage(session);

        InternetAddress[] toList = new InternetAddress[email.getTo().size()];
        for (int i = 0; i < email.getTo().size(); i++) {
            toList[i] = new InternetAddress(email.getTo().get(i));
        }
        message.setRecipients(
                Message.RecipientType.TO, toList);
        message.setFrom(new InternetAddress(config.getUsername()));
        InternetAddress[] ccList = new InternetAddress[email.getCc().size()];
        for (int i = 0; i < email.getCc().size(); i++) {
            ccList[i] = new InternetAddress(email.getCc().get(i));
        }
        message.setRecipients(
                Message.RecipientType.CC, ccList);

        message.setContent(email.getContent(), "text/html");
        message.setSubject(email.getSubject());

        log.debug("Sending email ....");
        Transport.send(message);
        log.debug("Email sent ....");

        email.setFrom(config.getUsername());
        email.setSentDate(LocalDate.now());
        emailRepository.save(email);

    }

    public void extractBouncedFrom(String folder) {
        try {
            Config config = configRepository.find();

            //create properties field
            Properties props = new Properties();
            props.setProperty("mail.host", hostRead);
            props.setProperty("mail.port", portRead);
            props.setProperty("mail.transport.protocol", "imaps");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(config.getUsername(), config.getPassword());
                        }
                    });
            Store store = session.getStore("imaps");
            store.connect();
            LocalDate date = LocalDate.of(2020, 1, 1);
            
            SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, 
                    Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            Folder inbox = store.getFolder(folder);
            //create the folder object and open it
            inbox.open(Folder.READ_ONLY);
            // retrieve the messages from the folder in an array and print it
            Message[] messages = inbox.search(newerThan);
            for (int i = 0, n = messages.length; i < n; i++) {
                try {
                    Message message = messages[i];
                    log.debug("---------------------------------");
                    log.debug("Reading message  ---" + i);
                    log.debug("Date: " + message.getReceivedDate());
                    log.debug("Subject: " + message.getSubject());
                    log.debug("From: " + message.getFrom()[0]);
                    
                    if (//message.getReceivedDate().getYear() > 2017 && 
                            (message.getFrom()[0].toString().toLowerCase().contains("mail delivery") ||
                                    message.getFrom()[0].toString().toLowerCase().contains("microsoft outlook") ||
                                    message.getFrom()[0].toString().toLowerCase().contains("postmaster@"))) {
                        if (message.getFrom()[0].toString().toLowerCase().contains("microsoft outlook")) {
                            log.debug("Type of text: " + message.getContentType());
                        }

                        String content = this.getTextFromMessage(message);
                        log.debug("Text: " + content);

                        Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(content);
                        Set<String> emailList = new HashSet<>();
                        while (m.find()) {
                            emailList.add(m.group());
                            log.debug("Email address extracted from text: " + m.group());
                        }
                        List<Email> sentEmailList = emailRepository.findByEmail(emailList);
                        Set<String> resultEmailList = new HashSet<>();
                        Set<String> articleIdList = new HashSet<>();
                        for (Email email : sentEmailList) {
                            articleIdList.add(email.getIdArticle().toString());
                            resultEmailList.addAll(email.getTo());
                        }
                        if (resultEmailList.size() == 1) {
                            Contact contact = contactRepository.update2Bounced((String) resultEmailList.toArray()[0]);
                            for (String id : articleIdList) {
                                // check if there is a new email for the bounced author
                                String reconstructionsStatus = "Bounced";
                                if (contact.existNotBounced()){
                                    reconstructionsStatus = "Not bounced";
                                } 
                                articleCommunication.update2Status(id, reconstructionsStatus);
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("Error updating bounced for message: ", e);
                }

            }

            //close the store and folder objects
            inbox.close(false);
            store.close();

        } catch (Exception e) {
            log.error("Error", e);
        }
    }

    private String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart, result);
        }
        return result;
    }

    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart, String result) throws MessagingException, IOException {
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            log.debug("Type of text: " + bodyPart.getContentType());
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                result = (String) bodyPart.getContent();
            } else if (bodyPart.isMimeType("multipart/*")) {
                result = this.getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent(), result);
            } else if (bodyPart.isMimeType("application/octet-stream")) {
                result = result + "\n" + new BufferedReader(new InputStreamReader(bodyPart.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
            }
        }
        return result;
    }

    public void generateAndSendEmail(Article article, String emailType, String type) throws MessagingException {
        Email email = this.generateEmail(article, emailType, type);
        this.sendEmail(email);
    }
}
