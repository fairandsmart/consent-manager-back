<#-- Crée un toggle switch -->
<#macro toggleSwitch key isChecked=false>
    <label class="switch" id="${key}-switch">
        <input class="consent-checkbox" type="checkbox" id="${key}" <#if isChecked>checked</#if>>
        <span class="slider"></span>
        <div class="text accept"><@readBundle "accept" "missingValue"></@readBundle></div>
        <div class="text refuse"><@readBundle "refuse" "missingValue"></@readBundle></div>
        <select class="switch-select" name="${key}">
            <option id="${key}-accepted" value="accepted" <#if isChecked>selected</#if>></option>
            <option id="${key}-refused" value="refused" <#if !isChecked>selected</#if>></option>
        </select>
    </label>
</#macro>
