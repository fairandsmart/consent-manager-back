<div class="treatment">
    <#if treatment?is_hash>
    <#-- Header -->
        <div class="treatment-header">
            <h3><@valueOrError treatment.treatmentTitle "missingValue"></@valueOrError></h3>

            <@toggleSwitch "${identifier}"></@toggleSwitch>
        </div>

    <#-- Data -->
        <div class="item-wrapper">
            <#if treatment.dataTitle?has_content>
                <h4>${treatment.dataTitle}</h4>
            </#if>
            <p class="treatment-body"><@valueOrError treatment.dataBody "missingValue"></@valueOrError></p>
        </div>

    <#-- Retention -->
        <div class="item-wrapper">
            <#if treatment.retentionTitle?has_content>
                <h4>${treatment.retentionTitle}</h4>
            </#if>
            <p class="treatment-body"><@valueOrError treatment.retentionBody "missingValue"></@valueOrError></p>
        </div>

    <#-- Usage & purposes -->
        <div class="item-wrapper">
            <#if treatment.usageTitle?has_content>
                <h4>${treatment.usageTitle}</h4>
            </#if>
            <p class="treatment-body"><@valueOrError treatment.usageBody "missingValue"></@valueOrError></p>
            <p class="treatment-body purpose-container">
                <#list treatment.purposes as purpose>
                    <img class="purpose" src="/assets/img/purpose/${purpose?lower_case}.png"
                         alt="${purpose?lower_case}"/>
                </#list>
            </p>
        </div>

    <#-- Data controller -->
        <#if treatment.showDataController && treatment.dataController?has_content>
            <#assign dataController=treatment.dataController>
            <#include "../data-controller.ftl">
        </#if>

    <#-- Sensitive data -->
        <#if treatment.containsSensitiveData || treatment.containsMedicalData>
            <div class="block-wrapper">
                <h4><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul class="treatment-body">
                    <#if treatment.containsSensitiveData>
                        <li><span class="list-value"><@readBundle "containsSensitiveData"></@readBundle></span></li>
                    </#if>
                    <#if treatment.containsMedicalData>
                        <li><span class="list-value"><@readBundle "containsMedicalData"></@readBundle></span></li>
                    </#if>
                </ul>
            </div>
        </#if>

    <#-- Third parties -->
        <#if treatment.thirdParties?has_content>
            <div class="block-wrapper">
                <h4><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                <ul class="treatment-body">
                    <#list treatment.thirdParties as thirdParty>
                        <li><span class="list-label">${thirdParty.name} :</span> <span
                                    class="list-value">${thirdParty.value}</span></li>
                    </#list>
                </ul>
            </div>
        </#if>
    <#else>
        <p><@writeError "missingLocale"></@writeError></p>
    </#if>
</div>