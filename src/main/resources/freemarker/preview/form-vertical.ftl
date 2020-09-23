<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "../macros/lang-macros.ftl">
    <#include "../macros/elements-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "../style/common-style.ftl">
    <#include "../style/consent-style.ftl">
    <#include "../style/vertical-style.ftl">
    <#if data.theme??>
        <@fetchMultiLangContent data.theme></@fetchMultiLangContent>
        <#assign theme=langContent>
        <#if theme?is_hash>
            <style>${theme.css}</style>
        </#if>
    </#if>

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
<div class="consent-form">

    <#if data.header??>
        <@fetchMultiLangContent data.header></@fetchMultiLangContent>
        <#assign header=langContent>
        <#include "header-logo.ftl">
    </#if>

    <div class="treatments">
        <#if data.header??>
            <#include "header.ftl">
        </#if>

        <#list data.elements as element>
            <@fetchMultiLangContent element></@fetchMultiLangContent>
            <#assign element_content=langContent>
            <#assign identifier=element?index>
            <#include element.entry.type + ".ftl">
        </#list>
    </div>

    <#if data.footer??>
        <@fetchMultiLangContent data.footer></@fetchMultiLangContent>
        <#assign footer=langContent>
        <#include "footer.ftl">
    </#if>
</div>

<script src="/assets/js/consent.js"></script>
<script src="/assets/js/iframeresizer/iframeResizer-4.0.4.contentWindow.min.js" crossorigin=""
        integrity="sha256-36C1/Kln8nS9OWK0+tTRIYQyhdp+eY117441VyJaj+o="></script>

</body>
</html>
