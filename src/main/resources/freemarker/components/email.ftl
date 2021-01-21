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
<#if email?is_hash>
    <div class="email-content">
        <#include "logo.ftl">

        <h1 class="email-title">
            <#if email.title?has_content>
                ${email.title}
            <#else>
                <@writeError "missingValue"></@writeError>
            </#if>
        </h1>

        <div class="email-body">
            <#if email.body?has_content>
                ${email.body}
            <#else>
                <@writeError "missingValue"></@writeError>
            </#if>
        </div>

        <div class="email-button-container">
            <#if url?has_content>
                <div class="email-button">
                    <a id="form-url" class="email-button-content" <@linkHref url?html data.preview></@linkHref>>
                        <@valueOrError email.buttonLabel "missingValue"></@valueOrError>
                    </a>
                </div>
            </#if>
        </div>

        <div class="email-footer">
            <#if email.footer?has_content>
                ${email.footer}
            <#else>
                <@writeError "missingValue"></@writeError>
            </#if>
        </div>

        <div class="email-signature">
            <#if email.signature?has_content>
                ${email.signature}
            <#else>
                <@writeError "missingValue"></@writeError>
            </#if>
        </div>
    </div>
<#else>
    <div>
        <p><@writeError "missingLanguage"></@writeError></p>
    </div>
</#if>
