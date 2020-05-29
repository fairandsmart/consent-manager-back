<div class="treatment">
    <@fetchMultiLangContent element></@fetchMultiLangContent>

    <#if langContent?has_content && !langContent?is_string>
        <#-- Header -->
        <div class="treatment-header">
            <h3><@valueOrError langContent.treatmentTitle "missingValue"></@valueOrError></h3>

            <#assign isChecked=(data.previousValues[element.serial]?has_content && data.previousValues[element.serial]=="accepted")>
            <@toggleSwitch "${element.identifier}" isChecked></@toggleSwitch>
        </div>

        <#-- Data -->
        <#if langContent.dataTitle?has_content>
            <h4>${langContent.dataTitle}</h4>
        </#if>
        <p class="treatment-body"><@valueOrError langContent.dataBody "missingValue"></@valueOrError><p>

        <#-- Retention -->
        <#if langContent.retentionBody?has_content>
            <#if langContent.retentionTitle?has_content>
                <h4>${langContent.retentionTitle}</h4>
            </#if>
            <p class="treatment-body">${langContent.retentionBody}<p>
        </#if>

        <#-- Usage & purposes -->
        <#if langContent.usageTitle?has_content>
            <h4>${langContent.usageTitle}</h4>
        </#if>
        <p class="treatment-body"><@valueOrError langContent.usageBody "missingValue"></@valueOrError><p>
        <p class="treatment-body purpose-container">
            <#list langContent.purposes as purpose>
                <img class="purpose" src="/assets/img/purpose/${purpose?lower_case}.png"/>
            </#list>
        </p>

        <#-- Data controller -->
        <#if langContent.showDataController && langContent.dataController?has_content>
            <#assign dataController=langContent.dataController>
            <#include "dataController.ftl">
        </#if>

        <#-- Sensitive data -->
        <#if langContent.containsSensitiveData || langContent.containsMedicalData>
            <div class="block-wrapper">
                <h4><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul class="treatment-body">
                    <#if langContent.containsSensitiveData>
                        <li><@readBundle "containsSensitiveData"></@readBundle></li>
                    </#if>
                    <#if langContent.containsMedicalData>
                        <li><@readBundle "containsMedicalData"></@readBundle></li>
                    </#if>
                </ul>
            </div>
        </#if>

        <#-- Third parties -->
        <#if langContent.thirdParties?has_content>
            <div class="block-wrapper">
                <h4><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                <ul class="treatment-body">
                    <#list langContent.thirdParties as thirdPartyName, thirdPartyDescription>
                        <li>${thirdPartyName} : ${thirdPartyDescription}</li>
                    </#list>
                </ul>
            </div>
        </#if>
    </#if>
</div>