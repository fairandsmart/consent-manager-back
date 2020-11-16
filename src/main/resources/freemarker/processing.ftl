<div class="processing">
    <@fetchMultiLangContent element></@fetchMultiLangContent>

    <#if langContent?is_hash && langContent?has_content>
    <#-- Header -->
        <div class="processing-header">
            <h3><@valueOrError langContent.title "missingValue"></@valueOrError></h3>

            <#assign isChecked=(!data.preview && data.previousValues[element.serial]?has_content && data.previousValues[element.serial]=="accepted")>
            <@toggleSwitch "${element.identifier}" isChecked></@toggleSwitch>
        </div>

    <#-- Data -->
        <div class="item-wrapper">
            <p class="processing-body"><@valueOrError langContent.data "missingValue"></@valueOrError></p>
        </div>

    <#-- Retention -->
        <div class="item-wrapper">
            <p class="processing-body">
                <@valueOrError langContent.retention "missingValue"></@valueOrError> <@valueOrError langContent.retentionValue "missingValue"></@valueOrError> <@readBundle langContent.retentionUnit "missingValue"></@readBundle>.
            </p>
        </div>

    <#-- Usage & purposes -->
        <div class="item-wrapper">
            <p class="processing-body"><@valueOrError langContent.usage "missingValue"></@valueOrError></p>
            <p class="processing-body purpose-container">
                <#list langContent.purposes as purpose>
                    <img class="purpose" src="/assets/img/purpose/${purpose?lower_case}.png"
                         alt="${purpose?lower_case}"/>
                </#list>
            </p>
        </div>

    <#-- Data controller -->
        <#if langContent.showDataController && langContent.dataController?has_content>
            <#assign dataController=langContent.dataController>
            <#assign dataControllerId=element.identifier + "-controller">
            <#include "data-controller.ftl">
        </#if>

    <#-- Sensitive data -->
        <#if langContent.containsSensitiveData || langContent.containsMedicalData>
            <div class="block-wrapper">
                <h4><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                <ul class="processing-body">
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

                <ul class="processing-body">
                    <#list langContent.thirdParties as thirdParty>
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