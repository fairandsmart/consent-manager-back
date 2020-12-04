<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "macros/lang-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/email-style.ftl">
    <#include "theme.ftl">

    <@fetchMultiLangContent data.model></@fetchMultiLangContent>
    <title>${langContent.subject}</title>
</head>

<body>
    <div class="email-content">
        <#if theme?is_hash && theme.logoPosition?has_content>
            <#assign position=theme.logoPosition?lower_case>
        <#else>
            <#assign position="center">
        </#if>

        <div class="logo-wrapper" style="text-align: ${position};">
            <#if theme?is_hash && theme.logoPath?has_content>
                <img class="logo" src="${theme.logoPath}" alt="${theme.logoAltText}">
            </#if>
        </div>

        <h1 class="email-title">${langContent.title}</h1>
        <div class="email-body">${langContent.body}</div>
        <div class="email-button-wrapper">
            <div class="email-button">
                <a id="form-url" href="${(data.url?has_content)?then(data.url, '#')}">${langContent.buttonLabel}</a>
            </div>
        </div>
        <div class="email-footer">${langContent.footer}</div>
        <div class="email-signature">${langContent.signature}</div>
    </div>
</body>
</html>