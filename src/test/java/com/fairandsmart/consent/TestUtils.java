package com.fairandsmart.consent;

import com.fairandsmart.consent.api.dto.ContentDto;
import com.fairandsmart.consent.api.dto.CreateModelDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.model.*;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestUtils {

    public static CreateModelDto generateCreateModelDto(String key, String type) {
        CreateModelDto dto = new CreateModelDto();
        dto.setKey(key);
        dto.setType(type);
        dto.setName("Name " + key);
        dto.setDescription("Description " + key);
        return dto;
    }

    public static ContentDto generateContentDto(String key, String type, String locale) {
        ContentDto dto = new ContentDto();
        dto.setLocale(locale);

        switch (type) {
            case Header.TYPE:
                dto.setContent(generateHeader(key));
                break;
            case Footer.TYPE:
                dto.setContent(generateFooter(key));
                break;
            case Treatment.TYPE:
                dto.setContent(generateTreatment(key));
                break;
            case Conditions.TYPE:
                dto.setContent(generateConditions(key));
                break;
            case Email.TYPE:
                dto.setContent(generateEmail(key));
                break;
            case Theme.TYPE:
                dto.setContent(generateTheme(key));
                break;
        }

        return dto;
    }

    public static Header generateHeader(String key) {
        return new Header()
                .withTitle("Title " + key)
                .withBody("Body " + key)
                .withLogoPath("Logo path " + key)
                .withLogoAltText("Logo alt text " + key)
                .withPrivacyPolicyUrl("Privacy policy URL " + key)
                .withDataController(generateDataController(key + "_dc"))
                .withShowDataController(true)
                .withCollectionMethod(ConsentContext.CollectionMethod.WEBFORM.name())
                .withShowCollectionMethod(true)
                .withJurisdiction("Jurisdiction " + key)
                .withShowJurisdiction(true)
                .withScope("Scope " + key)
                .withShowScope(true)
                .withShortNoticeLink("Short notice " + key)
                .withShowShortNoticeLink(true)
                .withCustomPrivacyPolicyText("Privacy policy label " + key);
    }

    public static Footer generateFooter(String key) {
        return new Footer()
                .withBody("Body " + key)
                .withCustomAcceptAllText("Accept all " + key)
                .withShowAcceptAll(true);
    }

    public static Treatment generateTreatment(String key) {
        return new Treatment()
                .withTreatmentTitle("Treatment title " + key)
                .withDataTitle("Data title " + key)
                .withDataBody("Data body " + key)
                .withRetentionTitle("Retention title " + key)
                .withRetentionBody("Retention body " + key)
                .withUsageTitle("Usage title " + key)
                .withUsageBody("Usage body " + key)
                .withPurpose(Treatment.Purpose.CONSENT_CORE_SERVICE)
                .withPurpose(Treatment.Purpose.CONSENT_THIRD_PART_SHARING)
                .withDataController(generateDataController(key + "_dc"))
                .withShowDataController(true)
                .withContainsMedicalData(true)
                .withContainsSensitiveData(true)
                .withThirdParty(
                        new NameValuePair(
                                "Third party 1 name " + key,
                                "Third party 1 description " + key))
                .withThirdParty(
                        new NameValuePair(
                                "Third party 2 name " + key,
                                "Third party 2 description " + key));
    }

    public static Conditions generateConditions(String key) {
        return new Conditions()
                .withTitle("Title " + key)
                .withBody("Body " + key)
                .withAcceptLabel("Accept " + key)
                .withRejectLabel("Reject" + key);
    }

    public static Email generateEmail(String key) {
        return new Email()
                .withSender("Sender " + key)
                .withSubject("Subject " + key)
                .withTitle("Title " + key)
                .withBody("Body " + key)
                .withButtonLabel("Button label " + key)
                .withFooter("Footer " + key)
                .withSignature("Signature " + key);
    }

    public static Theme generateTheme(String key) {
        return new Theme()
                .withName("Name " + key)
                .withPresentation("Presentation " + key)
                .withIcon("Icon " + key)
                .withTargetType(Theme.TargetType.FORM)
                .withCss("CSS " + key);
    }

    private static Controller generateDataController(String key) {
        return new Controller()
                .withName("Name " + key)
                .withCompany("Company " + key)
                .withAddress("Address " + key)
                .withEmail(key + "@email.com")
                .withPhoneNumber("0123456789")
                .withActingBehalfCompany(true);
    }

    public static Map<String, String> readFormInputs(Document html) {
        Elements inputs = html.getAllElements();
        List<FormElement> forms = inputs.forms();
        Map<String, String> values = new HashMap<>();
        for (FormElement form : forms) {
            // Select consent form
            if (form.id().equals("consent")) {
                values = form.formData().stream().collect(Collectors.toMap(Connection.KeyVal::key, Connection.KeyVal::value));
                // Select user inputs
                List<Element> selectElements = form.elements().stream().filter(e -> e.tagName().equals("select")).collect(Collectors.toList());
                for (Element element : selectElements) {
                    Element option = element.children().first();
                    // Ignore "accept all" button
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
        return values;
    }
}
