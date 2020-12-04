<#if email?is_hash>
    <div class="metadata-list">
        <div class="metadata">
            <label class="metadata-label"><@readBundle "email_sender" "missingValue"></@readBundle></label>
            <div class="metadata-content"><@valueOrError email.sender "missingValue"></@valueOrError></div>
        </div>
        <div class="metadata">
            <label class="metadata-label"><@readBundle "email_subject" "missingValue"></@readBundle></label>
            <div class="metadata-content"><@valueOrError email.subject "missingValue"></@valueOrError></div>
        </div>
    </div>

    <div class="email-content">
        <#if theme?? && theme?is_hash && theme.logoPosition?has_content>
            <#assign position=theme.logoPosition?lower_case>
        <#else>
            <#assign position="center">
        </#if>

        <div class="logo-wrapper" style="text-align: ${position};">
            <#if theme?? && theme?is_hash && theme.logoPath?has_content>
                <img class="logo" src="${theme.logoPath}" alt="${theme.logoAltText}">
            </#if>
        </div>

        <h1 class="email-title"><@valueOrError email.title "missingValue"></@valueOrError></h1>
        <div class="email-body"><@valueOrError email.body "missingValue"></@valueOrError></div>
        <div class="email-button-wrapper">
            <div class="email-button">
                <a id="form-url"><@valueOrError email.buttonLabel "missingValue"></@valueOrError></a>
            </div>
        </div>
        <div class="email-footer"><@valueOrError email.footer "missingValue"></@valueOrError></div>
        <div class="email-signature"><@valueOrError email.signature "missingValue"></@valueOrError></div>
    </div>
<#else>
    <div>
        <p><@writeError "missingLanguage"></@writeError></p>
    </div>
</#if>
