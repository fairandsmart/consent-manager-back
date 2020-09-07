package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.TestUtils;
import com.fairandsmart.consent.api.dto.CreateModelDto;
import com.fairandsmart.consent.api.dto.ContentDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Treatment;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import java.util.*;
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

    private static final String locale = "fr_FR";
    private static final String hKey = "sct_h1";
    private static final String t1Key = "sct_t1";
    private static final String t2Key = "sct_t2";
    private static final String fKey = "sct_f1";

    @BeforeEach
    public void setup() {
        //Check that the app is running
        given().contentType(ContentType.JSON).
                when().get("/health").
                then().body("status", equalTo("UP"));

        //Generate test elements
        List<String> keys = List.of(hKey, t1Key, t2Key, fKey);
        List<String> types = List.of(Header.TYPE, Treatment.TYPE, Treatment.TYPE, Footer.TYPE);
        for (int index = 0; index < keys.size(); index++) {
            //Create model
            CreateModelDto model = TestUtils.generateCreateModelDto(keys.get(index), types.get(index));
            assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(model).size());
            Response response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(model).
                    when().post("/models");
            response.then().statusCode(200);
            ModelEntry entry = response.body().as(ModelEntry.class);

            //Create model version
            ContentDto content = TestUtils.generateContentDto(keys.get(index), types.get(index), locale);
            assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(content).size());
            response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.JSON).body(content).
                    when().post("/models/" + entry.id + "/versions");
            response.then().statusCode(200);
            ModelVersion version = response.body().as(ModelVersion.class);

            //Activate model version
            response = given().auth().basic(TEST_USER, TEST_PASSWORD).
                    contentType(ContentType.TEXT).body(ModelVersion.Status.ACTIVE).
                    when().put("/models/" + entry.id + "/versions/" + version.id + "/status");
            response.then().statusCode(200);
            version = response.body().as(ModelVersion.class);
            assertEquals(ModelVersion.Status.ACTIVE, version.status);
        }
    }

    /**
     * 1 : l'orga génère un context de collecte
     * le context contient le nom du user (subject) et les ids des traitements
     * 2 : le user (anonyme) appelle une URL avec le context en paramètre (header ou query param),
     * cas 1 : sujet inconnu car pas de collecte précédente, on génère un formulaire vide avec les éléments demandés dans le context et un nouveau token
     * 3 : le user (anonyme) poste le formulaire avec ses réponses sur une autre URL
     * cas 1 : tout est bon, un reçu est généré et renvoyé en réponse au user
     * 4 : le user retourne à son url initiale qui devrait être contenue dans le reçu ou dans le context
     * 5 : le user rappelle l'URL avec le même context
     * cas 2 : sujet connu car collecte précédente, on génère un formulaire pré-rempli avec les éléments demandés dans le context et un nouveau token
     * 6 : le user (anonyme) poste le formulaire avec ses réponses sur l'autre URL
     * cas 1 : tout est bon, un nouveau reçu est généré et renvoyé en réponse au user
     */
    @Test
    public void testSimpleCollect() {
        //PART 1
        //Use basic consent context for first generation
        String user = "mmichu";
        ConsentContext ctx = new ConsentContext()
                .setSubject(user)
                .setValidity("P2Y")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader(hKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setFooter(fKey)
                .setLocale(locale);
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx)
                .when().post("/consents/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token: " + token);

        //PART 2
        //Check consent form
        Response response = given().header("TOKEN", token).when().get("/consents");
        String page = response.asString();
        response.then().contentType("text/html").assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        //Orientation
        assertFalse(page.contains("class=\"right\"")); // Vertical
        //Header
        assertTrue(page.contains("Title " + hKey));
        assertTrue(page.contains("Body " + hKey));
        assertTrue(page.contains("href=\"Privacy policy URL " + hKey + "\""));
        //Treatment 1
        assertTrue(page.contains("Treatment title " + t1Key));
        assertTrue(page.contains("Data body " + t1Key));
        assertTrue(page.contains("Retention body " + t1Key));
        assertTrue(page.contains("Usage body " + t1Key));
        assertTrue(page.contains("consent_core_service.png"));
        //Treatment 2
        assertTrue(page.contains("Treatment title " + t2Key));
        assertTrue(page.contains("Data body " + t2Key));
        assertTrue(page.contains("Retention body " + t2Key));
        assertTrue(page.contains("Usage body " + t2Key));
        assertTrue(page.contains("consent_third_part_sharing.png"));
        //Footer
        assertTrue(page.contains("Body " + fKey));
        assertTrue(page.contains("accept-all-switch"));

        Document html = Jsoup.parse(page);
        Map<String, String> values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);
        List<String> elementsKeys = values.keySet().stream().filter(key -> key.startsWith("element")).collect(Collectors.toList());
        assertEquals(2, elementsKeys.size());
        for (String key : elementsKeys) {
            assertEquals("refused", values.get(key));
            values.replace(key, "accepted"); //User accepts every treatment
        }

        //PART 3
        //Post user answer
        Response postResponse = given().contentType(ContentType.URLENC).
                formParams(values).when().post("/consents");
        String postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Receipt page: " + postPage);
        assertTrue(postPage.contains("Reçu"));
        assertTrue(postPage.contains("Français"));
        assertTrue(postPage.contains(ConsentContext.CollectionMethod.WEBFORM.name()));
        assertTrue(postPage.contains("Data body " + t1Key));
        assertTrue(postPage.contains("Data body " + t2Key));
        assertTrue(postPage.contains("Accepté"));
        assertFalse(postPage.contains("Refusé"));
        assertTrue(postPage.contains(user));
        assertTrue(postPage.contains("Name " + hKey + "_dc"));

        //PART 4
        //TODO

        //PART 5
        //Check previous values are loaded on new consent form
        response = given().header("TOKEN", token).when().get("/consents");
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
            values.replace(key, "refused"); //User refuses every treatment
        }

        //PART 6
        //Post user new answer
        postResponse = given().contentType(ContentType.URLENC).
                formParams(values).when().post("/consents");
        postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Receipt page: " + postPage);
        assertTrue(postPage.contains("Data body " + t1Key));
        assertTrue(postPage.contains("Data body " + t2Key));
        assertFalse(postPage.contains("Accepté"));
        assertTrue(postPage.contains("Refusé"));
    }

}
