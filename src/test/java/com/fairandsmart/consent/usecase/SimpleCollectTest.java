package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.api.dto.CreateModelEntryDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Treatment;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
        h1.setLocale("fr_FR");
        h1.setKey("h1");
        h1.setName("H1");
        h1.setDescription("Le header H1");
        h1.setContent(new Header()
                .withTitle("Title h1")
                .withBody("Body h1")
                .withPrivacyPolicyUrl("Readmore h1")
        );
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(h1).size());
        given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(h1).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());

        //Create footer model
        CreateModelEntryDto f1 = new CreateModelEntryDto();
        f1.setLocale("fr_FR");
        f1.setKey("f1");
        f1.setName("F1");
        f1.setDescription("Le footer F1");
        f1.setContent(new Footer()
                .withShowAcceptAll(true)
                .withCustomAcceptAllText("J'accepte tout")
        );
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(f1).size());
        given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(f1).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());

        //Create treatment 1 model
        CreateModelEntryDto t1 = new CreateModelEntryDto();
        t1.setLocale("fr_FR");
        t1.setKey("t1");
        t1.setName("T1");
        t1.setDescription("Le traitement t1");
        t1.setContent(new Treatment()
                .withTreatmentTitle("Titre du traitement t1")
                .withDataBody("Nous avons besoin de votre nom.")
                .withRetentionBody("Nous le garderons pendant toute votre vie.")
                .withUsageBody("Nous pourrons ainsi tout savoir sur vous.")
                .withPurpose(Treatment.Purpose.CONSENT_CORE_SERVICE)
        );
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t1).size());
        given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(t1).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());

        CreateModelEntryDto t2 = new CreateModelEntryDto();
        t2.setLocale("fr_FR");
        t2.setKey("t2");
        t2.setName("T2");
        t2.setDescription("Le traitement t2");
        t2.setContent(new Treatment()
                .withTreatmentTitle("Titre du traitement t2")
                .withDataBody("Nous voulons votre email.")
                .withRetentionBody("Nous le conserverons 3 ans.")
                .withUsageBody("Nous pourrons ainsi vous contacter.")
                .withPurpose(Treatment.Purpose.CONSENT_MARKETING)
        );
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t2).size());
        given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(t2).
                when().post("/consents/models").
                then().statusCode(201).header("location", notNullValue());
    }

    /**
     * 1 : l'orga génère un context de collecte
     * le context contient le nom du user (subject) et les ids des traitements
     * 2 : Le user (anonyme) appelle une URL avec le context en paramètre (header ou query param),
     * cas 1 : sujet inconnu car pas de collecte précédente, on génère un formulaire vide avec les éléments demandés dans le context et un nouveau token
     * 3 : le user (anonyme) poste le formulaire avec ses réponses sur une autre URL
     * cas 1 : tout est bon, un reçu est généré et renvoyé en réponse au user
     * 4 : le user retourne à son url initiale qui devrait être contenue dans le reçu ou dans le context
     */
    @Test
    public void testSimpleCollect() {
        //PART 1
        //Use basic consent context for first generation
        ConsentContext ctx = new ConsentContext()
                .setSubject("mmichu")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader("h1")
                .setElements(Arrays.asList("t1", "t2"))
                .setFooter("f1")
                .setCallback("urldetest")
                .setLocale("fr_FR");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic("sheldon", "password").contentType(ContentType.JSON).body(ctx)
                .when().post("/consents/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token : " + token);

        //PART 2
        Response response = given().header("TOKEN", token).when().get("/consents");
        String page = response.asString();
        response.then().contentType("text/html").assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        //Orientation
        assertFalse(page.contains("class=\"right\"")); // Vertical
        //Header
        assertTrue(page.contains("Title h1"));
        assertTrue(page.contains("Body h1"));
        assertTrue(page.contains("href=\"Readmore h1\""));
        //Treatment 1
        assertTrue(page.contains("Titre du traitement t1"));
        assertTrue(page.contains("votre nom"));
        assertTrue(page.contains("toute votre vie"));
        assertTrue(page.contains("tout savoir sur vous"));
        assertTrue(page.contains("consent_core_service.png"));
        //Treatment 2
        assertTrue(page.contains("Titre du traitement t2"));
        assertTrue(page.contains("votre email"));
        assertTrue(page.contains("3 ans"));
        assertTrue(page.contains("vous contacter"));
        assertTrue(page.contains("consent_marketing.png"));
        //Footer
        assertTrue(page.contains("accept-all-switch"));

        Document html = Jsoup.parse(page);
        Elements inputs = html.getAllElements();
        List<FormElement> forms = inputs.forms();
        Map<String, String> values = Collections.EMPTY_MAP;
        for (FormElement form : forms) {
            if (form.id().equals("consent")) {
                values = form.formData().stream().collect(Collectors.toMap(Connection.KeyVal::key, Connection.KeyVal::value));
                Elements formElements = form.elements();
                for (Iterator<Element> i = formElements.iterator(); i.hasNext(); ) {
                    Element element = i.next();
                    // TODO : simplifier ?? ou pas la peine ?
                    if (element.tagName().equals("select")) {
                        Element option = element.children().first();
                        if (!option.id().contains("accept-all")) {
                            if (option.hasAttr("selected")) { // option: accepted
                                values.put(option.id().substring(0, option.id().length() - 9), option.val());
                            } else {
                                option = element.children().last(); // option: refused
                                if (option.hasAttr("selected")) {
                                    values.put(option.id().substring(0, option.id().length() - 8), option.val());
                                }
                            }
                        }
                    }
                }
            }
        }
        LOGGER.log(Level.INFO, "Form Values: " + values);

        //PART 3
        Response postResponse = given().contentType(ContentType.JSON).
                body(values).when().post("/consents");
        String postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Receipt page: " + postPage);
        assertTrue(postPage.contains("Receipt"));
        //TODO
    }
}
