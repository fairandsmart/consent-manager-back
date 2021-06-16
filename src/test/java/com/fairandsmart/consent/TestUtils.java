package com.fairandsmart.consent;

/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */

import com.fairandsmart.consent.api.dto.ModelEntryDto;
import com.fairandsmart.consent.api.dto.ModelVersionDto;
import com.fairandsmart.consent.manager.ConsentContext;
import com.fairandsmart.consent.manager.entity.ModelData;
import com.fairandsmart.consent.manager.model.*;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.Collectors;

public class TestUtils {

    public static ModelEntryDto generateModelEntryDto(String key, String type) {
        ModelEntryDto dto = new ModelEntryDto();
        dto.setKey(key);
        dto.setType(type);
        dto.setName("Name " + key);
        dto.setDescription("Description " + key);
        return dto;
    }

    public static ModelVersionDto generateModelVersionDto(String key, String type, String language) {
        ModelVersionDto dto = new ModelVersionDto();
        dto.setDefaultLanguage(language);

        switch (type) {
            case BasicInfo.TYPE:
                dto.setData(Collections.singletonMap(language, generateBasicInfo(key)));
                break;
            case Processing.TYPE:
                dto.setData(Collections.singletonMap(language, generateProcessing(key)));
                break;
            case Conditions.TYPE:
                dto.setData(Collections.singletonMap(language, generateConditions(key)));
                break;
            case Email.TYPE:
                dto.setData(Collections.singletonMap(language, generateEmail(key)));
                break;
            case Theme.TYPE:
                dto.setData(Collections.singletonMap(language, generateTheme(key)));
                break;
        }

        return dto;
    }

    public static Map<String, ModelData> generateModelData(String key, String type, String language) {
        ModelData modelData = null;

        switch (type) {
            case BasicInfo.TYPE:
                modelData = generateBasicInfo(key);
                break;
            case Processing.TYPE:
                modelData = generateProcessing(key);
                break;
            case Conditions.TYPE:
                modelData = generateConditions(key);
                break;
            case Email.TYPE:
                modelData = generateEmail(key);
                break;
            case Theme.TYPE:
                modelData = generateTheme(key);
                break;
        }

        Map<String, ModelData> map = new HashMap<>();
        map.put(language, modelData);
        return map;
    }

    public static BasicInfo generateBasicInfo(String key) {
        return new BasicInfo()
                .withTitle("Title " + key)
                .withHeader("Header " + key)
                .withFooter("Footer " + key)
                .withPrivacyPolicyUrl("Privacy policy URL " + key)
                .withDataController(generateDataController(key + "_dc"))
                .withShowDataController(true)
                .withJurisdiction("Jurisdiction " + key)
                .withShowJurisdiction(true)
                .withScope("Scope " + key)
                .withShowScope(true)
                .withShortNoticeLink("Short notice " + key)
                .withShowShortNoticeLink(true)
                .withCustomPrivacyPolicyText("Privacy policy label " + key);
    }

    public static Processing generateProcessing(String key) {
        return new Processing()
                .withTitle("Processing title " + key)
                .withData("Data body " + key)
                .withRetention(new RetentionInfo("Retention body " + key, 3, RetentionInfo.RetentionUnit.MONTH, "Retention body " + key + " " + 3 + " months"))
                .withUsage("Usage body " + key)
                .withPurpose(Processing.Purpose.CONSENT_CORE_SERVICE)
                .withPurpose(Processing.Purpose.CONSENT_THIRD_PART_SHARING)
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

    public static Preference generatePreference(String key) {
        List<String> options = new ArrayList<>();
        options.add("Option1");
        options.add("Option2");
        options.add("Option3");
        return new Preference()
                .withLabel("Label " + key)
                .withDescription("Description " + key)
                .withOptions(options)
                .withValueType(Preference.ValueType.RADIO_BUTTONS)
                .withIncludeDefault(true)
                .withDefaultValues(Collections.singletonList("Option3"));
    }

    public static Conditions generateConditions(String key) {
        return new Conditions()
                .withTitle("Title " + key)
                .withBody("Body " + key);
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
                .withIcon("Icon " + key)
                .withCss("CSS " + key);
    }

    public static FormLayout generateFormLayout(String info, List<String> elements) {
        return new FormLayout()
                .withInfo(info)
                .withElements(elements)
                .withAcceptAllVisible(true)
                .withAcceptAllText("AccepterTout")
                .withDesiredReceiptMimeType("application/pdf")
                .withIncludeIFrameResizer(false)
                .withOrientation(FormLayout.Orientation.VERTICAL)
                .withExistingElementsVisible(true)
                .withValidityVisible(false);

    }

    private static Controller generateDataController(String key) {
        return new Controller()
                .withInfo("Info " + key)
                .withCompany("Company " + key)
                .withAddress("Address " + key)
                .withEmail(key + "@email.com")
                .withPhoneNumber("0123456789");
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

    public static Map<String, String> readConfirmationInputs(Document html) {
        Elements inputs = html.getAllElements();
        List<FormElement> forms = inputs.forms();
        Map<String, String> values = new HashMap<>();
        for (FormElement form : forms) {
            // Select consent form
            if (form.id().equals("confirm")) {
                values = form.formData().stream().collect(Collectors.toMap(Connection.KeyVal::key, Connection.KeyVal::value));
            }
        }
        return values;
    }
}
