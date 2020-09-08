package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.TestUtils;
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
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.Validation;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class CollectWithEmailTest {

    private static final Logger LOGGER = Logger.getLogger(CollectWithEmailTest.class.getName());
    private static final String TEST_USER = "sheldon";
    private static final String TEST_PASSWORD = "password";

    private static final String locale = "fr_FR";
    private static final String hKey = "cwet_h1";
    private static final String t1Key = "cwet_t1";
    private static final String t2Key = "cwet_t2";
    private static final String fKey = "cwet_f1";
    private static final String eKey = "cwet_e1";
    private static final String recipient = "mmichu@localhost";

    @Inject
    MockMailbox mailbox;

    @ConfigProperty(name = "consent.public.url")
    String publicUrl;

    /**
     * 1 : l'orga génère un context de collecte qui contient entre autres un email d'optout
     * 2 : Le user (anonyme) appelle une URL avec le context en paramètre (header ou query param),
     * 3 : le user (anonyme) poste le formulaire avec ses réponses sur une autre URL
     * 4 : le user (anonyme) reçoit un email de confirmation
     * 5 : le user (anonyme) clique sur le lien d'opt-out
     */
    @Test
    @TestSecurity(user = "sheldon", roles = {"admin"})
    public void testCollectWithEmail() throws InterruptedException {

        //SETUP
        //Check that the app is running
        given().contentType(ContentType.JSON).
                when().get("/health").
                then().body("status", equalTo("UP"));

        //Generate test elements
        List<String> keys = List.of(hKey, t1Key, t2Key, fKey, eKey);
        List<String> types = List.of(Header.TYPE, Treatment.TYPE, Treatment.TYPE, Footer.TYPE, Email.TYPE);
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

        //PART 1
        //Use basic consent context for first generation
        ConsentContext ctx = new ConsentContext()
                .setSubject("mmichu")
                .setValidity("P2Y")
                .setOrientation(ConsentForm.Orientation.VERTICAL)
                .setHeader(hKey)
                .setElements(Arrays.asList(t1Key, t2Key))
                .setFooter(fKey)
                .setLocale(locale)
                .setOptoutModel(eKey)
                .setOptoutRecipient(recipient);
        assertEquals(0, Validation.buildDefaultValidatorFactory().getValidator().validate(ctx).size());

        String token = given().auth().basic(TEST_USER, TEST_PASSWORD).contentType(ContentType.JSON).body(ctx)
                .when().post("/consents/token").asString();
        assertNotNull(token);
        LOGGER.log(Level.INFO, "Token :" + token);

        //PART 2
        Response response = given().header("TOKEN", token).when().get("/consents");
        String page = response.asString();
        response.then().contentType("text/html").assertThat().statusCode(200);

        LOGGER.log(Level.INFO, "Consent form page: " + page);
        Document html = Jsoup.parse(page);
        Map<String, String> values = TestUtils.readFormInputs(html);
        LOGGER.log(Level.INFO, "Form Values: " + values);

        //PART 3
        Response postResponse = given().contentType(ContentType.URLENC).formParams(values).when().post("/consents");
        postResponse.then().assertThat().statusCode(200);

        //PART 4
        Thread.sleep(500);
        assertTrue(mailbox.getTotalMessagesSent() > 0);
        List<Mail> sent = mailbox.getMessagesSentTo(recipient);
        assertEquals(1, sent.size());
        assertEquals("Subject " + eKey, sent.get(0).getSubject());
        String received = sent.get(0).getHtml();
        assertTrue(received.contains("Title " + eKey));
        assertTrue(received.contains("Body " + eKey));
        assertTrue(received.contains("Footer " + eKey));
        assertTrue(received.contains("Signature " + eKey));
        assertTrue(received.contains(publicUrl + "/consents?t="));
        assertEquals("Sender " + eKey, sent.get(0).getFrom());

        //PART 5
        html = Jsoup.parse(received);
        Optional<Element> optOutLink = html.select("a[href]").stream().filter(l -> l.id().equals("form-url")).findFirst();
        if (optOutLink.isPresent()) {
            response = given().when().get(optOutLink.get().attr("abs:href"));
            response.then().contentType("text/html").assertThat().statusCode(200);
        } else {
            fail("Optout link not found");
        }
    }

}
