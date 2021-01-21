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
<#if conditions?is_hash>
    <h1><@valueOrError conditions.title "missingValue"></@valueOrError></h1>

    <div class="conditions">${conditions.body}</div>

    <#if data.preview>
        <#assign acceptOptions="disabled style='pointer-events: none !important;'">
        <#assign refuseOptions="disabled style='pointer-events: none !important;'">
    <#else>
        <#assign acceptOptions="onclick='rejectConditions()'">
        <#assign refuseOptions="onclick='acceptConditions()'">
    </#if>

    <div class="buttons-container">
        <button type="button" class="submit-button reject" ${refuseOptions}>
            <#if conditions.rejectLabel?has_content>
                ${conditions.rejectLabel?html}
            <#else>
                <@readBundle "refuse" "missingValue"></@readBundle>
            </#if>
        </button>
        <button type="button" class="submit-button accept" ${acceptOptions}>
            <#if conditions.acceptLabel?has_content>
                ${conditions.acceptLabel?html}
            <#else>
                <@readBundle "accept" "missingValue"></@readBundle>
            </#if>
        </button>
    </div>
<#else>
    <h1><@writeError "missingValue"></@writeError></h1>
</#if>
