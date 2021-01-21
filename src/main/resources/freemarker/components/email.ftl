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
