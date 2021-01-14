<#if email?is_hash>
    <div class="email-content">
        <#include "logo.ftl">

        <h1 class="email-title">${email.title}</h1>
        <div class="email-body">${email.body}</div>
        <div class="email-button-container">
            <#if url?has_content && email.buttonLabel?has_content>
                <div class="email-button">
                    <a id="form-url" class="email-button-content" <@linkHref url?html data.preview></@linkHref>>
                        <@valueOrError email.buttonLabel?html "missingValue"></@valueOrError>
                    </a>
                </div>
            </#if>
        </div>
        <div class="email-footer">${email.footer}</div>
        <div class="email-signature">${email.signature}</div>
    </div>
<#else>
    <div>
        <p><@writeError "missingLanguage"></@writeError></p>
    </div>
</#if>