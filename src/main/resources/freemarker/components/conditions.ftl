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
