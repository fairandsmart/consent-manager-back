<#-- Crée un toggle switch -->
<#macro toggleSwitch key isChecked=false acceptText="" refuseText="">
    <label class="switch" id="${key}-switch">
        <input class="consent-checkbox" type="checkbox" id="${key}" <#if isChecked>checked</#if>>
        <span class="switch-slider"></span>
        <#if acceptText=="" || refuseText=="">
            <div class="switch-text accept"><@readBundle "accept" "missingValue"></@readBundle></div>
            <div class="switch-text refuse"><@readBundle "refuse" "missingValue"></@readBundle></div>
            <#assign acceptValue="accepted">
            <#assign refuseValue="refused">
        <#else>
            <div class="switch-text accept">${acceptText}</div>
            <div class="switch-text refuse">${refuseText}</div>
            <#assign acceptValue=acceptText>
            <#assign refuseValue=refuseText>
        </#if>
        <select class="switch-select" name="${key}">
            <option id="${key}-accepted" value="${acceptValue}" <#if isChecked>selected</#if>></option>
            <option id="${key}-refused" value="${refuseValue}" <#if !isChecked>selected</#if>></option>
        </select>
    </label>
</#macro>

<#macro linkHref url isPreview>
    <#if isPreview>
        href='#' style='pointer-events: none !important;'
    <#else>
        href='${url}'
    </#if>
</#macro>