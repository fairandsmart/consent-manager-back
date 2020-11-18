<div class="processing">
    <#if element_content?is_hash>
    <#-- Header -->
        <div class="processing-header">
            <h3><@valueOrError element_content.title "missingValue"></@valueOrError></h3>

            <@toggleSwitch "${identifier}"></@toggleSwitch>
        </div>

    <#-- Data -->
        <div class="item-wrapper">
            <p class="processing-body"><@valueOrError element_content.data "missingValue"></@valueOrError></p>
        </div>

    <#-- Retention -->
        <div class="item-wrapper">
            <p class="processing-body">
                <@valueOrError element_content.retentionLabel "missingValue"></@valueOrError> <@valueOrError element_content.retentionValue "missingValue"></@valueOrError> <@readBundle element_content.retentionUnit "missingValue"></@readBundle>.
            </p>
        </div>

    <#-- Usage & purposes -->
        <div class="item-wrapper">
            <p class="processing-body"><@valueOrError element_content.usage "missingValue"></@valueOrError></p>
            <p class="processing-body purpose-container">
                <#list element_content.purposes as purpose>
                    <img class="purpose" src="/assets/img/purpose/${purpose?lower_case}.png"
                         alt="${purpose?lower_case}"/>
                </#list>
            </p>
        </div>

    <#-- Data controller -->
        <#if element_content.showDataController && element_content.dataController?has_content>
            <#assign dataController=element_content.dataController>
            <#assign dataControllerId="processing-controller">
            <#include "../data-controller.ftl">
        </#if>

    <#-- Sensitive data -->
        <#if element_content.containsSensitiveData || element_content.containsMedicalData>
            <div class="block-wrapper">
                <h4><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul class="processing-body">
                    <#if element_content.containsSensitiveData>
                        <li><span class="list-value"><@readBundle "containsSensitiveData"></@readBundle></span></li>
                    </#if>
                    <#if element_content.containsMedicalData>
                        <li><span class="list-value"><@readBundle "containsMedicalData"></@readBundle></span></li>
                    </#if>
                </ul>
            </div>
        </#if>

    <#-- Third parties -->
        <#if element_content.thirdParties?has_content>
            <div class="block-wrapper">
                <h4><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                <ul class="processing-body">
                    <#list element_content.thirdParties as thirdParty>
                        <li><span class="list-label">${thirdParty.name} :</span> <span
                                    class="list-value">${thirdParty.value}</span></li>
                    </#list>
                </ul>
            </div>
        </#if>
    <#else>
        <p><@writeError "missingLanguage"></@writeError></p>
    </#if>
</div>