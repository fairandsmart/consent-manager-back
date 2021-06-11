<#--
 #%L
 Right Consents, a Universal Consents & Preferences Management Platform.
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This file is part of Right Consents Community Edition.
 
 Right Consents Community Edition is published by FAIR AND SMART under the
 GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 
 For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 files, or see https://www.fairandsmart.com/opensource/.
 #L%
-->
<#if elementContent?is_hash>
    <h1><@valueOrError elementContent.title "missingValue"></@valueOrError></h1>

    <div class="conditions">${elementContent.body}</div>

    <div class="conditions-response">
        <#if data.previousValues[element.serial]?has_content>
            <div class="processing-response ${data.previousValues[element.serial]?html}">
                <@readBundle data.previousValues[element.serial]?html></@readBundle>
            </div>
        <#else>
            <@toggleSwitch "${identifier}" isChecked></@toggleSwitch>
        </#if>
    </div>
<#else>
    <h1><@writeError "missingValue"></@writeError></h1>
</#if>
