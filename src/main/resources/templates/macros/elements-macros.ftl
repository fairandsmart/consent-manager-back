<#-- Crée un toggle switch -->
<#macro toggleSwitch key defaultChecked=false>
    <label class="switch" id="${key}-switch">
        <input type="checkbox" name="${key}" id="${key}" <#if defaultChecked>checked</#if>>
        <span class="slider"></span>
        <div class="text accept"><@readBundle "accept" "missingValue"></@readBundle></div>
        <div class="text refuse"><@readBundle "refuse" "missingValue"></@readBundle></div>
        <select class="switch-select">
            <option id="${key}-accepted" value="accepted" <#if defaultChecked>selected</#if>></option>
            <option id="${key}-refused" value="refused" <#if !defaultChecked>selected</#if>></option>
        </select>
    </label>
</#macro>
