<#if email?is_hash>
    <div class="email-content">
        <#include "logo.ftl">

        <h1 class="email-title"><@valueOrError email.title "missingValue"></@valueOrError></h1>
        <div class="email-body"><@valueOrError email.body "missingValue"></@valueOrError></div>
        <div class="email-button-wrapper">
            <#if url?has_content && email.buttonLabel?has_content>
                <div class="email-button">
                    <a id="form-url" class="email-button-content" <@linkHref url data.preview></@linkHref>>
                        <@valueOrError email.buttonLabel "missingValue"></@valueOrError>
                    </a>
                </div>
            </#if>
        </div>
        <div class="email-footer"><@valueOrError email.footer "missingValue"></@valueOrError></div>
        <div class="email-signature"><@valueOrError email.signature "missingValue"></@valueOrError></div>
    </div>
<#else>
    <div>
        <p><@writeError "missingLanguage"></@writeError></p>
    </div>
</#if>