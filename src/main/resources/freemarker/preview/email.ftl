<#if email?is_hash>
    <div class="metadata-list">
        <div class="metadata">
            <label class="metadata-label"><@readBundle "email_sender" "missingValue"></@readBundle></label>
            <div class="metadata-content">${email.sender}</div>
        </div>
        <div class="metadata">
            <label class="metadata-label"><@readBundle "email_subject" "missingValue"></@readBundle></label>
            <div class="metadata-content">${email.subject}</div>
        </div>
    </div>

    <div class="fsc-opt-out-wrapper">
        <h1 class="fsc-opt-out-title h2">${email.title}</h1>
        <div class="fsc-opt-out-body">${email.body}</div>
        <div class="fsc-opt-out-button"><a id="form-url" class="btn btn-primary">${email.buttonLabel}</a></div>
        <div class="fsc-opt-out-footer">${email.footer}</div>
        <div class="fsc-opt-out-signature">${email.signature}</div>
    </div>
<#else>
    <div>
        <p><@writeError "missingLocale"></@writeError></p>
    </div>
</#if>
