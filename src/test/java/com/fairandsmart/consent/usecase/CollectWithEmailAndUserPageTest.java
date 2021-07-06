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
import com.fairandsmart.consent.common.config.ClientConfig;
import com.fairandsmart.consent.common.exception.UnexpectedException;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.BasicInfo;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.manager.model.FormLayout;
import com.fairandsmart.consent.manager.model.Processing;
import com.fairandsmart.consent.profile.AlternateProfile;
import com.fairandsmart.consent.token.*;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.Validation;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestProfile(AlternateProfile.class)
public class CollectWithEmailAndUserPageTest {

    private static final Logger LOGGER = Logger.getLogger(CollectWithEmailAndUserPageTest.class.getName());
    private static final String TEST_USER = "sheldon";
    private static final String TEST_PASSWORD = "password";
    private static final String SUBJECT = "mmichu";

    private static final String language = "fr";
    private static final String biKey = "cweupt_bi1";
    private static final String t1Key = "cweupt_t1";
    private static final String t2Key = "cweupt_t2";
    private static final String eKey = "cweupt_e1";
    private static final String recipient = "mmichu@localhost";

    @Inject
    MockMailbox mailbox;

    @Inject
    ClientConfig clientConfig;

    @Inject
    TokenService tokenService;

    /**
     * 1 : l'orga génère un context de collecte qui contient entre autres un email de notification
     * 2 : Le user (anonyme) appelle une URL avec le context en paramètre (header ou query param),
     * 3 : le user (anonyme) poste le formulaire avec ses réponses sur une autre URL
     * 4 : le user (anonyme) reçoit un email de confirmation avec un lien vers la page user
     */
    @Test
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testCollectWithEmail() throws InterruptedException, MalformedURLException, InvalidTokenException, TokenExpiredException, UnexpectedException {
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

        //PART 1
        //Use basic consent context for first generation
        LOGGER.log(Level.INFO, "Creating context & token");
        ConsentContext ctx = new ConsentContext()
                .setSubject(SUBJECT)
                .setValidity("P2Y")
                .setLanguage(language)
                .setLayoutData(TestUtils.generateFormLayout(biKey, Arrays.asList(t1Key, t2Key)).withOrientation(FormLayout.Orientation.VERTICAL).withNotification(eKey))
                .setNotificationRecipient(recipient);
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx)
                .when().post("/tokens").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token : " + token);

        //PART 2
        Response response = given().accept(ContentType.HTML).when().get("/consents?t=" + token);
        String page = response.asString();
        response.then().contentType(ContentType.HTML).assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        Document html = Jsoup.parse(page);
        Map<String, String> values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);

        //PART 3
        LOGGER.log(Level.INFO, "Posting user answer");
        Response postResponse = given().accept(ContentType.HTML).contentType(ContentType.URLENC).formParams(values).when().post("/consents");
        postResponse.then().assertThat().statusCode(200);

        //PART 4
        Thread.sleep(5000);
        LOGGER.log(Level.INFO, "Checking email");
        assertTrue(mailbox.getTotalMessagesSent() > 0);
        List<Mail> sent = mailbox.getMessagesSentTo(recipient);
        assertEquals(1, sent.size());
        assertEquals("Subject " + eKey, sent.get(0).getSubject());
        String received = sent.get(0).getHtml();
        assertTrue(received.contains("Title " + eKey));
        assertTrue(received.contains("Body " + eKey));
        assertTrue(received.contains("Footer " + eKey));
        assertTrue(received.contains("Signature " + eKey));
        assertFalse(sent.get(0).getAttachments().isEmpty());
        assertEquals("Sender " + eKey, sent.get(0).getFrom());

        html = Jsoup.parse(received);
        Optional<Element> notificationLink = html.select("a[href]").stream().filter(l -> l.id().equals("form-url")).findFirst();
        if (notificationLink.isPresent()) {
            URL url = new URL(notificationLink.get().attr("href"));
            assertTrue(url.toString().startsWith(clientConfig.userPagePublicUrl().get()));
            String stoken = URLDecoder.decode(url.getQuery().substring(url.getQuery().indexOf("=") + 1), StandardCharsets.UTF_8);
            AccessToken accessToken = tokenService.readToken(stoken);
            assertEquals(SUBJECT, accessToken.getSubject());
        } else {
            fail("notificationLink link not found");
        }
    }

}
