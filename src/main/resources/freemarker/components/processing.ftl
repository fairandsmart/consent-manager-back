<#--
 #%L
 Right Consents / A Consent Manager Platform
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This program is free software: you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or (at your
 option) any later version.
 
 You accept that the Program was not created with a view to satisfy Your
 individual requirements. Therefore, you must ensure that the Program
 comply with Your needs, requirements and constraints. FAIR AND SMART
 represents and warrants that it holds, without any restriction or
 reservation, all the legal titles, authorizations and intellectual
 property rights granted in the context of the GPLv3 License. See the
 Additional Terms for more details.
 
 You should have received a copy of the GNU General Public License along
 with this program. If not, see <https://www.gnu.org/licenses/>.
 
 You should have received a copy of the Additional Terms along with this
 program. If not, see <https://www.fairandsmart.com/opensource/>.
 #L%
-->
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
        <#if elementContent.dataControllerVisible && elementContent.dataController?has_content>
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
