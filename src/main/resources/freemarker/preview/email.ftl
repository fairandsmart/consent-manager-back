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

    <div class="fsc-opt-out-wrapper">
        <h1 class="fsc-opt-out-title h2"><@valueOrError email.title "missingValue"></@valueOrError></h1>
        <div class="fsc-opt-out-body"><@valueOrError email.body "missingValue"></@valueOrError></div>
        <div class="fsc-opt-out-button"><a id="form-url" class="btn btn-primary"><@valueOrError email.buttonLabel "missingValue"></@valueOrError></a></div>
        <div class="fsc-opt-out-footer"><@valueOrError email.footer "missingValue"></@valueOrError></div>
        <div class="fsc-opt-out-signature"><@valueOrError email.signature "missingValue"></@valueOrError></div>
    </div>
<#else>
    <div>
        <p><@writeError "missingLanguage"></@writeError></p>
    </div>
</#if>
