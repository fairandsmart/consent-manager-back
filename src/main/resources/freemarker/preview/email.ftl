<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "../macros/lang-macros.ftl">
    <#include "../macros/elements-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <link rel="stylesheet" type="text/css" href="/assets/css/common.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/email.css">

    <title><@readBundle "email_title" "missingValue"></@readBundle></title>
</head>

<body>
    <@fetchMultiLangContent data.optoutEmail></@fetchMultiLangContent>
    <#if langContent?is_hash>
        <div class="metadata-list">
            <div class="metadata">
                <label class="metadata-label"><@readBundle "email_sender" "missingValue"></@readBundle></label>
                <div class="metadata-content">${langContent.sender}</div>
            </div>
            <div class="metadata">
                <label class="metadata-label"><@readBundle "email_subject" "missingValue"></@readBundle></label>
                <div class="metadata-content">${langContent.subject}</div>
            </div>
        </div>

        <div class="fsc-opt-out-wrapper">
            <h1 class="fsc-opt-out-title h2">${langContent.title}</h1>
            <div class="fsc-opt-out-body">${langContent.body}</div>
            <div class="fsc-opt-out-button"><a id="form-url" class="btn btn-primary">${langContent.buttonLabel}</a></div>
            <div class="fsc-opt-out-footer">${langContent.footer}</div>
            <div class="fsc-opt-out-signature">${langContent.signature}</div>
        </div>
    <#else>
        <div>
            <p><@writeError "missingLocale"></@writeError></p>
        </div>
    </#if>

<script src="/assets/js/consent.js"></script>
<script src="/assets/js/iframeresizer/iframeResizer-4.0.4.contentWindow.min.js" crossorigin="" integrity="sha256-36C1/Kln8nS9OWK0+tTRIYQyhdp+eY117441VyJaj+o="></script>

</body>
</html>
