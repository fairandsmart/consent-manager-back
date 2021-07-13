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
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CollectBaseTest {

    private static final Logger LOGGER = Logger.getLogger(CollectBaseTest.class.getName());
    private static final String TEST_USER = "sheldon";
    private static final String TEST_PASSWORD = "password";
    private static final String SUBJECT = "mmichu";

    private static final String language = "fr";
    private static final String biKey = "cbt_bi1";
    private static final String t1Key = "cbt_t1";
    private static final String t2Key = "cbt_t2";

    /**
     * 1 : Le operator crée une transaction à partir du context de consentement
     *     il fournit l'url de cette transaction au user cible.
     * 2 : le user appelle l'url de la transaction
     *     le sujet est inconnu car il n'y a encore pas eu de collecte précédente, on génère un formulaire vide avec les éléments demandés dans le context et un nouveau token
     * 3 : le user poste le formulaire avec ses réponses sur l'url intégrée au formulaire
     *     aucune confirmation n'est nécessaire, l'utilisateur est redirigé vers l'url de la transaction
     *     la transaction étant terminée, l'utilisateur est redirigé sur le reçu
     * 4 : le user utilise l'url intégrée au reçu pour générer une nouvelle transaction liée au même contexte
     *     il est redirigé vers la nouvelle transaction créée
     *     le sujet est connu car il y a eu une collecte précédente, on génère un formulaire pré-rempli avec les éléments demandés dans le context et un nouveau token
     * 5 : le user poste le formulaire avec ses réponses sur l'url intégrée au formulaire
     *     aucune confirmation n'est nécessaire, l'utilisateur est redirigé vers l'url de la transaction
     *     la transaction étant terminée, l'utilisateur est redirigé sur le reçu
     */
    @Test
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testSimpleCollectHtml() {
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
            ModelVersionStatusDto statusDto = new ModelVersionStatusDto();
            statusDto.setStatus(ModelVersion.Status.ACTIVE);
            response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(statusDto).
                    when().put("/models/" + entry.getId() + "/versions/" + dto.getId() + "/status");
            response.then().statusCode(200);
            dto = response.body().as(ModelVersionDto.class);
            assertEquals(ModelVersion.Status.ACTIVE, dto.getStatus());
        }

        //PART 1
        //Generate initial transaction (as operator)
        LOGGER.log(Level.INFO, "Create transaction from context");
        ConsentContext ctx = new ConsentContext()
                .setSubject(SUBJECT)
                .setValidity("P2Y")
                .setLayoutData(TestUtils.generateFormLayout(biKey, Arrays.asList(t1Key, t2Key)).withOrientation(FormLayout.Orientation.VERTICAL).withDesiredReceiptMimeType(MediaType.TEXT_HTML).withAcceptAllVisible(true))
                .setLanguage(language);
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        Response response = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx).when().post("/consents");
        response.then().header("location", containsStringIgnoringCase("/consents")).assertThat().statusCode(201);
        String txLocation = response.getHeader("location");
        LOGGER.log(Level.INFO, "Transaction URI: " + txLocation);

        //PART 2
        //Call consent form (enduser)
        LOGGER.log(Level.INFO, "Consult transaction location in HTML, should redirect to consent form view");
        response = given().accept(ContentType.HTML).when().get(txLocation);
        String page = response.asString();
        response.then().contentType(ContentType.HTML).assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Consent form page: " + page);

        //Orientation
        assertFalse(page.contains("class=\"right\"")); // Vertical
        //BasicInfo
        assertTrue(page.contains("Title " + biKey));
        assertTrue(page.contains("Header " + biKey));
        assertTrue(page.contains("Privacy policy label " + biKey));
        assertTrue(page.contains("Footer " + biKey));
        assertTrue(page.contains("accept-all-switch"));
        //Processing 1
        assertTrue(page.contains("Processing title " + t1Key));
        assertTrue(page.contains("Data body " + t1Key));
        assertTrue(page.contains("Retention body " + t1Key));
        assertTrue(page.contains("Usage body " + t1Key));
        assertTrue(page.contains("Service principal"));
        //Processing 2
        assertTrue(page.contains("Processing title " + t2Key));
        assertTrue(page.contains("Data body " + t2Key));
        assertTrue(page.contains("Retention body " + t2Key));
        assertTrue(page.contains("Usage body " + t2Key));
        assertTrue(page.contains("Partage à des tierces-parties"));

        Document html = Jsoup.parse(page);
        String action = TestUtils.extractFormAction(html);
        LOGGER.log(Level.INFO, "Form Action: " + action);
        Map<String, String> values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);
        List<String> elementsKeys = values.keySet().stream().filter(key -> key.startsWith("element")).collect(Collectors.toList());
        assertEquals(2, elementsKeys.size());
        for (String key : elementsKeys) {
            assertEquals("refused", values.get(key));
            values.replace(key, "accepted"); //User accepts every processing
        }

        //PART 3
        //Post consent answers (enduser)
        String postUrl = txLocation.substring(0, txLocation.indexOf("?"));
        LOGGER.log(Level.INFO, "Post URL: " + postUrl);
        Response postResponse = given().accept(ContentType.HTML).contentType(ContentType.URLENC)
                .formParams(values).when().post(postUrl + "/" + action);
        String postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent Submit Response page: " + postPage);

        assertTrue(postPage.contains("Merci"));
        assertTrue(postPage.contains("Voir le reçu"));
        html = Jsoup.parse(postPage);
        Elements receiptlinks = html.getElementsByAttributeValue("id", "receipt-link");
        String receiptLink = receiptlinks.get(0).attr("href");

        LOGGER.log(Level.INFO, "Receipt page link: " + receiptLink);

        // Check that embed link redirect to the correct receipt mime type (html)
        Response receiptResponse = given().contentType(ContentType.URLENC).when().get(receiptLink.replace("%2F", "/"));
        String receiptPage = receiptResponse.asString();
        receiptResponse.then().contentType(ContentType.HTML).assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Receipt page HTML: " + receiptPage);
        assertTrue(receiptPage.contains("<html"));
        assertTrue(receiptPage.contains("RE&Ccedil;U"));
        assertTrue(receiptPage.contains("<title>RE&Ccedil;U DE CONSENTEMENT</title>"));
        assertTrue(receiptPage.contains("Fran&ccedil;ais (France)"));
        assertTrue(receiptPage.contains("Data body " + t1Key));
        assertTrue(receiptPage.contains("Data body " + t2Key));
        assertTrue(receiptPage.contains("<h3 class=\"element-title\">Processing title " + t1Key + "</h3>"));
        assertTrue(receiptPage.contains("<h3 class=\"element-title\">Processing title " + t2Key + "</h3>"));
        assertTrue(receiptPage.contains("<div class=\"processing-response accepted\">Accept&eacute;</div>"));
        assertFalse(receiptPage.contains("<div class=\"processing-response accepted\">Refus&eacute;</div>"));
        assertTrue(receiptPage.contains(SUBJECT));

        //PART 4
        //Generate new transaction from previous one
        LOGGER.log(Level.INFO, "Breed the transaction for changing user choices");
        Elements breedlinks = html.getElementsByAttributeValue("id", "breed-link");
        String breedlink = breedlinks.get(0).attr("href");
        LOGGER.log(Level.INFO, "Breed TX Link: " + breedlink);
        response = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx).when().post(breedlink);
        response.then().header("location", containsStringIgnoringCase("/consents")).assertThat().statusCode(201);
        String newTxLocation = response.getHeader("location");

        //Get the new transaction submit form
        response = given().accept(ContentType.HTML).when().get(newTxLocation);
        page = response.asString();
        response.then().contentType(ContentType.HTML).assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Consent form page: " + page);

        html = Jsoup.parse(page);
        action = TestUtils.extractFormAction(html);
        LOGGER.log(Level.INFO, "Form Action: " + action);
        values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);
        elementsKeys = values.keySet().stream().filter(key -> key.startsWith("element")).collect(Collectors.toList());
        assertEquals(2, elementsKeys.size());
        for (String key : elementsKeys) {
            assertEquals("accepted", values.get(key));
            values.replace(key, "refused"); //User refuses every processing
        }

        //PART 5
        //Post consent answers (enduser)
        postUrl = newTxLocation.substring(0, newTxLocation.indexOf("?"));
        LOGGER.log(Level.INFO, "Post URL: " + postUrl);
        postResponse = given().accept(ContentType.HTML).contentType(ContentType.URLENC)
                .formParams(values).when().post(postUrl + "/" + action);
        postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent Submit Response page: " + postPage);

        assertTrue(postPage.contains("Merci"));
        assertTrue(postPage.contains("Voir le reçu"));
    }

}
