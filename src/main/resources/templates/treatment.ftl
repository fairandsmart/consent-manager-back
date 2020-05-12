<div class="treatment">
    <@fetchMultiLangContent element></@fetchMultiLangContent>

    <#if langContent?has_content>
        <#-- Header -->
        <div class="treatment-header">
            <h3><@valueOrError langContent.treatmentTitle "missingValue"></@valueOrError></h3>
            <@toggleSwitch "treatment-${element.entry.key}-${element.serial}"></@toggleSwitch>
        </div>

        <#-- Data -->
        <#if langContent.dataTitle?has_content>
            <h4>${langContent.dataTitle}</h4>
        </#if>
        <p><@valueOrError langContent.dataBody "missingValue"></@valueOrError><p>

        <#-- Retention -->
        <#if langContent.retentionBody?has_content>
            <#if langContent.retentionTitle?has_content>
                <h4>${langContent.retentionTitle}</h4>
            </#if>
            <p>${langContent.retentionBody}<p>
        </#if>

        <#-- Usage & purposes -->
        <#if langContent.usageTitle?has_content>
            <h4>${langContent.usageTitle}</h4>
        </#if>
        <p><@valueOrError langContent.usageBody "missingValue"></@valueOrError><p>
        <p>
            <#list langContent.purposes as purpose>
                <img src="/assets/img/purpose/${purpose?lower_case}.png" width="20" height="20" class="mr-2"/>
            </#list>
        </p>

        <#-- Data controller -->
        <#if langContent.showDataController>
            <#assign dataController=langContent.dataController>
            <#include "dataController.ftl">
        </#if>

        <#-- Sensitive data -->
        <#if langContent.containsSensitiveData || langContent.containsMedicalData>
            <div class="block-wrapper">
                <h4><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul>
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

                <ul>
                    <#list langContent.thirdParties as thirdPartyName, thirdPartyDescription>
                        <li>${thirdPartyName} : ${thirdPartyDescription}</li>
                    </#list>
                </ul>
            </div>
        </#if>
    </#if>
</div>