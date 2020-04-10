package com.fairandsmart.consent.usecase;

import com.fairandsmart.consent.api.dto.CreateInformationDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.Information;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
public class SimpleCollectTest {

    /**
     * 1 : l'orga génère un context de collecte
     *      le context contient le nom du user (subject) et les ids des traitements
     * 2 : Le user appel une URL avec le context en paramètre (header ou query param),
     *      cas 1 : subject inconnu pas de collecte précédente, on génère un formulaire vide avec les éléments demandés dans le context
     * 3 : le user post le formulaire avec ses réponses sur une autre URL
     *      cas 1 : tout est bon, un reçu est généré et renvoyé en réponse au user
     * 4 : le user retourne à son url initiale qui devrait être contenu dans le reçu ou dans le context
     */
    @Test
    public void testSimpleCollect() {
        //Préconditions
        CreateInformationDto h1 = new CreateInformationDto();
        h1.setType(Information.Type.HEAD);
        h1.setCountry("FR");
        h1.setDefaultLanguage("fr");
        h1.setName("H1");
        h1.setDescription("Le header H1");

        given().contentType(ContentType.JSON).body(h1).
        when().post("/models/informations").
        then().statusCode(201).header("location", notNullValue());

        ConsentContext ctx = new ConsentContext()
                .withSubject("mmichu")
                .withOrientation(ConsentContext.Orientation.VERTICAL)
                .withHeader("h1")
                .withTreatments(Collections.singletonList("t1"))
                .withFooter("f1");

        given()
            .when().post("/admin/token")
            .then()
            .statusCode(200)
            .body(notNullValue());
    }

}
