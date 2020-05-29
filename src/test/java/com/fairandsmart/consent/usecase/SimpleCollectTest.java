package com.fairandsmart.consent.usecase;

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
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omg.PortableInterceptor.ACTIVE;

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

    @BeforeEach
    public void setup() {
        //Check that app is running
        given().contentType(ContentType.JSON).
                when().get("/health").
                then().body("status", equalTo("UP"));

        //Create header model
        CreateModelDto h1 = new CreateModelDto();
        h1.setKey("h1");
        h1.setType(Header.TYPE);
        h1.setName("H1");
        h1.setDescription("Le header H1");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(h1).size());
        Response response = given().auth().basic("sheldon", "password").
                                contentType(ContentType.JSON).body(h1).
                                when().post("/models");
        response.then().statusCode(200);
        ModelEntry eh1 = response.body().as(ModelEntry.class);

        //Create header model version
        ContentDto ch1 = new ContentDto();
        ch1.setLocale("fr_FR");
        ch1.setContent(new Header()
                .withTitle("Title h1")
                .withBody("Body h1")
                .withPrivacyPolicyUrl("Readmore h1")
        );
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ch1).size());
        response = given().auth().basic("sheldon", "password").
                   contentType(ContentType.JSON).body(ch1).
                   when().post("/models/" + eh1.id + "/versions");
        response.then().statusCode(200);
        ModelVersion vh1 = response.body().as(ModelVersion.class);

        //Activate header model version
        response = given().auth().basic("sheldon", "password").
                   contentType(ContentType.TEXT).body(ModelVersion.Status.ACTIVE).
                   when().put("/models/" + eh1.id + "/versions/" + vh1.id + "/status");
        response.then().statusCode(200);
        vh1 = response.body().as(ModelVersion.class);
        assertEquals(ModelVersion.Status.ACTIVE, vh1.status);


        //Create footer model
        CreateModelDto f1 = new CreateModelDto();
        f1.setKey("f1");
        f1.setType(Footer.TYPE);
        f1.setName("F1");
        f1.setDescription("Le footer F1");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(f1).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(f1).
                when().post("/models");
        response.then().statusCode(200);
        ModelEntry ef1 = response.body().as(ModelEntry.class);

        //Create footer model version
        ContentDto cf1 = new ContentDto();
        cf1.setLocale("fr_FR");
        cf1.setContent(new Footer()
                        .withShowAcceptAll(true)
                        .withCustomAcceptAllText("J'accepte tout"));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(cf1).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(cf1).
                when().post("/models/" + ef1.id + "/versions");
        response.then().statusCode(200);
        ModelVersion vf1 = response.body().as(ModelVersion.class);

        //Activate footer model version
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.TEXT).body(ModelVersion.Status.ACTIVE).
                when().put("/models/" + ef1.id + "/versions/" + vf1.id + "/status");
        response.then().statusCode(200);
        vf1 = response.body().as(ModelVersion.class);
        assertEquals(ModelVersion.Status.ACTIVE, vf1.status);


        //Create treatment 1 model
        CreateModelDto t1 = new CreateModelDto();
        t1.setKey("t1");
        t1.setType(Treatment.TYPE);
        t1.setName("T1");
        t1.setDescription("Le traitement t1");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t1).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(t1).
                when().post("/models");
        response.then().statusCode(200);
        ModelEntry et1 = response.body().as(ModelEntry.class);

        //Create treatment 1 model version
        ContentDto ct1 = new ContentDto();
        ct1.setLocale("fr_FR");
        ct1.setContent(new Treatment()
                .withTreatmentTitle("Titre du traitement t1")
                .withDataBody("Nous avons besoin de votre nom.")
                .withRetentionBody("Nous le garderons pendant toute votre vie.")
                .withUsageBody("Nous pourrons ainsi tout savoir sur vous.")
                .withPurpose(Treatment.Purpose.CONSENT_CORE_SERVICE)
        );
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ct1).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(ct1).
                when().post("/models/" + et1.id + "/versions");
        response.then().statusCode(200);
        ModelVersion vt1 = response.body().as(ModelVersion.class);

        //Activate treatment 1 model version
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.TEXT).body(ModelVersion.Status.ACTIVE).
                when().put("/models/" + et1.id + "/versions/" + vt1.id + "/status");
        response.then().statusCode(200);
        vt1 = response.body().as(ModelVersion.class);
        assertEquals(ModelVersion.Status.ACTIVE, vt1.status);


        //Create treatment 2 model
        CreateModelDto t2 = new CreateModelDto();
        t2.setKey("t2");
        t2.setType(Treatment.TYPE);
        t2.setName("T2");
        t2.setDescription("Le traitement t2");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(t2).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(t2).
                when().post("/models");
        response.then().statusCode(200);
        ModelEntry et2 = response.body().as(ModelEntry.class);

        //Create treatment 2 model version
        ContentDto ct2 = new ContentDto();
        ct2.setLocale("fr_FR");
        ct2.setContent(new Treatment()
                .withTreatmentTitle("Titre du traitement t2")
                .withDataBody("Nous voulons votre email.")
                .withRetentionBody("Nous le conserverons 3 ans.")
                .withUsageBody("Nous pourrons ainsi vous contacter.")
                .withPurpose(Treatment.Purpose.CONSENT_MARKETING)
        );
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ct2).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(ct2).
                when().post("/models/" + et2.id + "/versions");
        response.then().statusCode(200);
        ModelVersion vt2 = response.body().as(ModelVersion.class);

        //Activate treatment 1 model version
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.TEXT).body(ModelVersion.Status.ACTIVE).
                when().put("/models/" + et2.id + "/versions/" + vt2.id + "/status");
        response.then().statusCode(200);
        vt2 = response.body().as(ModelVersion.class);
        assertEquals(ModelVersion.Status.ACTIVE, vt2.status);
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
                for (Element element : formElements) {
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
