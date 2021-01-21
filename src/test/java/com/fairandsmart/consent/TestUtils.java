package com.fairandsmart.consent;

/*-
 * #%L
 * Right Consents / A Consent Manager Platform
 * %%
 * Copyright (C) 2020 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
                .withIcon("Icon " + key)
                .withCss("CSS " + key);
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
}
