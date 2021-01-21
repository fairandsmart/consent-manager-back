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
<div class="info-footer">
    <#if info?is_hash>
        <#if info.footer?has_content>
            <div class="footer-body">
                ${info.footer?html}
            </div>
        </#if>
    <#else>
        <div class="footer-body">
            <p><@writeError "missingLanguage"></@writeError></p>
        </div>
    </#if>

    <#if displayAcceptAll>
        <div class="accept-all-container">
            <div class="accept-all-text">
                <#if data.acceptAllText?has_content>
                    ${data.acceptAllText?html}
                <#else>
                    <@readBundle "acceptAll" "missingValue"></@readBundle>
                </#if>
            </div>

            <@toggleSwitch "accept-all"></@toggleSwitch>
        </div>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit-button" <#if data.preview>disabled style="pointer-events: none !important;"</#if>>
            <@readBundle "submit" "missingValue"></@readBundle>
        </button>
    </div>
</div>
