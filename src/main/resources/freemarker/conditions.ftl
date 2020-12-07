<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "macros/lang-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/conditions-style.ftl">
    <#include "components/theme.ftl">

    <@fetchMultiLangContent data.elements[0]></@fetchMultiLangContent>
    <#assign conditions=langContent>
    <#if conditions?is_hash>
        <title>${conditions.title}</title>
    <#else>
        <title><@readBundle "conditionsPageTitle" "missingValue"></@readBundle></title>
    </#if>
</head>

<body>
<#if conditions?is_hash>
    <form method="post" id="consent" name="consent" action="#" class="conditions-wrapper">
        <#if !data.preview>
            <input name="token" id="token" value="${data.token}" hidden/>
            <input name="${data.elements[0].identifier}" id="choice" value="refused" hidden/>
            <button type="submit" id="submit" hidden></button>
        </#if>

        <#include "components/conditions.ftl">
    </form>
<#else>
    <div class="conditions-wrapper">
        <h1><@writeError "missingValue"></@writeError></h1>
    </div>
</#if>

<script src="/assets/js/conditions.js"></script>
<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js" crossorigin=""
        integrity="sha256-uqJEaigsh7a1ncOLCy3CLi7FboUzLC6zNE5u5dRNVmI="></script>

</body>
</html>
