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
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.BasicInfo;
import com.fairandsmart.consent.manager.model.FormLayout;
import com.fairandsmart.consent.manager.model.Processing;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class FormErrorTest {

    private static final Logger LOGGER = Logger.getLogger(FormErrorTest.class.getName());
    private static final String TEST_USER = "sheldon";
    private static final String TEST_PASSWORD = "password";
    private static final String SUBJECT = "mmichu";

    private static final String language = "fr";
    private static final String biKey = "fet_bi1";
    private static final String t1Key = "fet_t1";
    private static final String t2Key = "fet_t2";

    @Test
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testFormError() {
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
        Map<String, String> ids = new HashMap<>();
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
            ids.put(key, entry.getId());

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
            ModelVersionStatusDto statusDto = new ModelVersionStatusDto();
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
                .setLanguage(language)
                .setOrigin(ConsentContext.Origin.WEBFORM)
                .setLayoutData(new FormLayout().withOrientation(FormLayout.Orientation.VERTICAL).withInfo(biKey).withElements(Arrays.asList(t1Key, t2Key)).withExistingElementsVisible(true).withAcceptAllVisible(true).withDesiredReceiptMimeType(MediaType.TEXT_HTML));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        Response response = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx).when().post("/consents");
        response.then().header("location", containsStringIgnoringCase("/consents")).assertThat().statusCode(201);
        String txLocation = response.getHeader("location");
        String token = txLocation.substring(txLocation.indexOf("="));
        LOGGER.log(Level.INFO, "Transaction URI: " + txLocation);

        //Test submission of bad values
        response = given().accept(ContentType.HTML).when().get(txLocation);
        String page = response.asString();
        response.then().contentType(ContentType.HTML).assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Consent form page: " + page);

        Document html = Jsoup.parse(page);
        String action = TestUtils.extractFormAction(html);
        LOGGER.log(Level.INFO, "Form Action: " + action);
        Map<String, String> values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);
        List<String> elementsKeys = values.keySet().stream().filter(key -> key.startsWith("element")).collect(Collectors.toList());
        assertEquals(2, elementsKeys.size());
        for (String key : elementsKeys) {
            assertEquals("refused", values.get(key));
            values.replace(key, "incorrect"); //User accepts every processing
        }
        String postUrl = txLocation.substring(0, txLocation.indexOf("?"));
        Response postResponse = given().accept(ContentType.HTML).contentType(ContentType.URLENC)
                .formParams(values).when().post(postUrl + "/" + action);
        postResponse.then().assertThat().statusCode(500);

        // TODO add tests based on the new workflow

        /*//Test invalid token (expired token is hard to generate)
        response = given().accept(ContentType.HTML).when().get(txLocation + "NotH3re");
        page = response.asString();
        response.then().contentType(ContentType.HTML).assertThat().statusCode(500);
        LOGGER.log(Level.INFO, "Consent form error page: " + page);
        assertTrue(page.contains("Jeton invalide"));

        response = given().accept(ContentType.HTML).header("Accept-Language", "en").when().get(txLocation + "NotH3re");
        page = response.asString();
        response.then().contentType(ContentType.HTML).assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Consent form error page: " + page);
        assertTrue(page.contains("Invalid token"));*/

        //DELETE ENTRY t1KEY
        given().auth().basic(TEST_USER, TEST_PASSWORD).when().delete("/models/" + ids.get(t1Key));

        //Check consent form with embedded error
        response = given().accept(ContentType.HTML).header("Accept-Language", "en").when().get(txLocation);
        page = response.asString();
        response.then().contentType(ContentType.HTML).assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form error page: " + page);
        assertTrue(page.contains("Unable to generate consent form"));

        //TODO test submission
    }

}
