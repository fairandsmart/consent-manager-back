package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.api.dto.CreateInformationDto;
import com.fairandsmart.consent.api.dto.CreateTreatmentDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Content;
import com.fairandsmart.consent.manager.entity.Information;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import java.util.Arrays;
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

        //Create head information model
        CreateInformationDto h1 = new CreateInformationDto();
        h1.setType(Information.Type.HEAD);
        h1.setCountry("FR");
        h1.setDefaultLanguage("fr");
        h1.setName("H1");
        h1.setDescription("Le header H1");
        h1.setContent(new Content().withTitle("Title h1").withBody("Body h1").withFooter("Foot h1"));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(h1).size());
        given().contentType(ContentType.JSON).body(h1).
                when().post("/models/informations").
                then().statusCode(201).header("location", notNullValue());

        //Create footer information model
        CreateInformationDto f1 = new CreateInformationDto();
        f1.setType(Information.Type.HEAD);
        f1.setCountry("FR");
        f1.setDefaultLanguage("fr");
        f1.setName("F1");
        f1.setDescription("Le footer F1");
        f1.setContent(new Content().withTitle("Title f1").withBody("Body f1").withFooter("Foot f1"));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(f1).size());
        given().contentType(ContentType.JSON).body(f1).
                when().post("/models/informations").
                then().statusCode(201).header("location", notNullValue());

        //Create treatment 1 mdoel
        CreateTreatmentDto t1 = new CreateTreatmentDto();
        t1.setKey("T1");
        t1.setCountry("FR");
        t1.setDefaultLanguage("fr");
        t1.setName("T1");
        t1.setDescription("Le traitement t1");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t1).size());
        given().contentType(ContentType.JSON).body(t1).
                when().post("/models/treatments").
                then().statusCode(201).header("location", notNullValue());

        //Create treatment 2 mdoel
        CreateTreatmentDto t2 = new CreateTreatmentDto();
        t2.setKey("T2");
        t2.setCountry("FR");
        t2.setDefaultLanguage("fr");
        t2.setName("T2");
        t2.setDescription("Le traitement t2");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t2).size());
        given().contentType(ContentType.JSON).body(t2).
                when().post("/models/treatments").
                then().statusCode(201).header("location", notNullValue());

    }

    /**
     * 1 : l'orga génère un context de collecte
     *      le context contient le nom du user (subject) et les ids des traitements
     * 2 : Le user (anonyme) appel une URL avec le context en paramètre (header ou query param),
     *      cas 1 : sujet inconnu car pas de collecte précédente, on génère un formulaire vide avec les éléments demandés dans le context et un nouveau token
     * 3 : le user (anonyme) post le formulaire avec ses réponses sur une autre URL
     *      cas 1 : tout est bon, un reçu est généré et renvoyé en réponse au user
     * 4 : le user retourne à son url initiale qui devrait être contenu dans le reçu ou dans le context
     */
    @Test
    public void testSimpleCollect() {
        //Use basic consent context for first generation
        ConsentContext ctx = new ConsentContext()
                .withSubject("mmichu")
                .withOrientation(ConsentContext.Orientation.VERTICAL)
                .withHeader("H1")
                .withTreatments(Arrays.asList("T1","T2"))
                .withFooter("F1");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic("sheldon", "password").contentType(ContentType.JSON).body(ctx).
            when().post("/admin/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token : " + token);

        Response response = given().header("TOKEN", token).
            when().get("/consents");
        String form = response.asString();
        response.then().assertThat().statusCode(200);
        LOGGER.log(Level.INFO, "Content Form: " + form);
        assertTrue(form.contains("Title h1"));
        assertTrue(form.contains("Body h1"));
        assertTrue(form.contains("Foot h1"));



    }

}
