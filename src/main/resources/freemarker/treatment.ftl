<div class="treatment">
    <@fetchMultiLangContent element></@fetchMultiLangContent>

    <#if langContent?is_hash && langContent?has_content>
        <#-- Header -->
        <div class="treatment-header">
            <h3><@valueOrError langContent.treatmentTitle "missingValue"></@valueOrError></h3>

            <#assign isChecked=(!data.preview && data.previousValues[element.serial]?has_content && data.previousValues[element.serial]=="accepted")>
            <@toggleSwitch "${element.identifier}" isChecked></@toggleSwitch>
        </div>

        <#-- Data -->
        <div class="item-wrapper">
            <#if langContent.dataTitle?has_content>
                <h4>${langContent.dataTitle}</h4>
            </#if>
            <p class="treatment-body"><@valueOrError langContent.dataBody "missingValue"></@valueOrError></p>
        </div>

        <#-- Retention -->
        <div class="item-wrapper">
            <#if langContent.retentionTitle?has_content>
                <h4>${langContent.retentionTitle}</h4>
            </#if>
            <p class="treatment-body"><@valueOrError langContent.retentionBody "missingValue"></@valueOrError></p>
        </div>

        <#-- Usage & purposes -->
        <div class="item-wrapper">
            <#if langContent.usageTitle?has_content>
                <h4>${langContent.usageTitle}</h4>
            </#if>
            <p class="treatment-body"><@valueOrError langContent.usageBody "missingValue"></@valueOrError></p>
            <p class="treatment-body purpose-container">
                <#list langContent.purposes as purpose>
                    <img class="purpose" src="/assets/img/purpose/${purpose?lower_case}.png" alt="${purpose?lower_case}"/>
                </#list>
            </p>
        </div>

        <#-- Data controller -->
        <#if langContent.showDataController && langContent.dataController?has_content>
            <#assign dataController=langContent.dataController>
            <#include "data-controller.ftl">
        </#if>

        <#-- Sensitive data -->
        <#if langContent.containsSensitiveData || langContent.containsMedicalData>
            <div class="block-wrapper">
                <h4><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul class="treatment-body">
                    <#if langContent.containsSensitiveData>
                        <li><span class="list-value"><@readBundle "containsSensitiveData"></@readBundle></span></li>
                    </#if>
                    <#if langContent.containsMedicalData>
                        <li><span class="list-value"><@readBundle "containsMedicalData"></@readBundle></span></li>
                    </#if>
                </ul>
            </div>
        </#if>

        <#-- Third parties -->
        <#if langContent.thirdParties?has_content>
            <div class="block-wrapper">
                <h4><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                <ul class="treatment-body">
                    <#list langContent.thirdParties as thirdParty>
                        <li><span class="list-label">${thirdParty.name} :</span> <span class="list-value">${thirdParty.value}</span></li>
                    </#list>
                </ul>
            </div>
        </#if>
    <#else>
        <p><@writeError "missingLocale"></@writeError></p>
    </#if>
</div>