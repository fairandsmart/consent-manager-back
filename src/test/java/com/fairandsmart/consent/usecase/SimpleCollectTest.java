package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.TestUtils;
import com.fairandsmart.consent.api.dto.ModelEntryDto;
import com.fairandsmart.consent.api.dto.ModelVersionDto;
import com.fairandsmart.consent.api.dto.ModelVersionStatusDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Treatment;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    private static final String locale = "fr_FR";
    private static final String hKey = "sct_h1";
    private static final String t1Key = "sct_t1";
    private static final String t2Key = "sct_t2";
    private static final String fKey = "sct_f1";

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
        List<String> keys = List.of(hKey, t1Key, t2Key, fKey);
        List<String> types = List.of(Header.TYPE, Treatment.TYPE, Treatment.TYPE, Footer.TYPE);
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
            ModelVersionDto dto = TestUtils.generateModelVersionDto(key, type, locale);
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
                .setHeader(hKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setFooter(fKey)
                .setLocale(locale);
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx)
                .when().post("/consents/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token: " + token);

        //PART 1
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

        //PART 2
        //Post user answer
        LOGGER.log(Level.INFO, "Posting user answer");
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
        assertTrue(postPage.contains(SUBJECT));
        assertTrue(postPage.contains("Name " + hKey + "_dc"));

        //PART 3
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

        //PART 4
        //Post user new answer
        LOGGER.log(Level.INFO, "Posting new user answer");
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
