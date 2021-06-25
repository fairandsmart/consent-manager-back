package com.fairandsmart.consent.usecase;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.TestUtils;
import com.fairandsmart.consent.api.dto.ModelEntryDto;
import com.fairandsmart.consent.api.dto.ModelVersionDto;
import com.fairandsmart.consent.api.dto.ModelVersionStatusDto;
import com.fairandsmart.consent.api.dto.TransactionDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.BasicInfo;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.manager.model.FormLayout;
import com.fairandsmart.consent.manager.model.Processing;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.Validation;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CollectWithConfirmationButtonTest {

    private static final Logger LOGGER = Logger.getLogger(CollectWithConfirmationButtonTest.class.getName());
    private static final String TEST_USER = "sheldon";
    private static final String TEST_PASSWORD = "password";

    private static final String language = "fr";
    private static final String biKey = "cwct_bi1";
    private static final String t1Key = "cwct_t1";
    private static final String t2Key = "cwct_t2";
    private static final String eKey = "cwct_e1";
    private static final String recipient = "mmichu@localhost";

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "consent.public.url")
    String publicUrl;

    @BeforeEach
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void init() {
        //SETUP
        LOGGER.log(Level.INFO, "Initial setup");
        //Check that the app is running
        given().contentType(ContentType.JSON).
                when().get("/health").
                then().body("status", equalTo("UP"));

        //Purge mailbox
        LOGGER.log(Level.INFO, "Purge mailbox");
        mailbox.clear();
        assertEquals(mailbox.getTotalMessagesSent(), 0);

        //Generate test elements
        LOGGER.log(Level.INFO, "Generating entries");
        List<String> keys = List.of(biKey, t1Key, t2Key, eKey);
        List<String> types = List.of(BasicInfo.TYPE, Processing.TYPE, Processing.TYPE, Email.TYPE);
        for (int index = 0; index < keys.size(); index++) {
            //Create model
            String key = keys.get(index);
            String type = types.get(index);
            LOGGER.log(Level.INFO, "Creating " + type + " entry");
            ModelEntryDto entryDto = TestUtils.generateModelEntryDto(key, type);
            assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(entryDto).size());
            Response response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(entryDto).
                    when().post("/models");
            response.then().statusCode(200);
            entryDto = response.body().as(ModelEntryDto.class);

            //Create model version
            LOGGER.log(Level.INFO, "Creating " + type + " version");
            ModelVersionDto versionDto = TestUtils.generateModelVersionDto(key, type, language);
            assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(versionDto).size());
            response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(versionDto).
                    when().post("/models/" + entryDto.getId() + "/versions");
            response.then().statusCode(200);
            versionDto = response.body().as(ModelVersionDto.class);

            //Activate model version
            LOGGER.log(Level.INFO, "Activating " + type + " version");
            ModelVersionStatusDto statusDto =  new ModelVersionStatusDto();
            statusDto.setStatus(ModelVersion.Status.ACTIVE);
            response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(statusDto).
                    when().put("/models/" + entryDto.getId() + "/versions/" + versionDto.getId() + "/status");
            response.then().statusCode(200);
            versionDto = response.body().as(ModelVersionDto.class);
            assertEquals(ModelVersion.Status.ACTIVE, versionDto.getStatus());
        }
    }

    /**
     * Confirmation HTML Simple (bouton avec secret)
     *
     * 1 : l'orga démarre une transaction de collecte avec un context configuré pour une confirmation HTML
     * 2 : Le user (anonyme) consulte l'URL du formulaire attaché à la transaction
     * 3 : le user (anonyme) poste les réponses au formulaire
     * 4 : le user (anonyme) confirm le reçu en postant le secret contenu dans le bouton "je confirme"
     * 5 : le user (anonyme) visualise le reçu
     */
    @Test
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testCollectWithEmail() {
        //PART 1
        LOGGER.log(Level.INFO, "Creating context & token");
        ConsentContext ctx = new ConsentContext()
                .setSubject("mmichu@localhost")
                .setValidity("P2Y")
                .setLanguage(language)
                .setLayoutData(TestUtils.generateFormLayout(biKey, Arrays.asList(t1Key, t2Key)).withOrientation(FormLayout.Orientation.VERTICAL).withNotification(eKey))
                .setConfirmation(ConsentContext.Confirmation.FORM_CODE)
                .setConfirmationConfig(Collections.singletonMap("button_secret", "tagada54"))
                .setNotificationRecipient(recipient);
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        TransactionDto transaction = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx)
                .when().post("/consents").as(TransactionDto.class);
        assertNotNull(transaction);
        LOGGER.log(Level.INFO, "Transaction :" + transaction);

        //PART 2
        Response response = given().accept(ContentType.JSON).when().get(transaction.getActionURI());
        String formPage = response.asString();
        response.then().contentType(ContentType.JSON.toString()).assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + formPage);
        Document form = Jsoup.parse(formPage);
        Map<String, String> values = TestUtils.readFormInputs(form);
        LOGGER.log(Level.INFO, "Form Values: " + values);

        /*
        //PART 3
        LOGGER.log(Level.INFO, "Posting user answer");
        Response consentResponse = given().accept(ContentType.HTML).contentType(ContentType.URLENC).formParams(values).when().post("/consents/values");
        String confirmPage = consentResponse.asString();
        consentResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Confirmation page: " + confirmPage);
        Document confirmation = Jsoup.parse(confirmPage);
        Map<String, String> confirmationValues = TestUtils.readConfirmationInputs(confirmation);
        LOGGER.log(Level.INFO, "Confirmation Values: " + values);

        //PART 4
        LOGGER.log(Level.INFO, "Posting confirmation answer");
        Response confirmationResponse = given().accept(ContentType.HTML).contentType(ContentType.URLENC).formParams(values).when().post("/consents/confirm");
        String receiptPage = confirmationResponse.asString();
        confirmationResponse.then().assertThat().statusCode(200);

        //PART 5
        Document receipt = Jsoup.parse(receiptPage);
        //TODO Check that receipt contains confirmation content
         */
    }

}
