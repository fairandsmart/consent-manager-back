<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "macros/lang-macros.ftl">
    <@fetchMultiLangContent data.model></@fetchMultiLangContent>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/email-style.ftl">
    <#include "theme.ftl">

    <title>${langContent.subject}</title>
</head>

<body>
<div class="fsc-opt-out-wrapper">
    <h1 class="fsc-opt-out-title h2">${langContent.title}</h1>
    <div class="fsc-opt-out-body">${langContent.body}</div>
    <div class="fsc-opt-out-button">
        <a id="form-url" class="btn btn-primary" href="${(data.url?has_content)?then(data.url, '#')}">${langContent.buttonLabel}</a> </div>
    <div class="fsc-opt-out-footer">${langContent.footer}</div>
    <div class="fsc-opt-out-signature">${langContent.signature}</div>
</div>
</body>
</html>