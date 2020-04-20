package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.api.dto.CreateModelEntryDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.data.Information;
import com.fairandsmart.consent.manager.data.Treatment;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class SimpleCollectTest {

    private static final Logger LOGGER = Logger.getLogger(SimpleCollectTest.class.getName());

    @BeforeEach
    public void setup() {
        //Check that app is running
        given().contentType(ContentType.JSON).
                when().get("/health").
                then().body("status", equalTo("UP"));

        //Create header model
        CreateModelEntryDto h1 = new CreateModelEntryDto();
        h1.setType(ModelEntry.Type.HEADER);
        h1.setLocale("fr_FR");
        h1.setKey("h1");
        h1.setName("H1");
        h1.setDescription("Le header H1");
        h1.setContent(new Information().withTitle("Title h1").withBody("Body h1").withFooter("Foot h1"));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(h1).size());
        given().contentType(ContentType.JSON).body(h1).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());

        //Create footer model
        CreateModelEntryDto f1 = new CreateModelEntryDto();
        f1.setType(ModelEntry.Type.FOOTER);
        f1.setLocale("fr_FR");
        f1.setKey("f1");
        f1.setName("F1");
        f1.setDescription("Le footer F1");
        f1.setContent(new Information().withTitle("Title f1").withBody("Body f1").withFooter("Foot f1"));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(f1).size());
        given().contentType(ContentType.JSON).body(f1).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());

        //Create treatment 1 model
        CreateModelEntryDto t1 = new CreateModelEntryDto();
        t1.setType(ModelEntry.Type.TREATMENT);
        t1.setLocale("fr_FR");
        t1.setKey("t1");
        t1.setName("T1");
        t1.setDescription("Le traitement t1");
        t1.setContent(new Treatment().withData("Your name").withRetention("All your life").withPurpose(Treatment.Purpose.CONSENT_CORE_SERVICE));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t1).size());
        given().contentType(ContentType.JSON).body(t1).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());

        CreateModelEntryDto t2 = new CreateModelEntryDto();
        t2.setType(ModelEntry.Type.TREATMENT);
        t2.setLocale("fr_FR");
        t2.setKey("t2");
        t2.setName("T2");
        t2.setDescription("Le traitement t2");
        t2.setContent(new Treatment().withData("Your email").withRetention("All your life").withPurpose(Treatment.Purpose.CONSENT_CORE_SERVICE));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t2).size());
        given().contentType(ContentType.JSON).body(t2).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());

    }

    /**
     * 1 : l'orga génère un context de collecte
     *      le context contient le nom du user (subject) et les ids des traitements
     * 2 : Le user (anonyme) appelle une URL avec le context en paramètre (header ou query param),
     *      cas 1 : sujet inconnu car pas de collecte précédente, on génère un formulaire vide avec les éléments demandés dans le context et un nouveau token
     * 3 : le user (anonyme) poste le formulaire avec ses réponses sur une autre URL
     *      cas 1 : tout est bon, un reçu est généré et renvoyé en réponse au user
     * 4 : le user retourne à son url initiale qui devrait être contenue dans le reçu ou dans le context
     */
    @Test
    public void testSimpleCollect() {
        //PART 1
        //Use basic consent context for first generation
        ConsentContext ctx = new ConsentContext()
                .withSubject("mmichu")
                .withOrientation(ConsentContext.Orientation.VERTICAL)
                .withHeaderKey("h1")
                .withTreatmentsKeys(Arrays.asList("t1","t2"))
                .withFooterKey("f1")
                .withReferer("urldetest");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic("sheldon", "password").contentType(ContentType.JSON).body(ctx).
            when().post("/consents/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token : " + token);

        //PART 2
        Response response = given().header("TOKEN", token).
            when().get("/consents");
        String page = response.asString();
        response.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        //Orientation
        assertTrue(page.contains("vertical"));
        //Header
        assertTrue(page.contains("Title h1"));
        assertTrue(page.contains("Body h1"));
        assertTrue(page.contains("Foot h1"));
        //Traitements
        assertTrue(page.contains("Le traitement t1"));
        assertTrue(page.contains("Le traitement t2"));
        //Footer
        assertTrue(page.contains("Title f1"));
        assertTrue(page.contains("Body f1"));
        assertTrue(page.contains("Foot f1"));

        //PART 3
        //Réponses de l'utilisateur sur le formulaire
        Map<String, Integer> values = new HashMap<>();
        values.put("T1", 1);
        values.put("T2", 0);

        Response postResponse = given().header("TOKEN", token).contentType(ContentType.JSON).
                body(values).when().post("/consents");
        String postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Receipt page: " + postPage);
        assertTrue(postPage.contains("Receipt"));
        assertTrue(postPage.contains("T1 : accepté"));
        assertTrue(postPage.contains("T2 : refusé"));

        //PART 4
        //TODO

    }

}
