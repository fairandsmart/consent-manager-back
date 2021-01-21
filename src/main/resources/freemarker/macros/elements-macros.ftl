<#--
 #%L
 Right Consents Community Edition
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
