package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.api.dto.ContentDto;
import com.fairandsmart.consent.api.dto.CreateModelDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.ConsentForm;
import com.fairandsmart.consent.manager.entity.ModelEntry;
import com.fairandsmart.consent.manager.entity.ModelVersion;
import com.fairandsmart.consent.manager.model.Email;
import com.fairandsmart.consent.manager.model.Footer;
import com.fairandsmart.consent.manager.model.Header;
import com.fairandsmart.consent.manager.model.Treatment;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.maven.model.Organization;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.Validation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CollectWithEmailTest {

    private static final Logger LOGGER = Logger.getLogger(CollectWithEmailTest.class.getName());

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "consent.public.url")
    String publicUrl;

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

        //Activate treatment 2 model version
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.TEXT).body(ModelVersion.Status.ACTIVE).
                when().put("/models/" + et2.id + "/versions/" + vt2.id + "/status");
        response.then().statusCode(200);
        vt2 = response.body().as(ModelVersion.class);
        assertEquals(ModelVersion.Status.ACTIVE, vt2.status);

        //Create email model
        CreateModelDto e1 = new CreateModelDto();
        e1.setKey("email1");
        e1.setType(Email.TYPE);
        e1.setName("EMAIL1");
        e1.setDescription("L'email E1");
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(e1).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(e1).
                when().post("/models");
        response.then().statusCode(200);
        ModelEntry ee1 = response.body().as(ModelEntry.class);

        //Create email model version
        ContentDto ce1 = new ContentDto();
        ce1.setLocale("fr_FR");
        ce1.setContent(new Email()
                .withSender("optout@localhost")
                .withSubject("Vous avez fait un choix")
                .withTitle("Vous nous avez donné votre consentement, vous pouvez modifier votre choix")
                .withBody("Lors de votre dernire contact avec notre service client vous avez donné votre consentement sur l'utilisation " +
                        "de certaines données personnelles dans le cadre de nos différentes activités. Vous avez la possibilité de modifier " +
                        "ces choix en cliquant sur le lien ci-dessous")
                .withButtonLabel("Bouton modifier mon choix")
                .withFooter("Vous pouvez également modifier ces choix à tout moment depuis votre espace adhérent")
                .withSignature("Le service client ACME."));
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ce1).size());
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.JSON).body(ce1).
                when().post("/models/" + ee1.id + "/versions");
        response.then().statusCode(200);
        ModelVersion ve1 = response.body().as(ModelVersion.class);

        //Activate email model version
        response = given().auth().basic("sheldon", "password").
                contentType(ContentType.TEXT).body(ModelVersion.Status.ACTIVE).
                when().put("/models/" + ee1.id + "/versions/" + ve1.id + "/status");
        response.then().statusCode(200);
        ve1 = response.body().as(ModelVersion.class);
        assertEquals(ModelVersion.Status.ACTIVE, ve1.status);
    }

    /**
     * 1 : l'orga génère un context de collecte qui contient entre autres un email d'optout
     * 2 : Le user (anonyme) appelle une URL avec le context en paramètre (header ou query param),
     * 3 : le user (anonyme) poste le formulaire avec ses réponses sur une autre URL
     * 4 : le user (anonyme) reçoit un email de confirmation
     */
    @Test
    public void testCollectWithEmail() throws InterruptedException {
        //PART 1
        //Use basic consent context for first generation
        ConsentContext ctx = new ConsentContext()
                .setSubject("mmichu")
                .setValidity("P2Y")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader("h1")
                .setElements(Arrays.asList("t1", "t2"))
                .setFooter("f1")
                .setLocale("fr_FR")
                .setOptoutModel("email1")
                .setOptoutRecipient("mmichu@localhost");
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
        Response postResponse = given().contentType(ContentType.URLENC).formParams(values).when().post("/consents");
        String postPage = postResponse.asString();
        postResponse.then().assertThat().statusCode(200);

        //PART 4
        Thread.sleep(500);
        assertTrue(mailbox.getTotalMessagesSent() > 0);
        List<Mail> sent = mailbox.getMessagesSentTo("mmichu@localhost");
        assertEquals(1, sent.size());
        assertEquals("Vous avez fait un choix", sent.get(0).getSubject());
        String received = sent.get(0).getHtml();
        assertTrue(received.contains("Vous nous avez donné votre consentement, vous pouvez modifier votre choix"));
        assertTrue(received.contains("certaines données personnelles dans le cadre de nos différentes activités. Vous"));
        assertTrue(received.contains("Bouton modifier mon choix"));
        assertTrue(received.contains("à tout moment depuis votre espace adhérent"));
        assertTrue(received.contains("Le service client ACME."));
        assertTrue(received.contains(publicUrl + "/consents?t="));
        assertEquals("optout@localhost", sent.get(0).getFrom());

        //PART 5
        html = Jsoup.parse(received);
        String optoutlink = "";
        Elements links = html.select("a[href]");
        for (Element link : links) {
            if ( link.id().equals("form-url") ) {
                optoutlink = link.attr("abs:href");
            }
        }
        response = given().when().get(optoutlink);
        page = response.asString();
        response.then().contentType("text/html").assertThat().statusCode(200);


    }
}
