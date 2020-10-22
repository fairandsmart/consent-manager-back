<#-- Crée un toggle switch -->
<#macro toggleSwitch key isChecked=false acceptText="" refuseText="">
    <label class="switch" id="${key}-switch">
        <input class="consent-checkbox" type="checkbox" id="${key}" <#if isChecked>checked</#if>>
        <span class="slider"></span>
        <#if acceptText=="">
            <div class="text accept"><@readBundle "accept" "missingValue"></@readBundle></div>
        <#else>
            <div class="text accept">${acceptText}</div>
        </#if>
        <#if refuseText=="">
            <div class="text refuse"><@readBundle "refuse" "missingValue"></@readBundle></div>
        <#else>
            <div class="text refuse">${refuseText}</div>
        </#if>
        <select class="switch-select" name="${key}">
            <option id="${key}-accepted" value="accepted" <#if isChecked>selected</#if>></option>
            <option id="${key}-refused" value="refused" <#if !isChecked>selected</#if>></option>
        </select>
    </label>
</#macro>
