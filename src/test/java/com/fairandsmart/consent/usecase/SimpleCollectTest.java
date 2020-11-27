package com.fairandsmart.consent.usecase;

/*-
 * #%L
 * Right Consent / A Consent Manager Platform
 * 
 * Authors:
 * 
 * Xavier Lefevre <xavier.lefevre@fairandsmart.com> / FairAndSmart
 * Nicolas Rueff <nicolas.rueff@fairandsmart.com> / FairAndSmart
 * Jérôme Blanchard <jerome.blanchard@fairandsmart.com> / FairAndSmart
 * Alan Balbo <alan.balbo@fairandsmart.com> / FairAndSmart
 * Frederic Pierre <frederic.pierre@fairansmart.com> / FairAndSmart
 * Victor Guillaume <victor.guillaume@fairandsmart.com> / FairAndSmart
 * Manon Stremplewski <manon.stremplewski@fairandsmart.com> / FairAndSmart
 * Pauline Kullmann <pauline.kullmmann@fairandsmart.com> / FairAndSmart
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fairandsmart.consent.TestUtils;
import com.fairandsmart.consent.api.dto.ModelEntryDto;
import com.fairandsmart.consent.api.dto.ModelVersionDto;
import com.fairandsmart.consent.api.dto.ModelVersionStatusDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.BasicInfo;
import com.fairandsmart.consent.manager.model.Processing;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SimpleCollectTest {

    private static final Logger LOGGER = Logger.getLogger(SimpleCollectTest.class.getName());
    private static final String TEST_USER = "sheldon";
    private static final String TEST_PASSWORD = "password";
    private static final String SUBJECT = "mmichu";

    private static final String language = "fr";
    private static final String biKey = "sct_bi1";
    private static final String t1Key = "sct_t1";
    private static final String t2Key = "sct_t2";

    /**
     * 1 : le user appel l'url de génération du formulaire de collecte en passant le token contenant le context en paramètre (header ou query)
     *     le sujet est inconnu car il n'y a encore pas eu de collecte précédente, on génère un formulaire vide avec les éléments demandés dans le context et un nouveau token
     * 2 : le user poste le formulaire avec ses réponses et le token de context sur l'url de collecte
     *     tout est bon, un reçu est généré et renvoyé en réponse au user
     * 3 : le user retourne à son url initiale qui devrait être contenue dans le reçu ou dans le context
     *     le sujet est connu car il y a eu une collecte précédente, on génère un formulaire pré-rempli avec les éléments demandés dans le context et un nouveau token
     * 4 : le user poste le formulaire avec ses réponses et le token de context sur l'url de collecte
     *     tout est bon, un nouveau reçu est généré et renvoyé en réponse au user
     */
    @Test
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testSimpleCollect() {
        //INITIAL SETUP
        //Check that the app is running
        LOGGER.log(Level.INFO, "Initial setup");
        given().contentType(ContentType.JSON).
                when().get("/health").
                then().body("status", equalTo("UP"));

        //Generate test elements
        LOGGER.log(Level.INFO, "Generating entries");
        List<String> keys = List.of(biKey, t1Key, t2Key);
        List<String> types = List.of(BasicInfo.TYPE, Processing.TYPE, Processing.TYPE);
        for (int index = 0; index < keys.size(); index++) {
            //Create model
            String key = keys.get(index);
            String type = types.get(index);
            LOGGER.log(Level.INFO, "Creating " + type + " entry");
            ModelEntryDto model = TestUtils.generateModelEntryDto(key, type);
            assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(model).size());
            Response response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(model).
                    when().post("/models");
            response.then().statusCode(200);
            ModelEntryDto entry = response.body().as(ModelEntryDto.class);

            //Create model version
            LOGGER.log(Level.INFO, "Creating " + type + " version");
            ModelVersionDto dto = TestUtils.generateModelVersionDto(key, type, language);
            assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(dto).size());
            response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(dto).
                    when().post("/models/" + entry.getId() + "/versions");
            response.then().statusCode(200);
            dto = response.body().as(ModelVersionDto.class);

            //Activate model version
            LOGGER.log(Level.INFO, "Activating " + type + " version");
            ModelVersionStatusDto statusDto =  new ModelVersionStatusDto();
            statusDto.setStatus(ModelVersion.Status.ACTIVE);
            response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(statusDto).
                    when().put("/models/" + entry.getId() + "/versions/" + dto.getId() + "/status");
            response.then().statusCode(200);
            dto = response.body().as(ModelVersionDto.class);
            assertEquals(ModelVersion.Status.ACTIVE, dto.getStatus());
        }

        LOGGER.log(Level.INFO, "Creating context & token");
        ConsentContext ctx = new ConsentContext()
                .setSubject(SUBJECT)
                .setValidity("P2Y")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setReceiptDisplayType(ConsentContext.ReceiptDisplayType.HTML)
                .setLanguage(language);
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx)
                .when().post("/consents/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token: " + token);

        //PART 1
        //Check consent form
        Response response = given().accept(ContentType.JSON).header("TOKEN", token).when().get("/consents");
        response.then().contentType("application/json").assertThat().statusCode(200);

        response = given().accept(ContentType.HTML).when().get("/consents?t=" + token);
        String page = response.asString();
        response.then().contentType("text/html").assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        //Orientation
        assertFalse(page.contains("class=\"right\"")); // Vertical
        //BasicInfo
        assertTrue(page.contains("Title " + biKey));
        assertTrue(page.contains("Header " + biKey));
        assertTrue(page.contains("href=\"Privacy policy URL " + biKey + "\""));
        assertTrue(page.contains("Footer " + biKey));
        assertTrue(page.contains("accept-all-switch"));
        //Processing 1
        assertTrue(page.contains("Processing title " + t1Key));
        assertTrue(page.contains("Data body " + t1Key));
        assertTrue(page.contains("Retention body " + t1Key));
        assertTrue(page.contains("Usage body " + t1Key));
        assertTrue(page.contains("consent_core_service.png"));
        //Processing 2
        assertTrue(page.contains("Processing title " + t2Key));
        assertTrue(page.contains("Data body " + t2Key));
        assertTrue(page.contains("Retention body " + t2Key));
        assertTrue(page.contains("Usage body " + t2Key));
        assertTrue(page.contains("consent_third_part_sharing.png"));

        Document html = Jsoup.parse(page);
        Map<String, String> values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);
        List<String> elementsKeys = values.keySet().stream().filter(key -> key.startsWith("element")).collect(Collectors.toList());
        assertEquals(2, elementsKeys.size());
        for (String key : elementsKeys) {
            assertEquals("refused", values.get(key));
            values.replace(key, "accepted"); //User accepts every processing
        }

        //PART 2
        //Post user answer
        LOGGER.log(Level.INFO, "Posting user answer");
        Response postResponse = given().contentType(ContentType.URLENC)
                .formParams(values).when().post("/consents");
        String postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent Response page: " + postPage);
        assertTrue(postPage.contains("Merci"));
        assertTrue(postPage.contains("Cliquez ici"));
        assertTrue(postPage.contains("&format=text%2Fhtml"));

        html = Jsoup.parse(postPage);
        Elements links = html.getElementsByTag("a");
        String link = links.get(0).attr("href");

        LOGGER.log(Level.INFO, "Receipt page link: " + link);

        // Check that embed link redirect to the correct receipt mime type (html)
        Response receiptResponse = given().contentType(ContentType.URLENC).when().get(link.replace("%2F", "/"));
        String receiptPage = receiptResponse.asString();
        receiptResponse.then().contentType("text/html").assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Receipt page HTML: " + receiptPage);
        assertTrue(receiptPage.contains("<html"));
        assertTrue(receiptPage.contains("RE&Ccedil;U"));
        assertTrue(receiptPage.contains("<title>RE&Ccedil;U DE CONSENTEMENT</title>"));
        assertTrue(receiptPage.contains("Fran&ccedil;ais (France)"));
        assertTrue(receiptPage.contains("Formulaire web"));
        assertTrue(receiptPage.contains("Data body " + t1Key));
        assertTrue(receiptPage.contains("Data body " + t2Key));
        assertTrue(receiptPage.contains("<h3>Processing title " + t1Key + "</h3>"));
        assertTrue(receiptPage.contains("<h3>Processing title " + t2Key + "</h3>"));
        assertTrue(receiptPage.contains("<div class=\"processing-response accepted \">Accept&eacute;</div>"));
        assertFalse(receiptPage.contains("<div class=\"processing-response accepted \">Refus&eacute;</div>"));
        assertTrue(receiptPage.contains("Accept&eacute;"));
        assertFalse(receiptPage.contains("Refus&eacute;"));
        assertTrue(receiptPage.contains(SUBJECT));

        //PART 3
        //Check previous values are loaded on new consent form
        response = given().accept(ContentType.HTML).when().get("/consents?t=" + token);
        page = response.asString();
        response.then().contentType("text/html").assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        html = Jsoup.parse(page);
        values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);
        elementsKeys = values.keySet().stream().filter(key -> key.startsWith("element")).collect(Collectors.toList());
        assertEquals(2, elementsKeys.size());
        for (String key : elementsKeys) {
            assertEquals("accepted", values.get(key));
            values.replace(key, "refused"); //User refuses every processing
        }

        //PART 4
        //Post user new answer in new context with different receipt mime type
        LOGGER.log(Level.INFO, "Posting new user answer in new context");
        ctx = new ConsentContext()
                .setSubject(SUBJECT)
                .setValidity("P2Y")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setInfo(biKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setReceiptDisplayType(ConsentContext.ReceiptDisplayType.XML)
                .setLanguage(language);
        LOGGER.log(Level.INFO, "New context" + ctx.toString());
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        token = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx)
                .when().post("/consents/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token: " + token);

        //PART 1
        //Check consent form
        response = given().accept(ContentType.HTML).when().get("/consents?t=" + token);
        page = response.asString();
        response.then().contentType("text/html").assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        //Orientation
        assertFalse(page.contains("class=\"right\"")); // Vertical
        //BasicInfo
        assertTrue(page.contains("Title " + biKey));
        assertTrue(page.contains("Header " + biKey));
        assertTrue(page.contains("href=\"Privacy policy URL " + biKey + "\""));
        assertTrue(page.contains("Footer " + biKey));
        assertTrue(page.contains("accept-all-switch"));
        //Processing 1
        assertTrue(page.contains("Processing title " + t1Key));
        assertTrue(page.contains("Data body " + t1Key));
        assertTrue(page.contains("Retention body " + t1Key));
        assertTrue(page.contains("Usage body " + t1Key));
        assertTrue(page.contains("consent_core_service.png"));
        //Processing 2
        assertTrue(page.contains("Processing title " + t2Key));
        assertTrue(page.contains("Data body " + t2Key));
        assertTrue(page.contains("Retention body " + t2Key));
        assertTrue(page.contains("Usage body " + t2Key));
        assertTrue(page.contains("consent_third_part_sharing.png"));

        html = Jsoup.parse(page);
        values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);
        elementsKeys = values.keySet().stream().filter(key -> key.startsWith("element")).collect(Collectors.toList());
        assertEquals(2, elementsKeys.size());
        for (String key : elementsKeys) {
            assertEquals("accepted", values.get(key));
            values.replace(key, "refused"); //User refuse every processing
        }

        postResponse = given().contentType(ContentType.URLENC)
                .formParams(values).when().post("/consents");
        postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent Response page: " + postPage);
        assertTrue(postPage.contains("Merci"));
        assertTrue(postPage.contains("Cliquez ici"));
        assertTrue(postPage.contains("&format=application%2Fxml"));


        html = Jsoup.parse(postPage);
        links = html.getElementsByTag("a");
        link = links.get(0).attr("href");

        // Check that embed link redirect to the correct receipt display type (xml)
        receiptResponse = given().contentType(ContentType.URLENC).when().get(link.replace("%2F", "/"));
        receiptPage = receiptResponse.asString();
        receiptResponse.then().contentType("application/xml").assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Receipt page XML: " + receiptPage);
        assertTrue(receiptPage.contains("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
        assertTrue(receiptPage.contains("<receipt>"));
        assertTrue(receiptPage.contains("<language>fr</language>"));
        assertTrue(receiptPage.contains("<collectionMethod>" + ConsentContext.CollectionMethod.WEBFORM.name() + "</collectionMethod>"));
        assertTrue(receiptPage.contains("<data>Data body " + t1Key + "</data>"));
        assertTrue(receiptPage.contains("<data>Data body " + t2Key + "</data>"));
        assertTrue(receiptPage.contains("<value>refused</value>"));
        assertFalse(receiptPage.contains("<value>accepted</value>"));
        assertTrue(receiptPage.contains("<subject>" + SUBJECT + "</subject>"));
        assertTrue(receiptPage.contains("<name>Name " + biKey + "_dc</name>"));
    }

}
