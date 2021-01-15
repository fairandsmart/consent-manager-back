<div class="element">
    <#if elementContent?is_hash>
        <#-- Header -->
        <div class="element-header">
            <h3 class="element-title"><@valueOrError elementContent.title "missingValue"></@valueOrError></h3>

            <#if !data.preview>
                <#assign isChecked=!data.preview && data.previousValues[element.serial]?has_content && data.previousValues[element.serial]=="accepted">
            </#if>
            <@toggleSwitch "${identifier}" isChecked></@toggleSwitch>
        </div>

        <#-- Data -->
        <div class="item-container">
            <p class="item-body"><@valueOrError elementContent.data "missingValue"></@valueOrError></p>
        </div>

        <#-- Retention -->
        <div class="item-container">
            <p class="item-body">
                <@valueOrError elementContent.retention.label "missingValue"></@valueOrError> <@valueOrError elementContent.retention.value "missingValue"></@valueOrError> <@readBundle elementContent.retention.unit?html "missingValue"></@readBundle>.
            </p>
        </div>

        <#-- Usage -->
        <div class="item-container">
            <p class="item-body"><@valueOrError elementContent.usage "missingValue"></@valueOrError></p>
        </div>

        <#-- Purposes -->
        <div class="item-container">
            <p class="item-body">
                <span><@readBundle "purpose_title"></@readBundle></span>
                <span><#list elementContent.purposes as purpose><@readBundle purpose?lower_case></@readBundle><#sep>, </#list></span>
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
            <div class="block-container sensitive-container">
                <h4 class="block-title"><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul class="block-body">
                    <li><span class="list-value">
                        <#if elementContent.containsMedicalData>
                            <@readBundle "containsMedicalData"></@readBundle>
                        <#else>
                            <@readBundle "containsSensitiveData"></@readBundle>
                        </#if>
                    </span></li>
                </ul>
            </div>
        </#if>

        <#-- Third parties -->
        <#if elementContent.thirdParties?has_content>
            <div class="block-container third-parties-container">
                <h4 class="block-title"><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                <ul class="block-body">
                    <#list elementContent.thirdParties as thirdParty>
                        <li>
                            <span class="list-label"><@valueOrError thirdParty.name></@valueOrError> :</span>
                            <span class="list-value"><@valueOrError thirdParty.value></@valueOrError></span>
                        </li>
                    </#list>
                </ul>
            </div>
        </#if>
    <#else>
        <p><@writeError "missingLanguage"></@writeError></p>
    </#if>
</div>
