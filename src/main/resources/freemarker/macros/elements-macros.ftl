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
<#macro toggleSwitch key isChecked=false acceptText="" refuseText="" oneLine=false>
    <#if acceptText=="" || refuseText=="">
        <#assign acceptLabel><@readBundle "accept" "missingValue"></@readBundle></#assign>
        <#assign refuseLabel><@readBundle "refuse" "missingValue"></@readBundle></#assign>
        <#assign acceptValue="accepted">
        <#assign refuseValue="refused">
    <#else>
        <#assign acceptLabel>${acceptText}</#assign>
        <#assign refuseLabel>${refuseText}</#assign>
        <#assign acceptValue=acceptText>
        <#assign refuseValue=refuseText>
    </#if>
    <div class="switch-wrapper">
        <#if oneLine>
        <div class="switch-text refuse switch-inline">${refuseLabel}</div>
        </#if>
        <label class="switch" id="${key}-switch">
            <input class="consent-checkbox" type="checkbox" id="${key}" <#if isChecked>checked</#if>>
            <span class="switch-slider"></span>
            <#if !oneLine>
            <div class="switch-text accept">${acceptLabel}</div>
            <div class="switch-text refuse">${refuseLabel}</div>
            </#if>
            <select class="switch-select" name="${key}">
                <option id="${key}-accepted" value="${acceptValue}" <#if isChecked>selected</#if>></option>
                <option id="${key}-refused" value="${refuseValue}" <#if !isChecked>selected</#if>></option>
            </select>
        </label>
        <#if oneLine>
        <div class="switch-text accept switch-inline">${acceptLabel}</div>
        </#if>
    </div>
</#macro>

<#macro linkHref url isPreview>
    <#if isPreview>
        href='#' style='pointer-events: none !important;'
    <#else>
        href='${url}'
    </#if>
</#macro>
