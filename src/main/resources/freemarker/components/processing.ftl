<div class="processing">
    <#if elementContent?is_hash>
        <#-- Header -->
        <div class="processing-header">
            <h3><@valueOrError elementContent.title "missingValue"></@valueOrError></h3>

            <#if !data.preview>
                <#assign isChecked=!data.preview && data.previousValues[element.serial]?has_content && data.previousValues[element.serial]=="accepted">
            </#if>
            <@toggleSwitch "${identifier}" isChecked></@toggleSwitch>
        </div>

        <#-- Data -->
        <div class="item-wrapper">
            <p class="processing-body"><@valueOrError elementContent.data "missingValue"></@valueOrError></p>
        </div>

        <#-- Retention -->
        <div class="item-wrapper">
            <p class="processing-body">
                <@valueOrError elementContent.retention.label "missingValue"></@valueOrError> <@valueOrError elementContent.retention.value "missingValue"></@valueOrError> <@readBundle elementContent.retention.unit "missingValue"></@readBundle>.
            </p>
        </div>

        <#-- Usage & purposes -->
        <div class="item-wrapper">
            <p class="processing-body"><@valueOrError elementContent.usage "missingValue"></@valueOrError></p>
            <p class="processing-body purpose-container">
                <#list elementContent.purposes as purpose>
                    <img class="purpose" src="/assets/img/purpose/${purpose}.png" alt="${purpose}"/>
                </#list>
            </p>
        </div>

        <#-- Data controller -->
        <#if elementContent.showDataController && elementContent.dataController?has_content>
            <#assign dataController=elementContent.dataController>
            <#assign dataControllerId=identifier + "-controller">
            <#include "data-controller.ftl">
        </#if>

        <#-- Sensitive data -->
        <#if elementContent.containsSensitiveData>
            <div class="block-wrapper">
                <h4><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul class="processing-body">
                    <#if elementContent.containsMedicalData>
                        <li><span class="list-value"><@readBundle "containsMedicalData"></@readBundle></span></li>
                    <#else>
                        <li><span class="list-value"><@readBundle "containsSensitiveData"></@readBundle></span></li>
                    </#if>
                </ul>
            </div>
        </#if>

        <#-- Third parties -->
        <#if elementContent.thirdParties?has_content>
            <div class="block-wrapper">
                <h4><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                <ul class="processing-body">
                    <#list elementContent.thirdParties as thirdParty>
                        <li>
                            <span class="list-label">${thirdParty.name} :</span>
                            <span class="list-value">${thirdParty.value}</span>
                        </li>
                    </#list>
                </ul>
            </div>
        </#if>
    <#else>
        <p><@writeError "missingLanguage"></@writeError></p>
    </#if>
</div>