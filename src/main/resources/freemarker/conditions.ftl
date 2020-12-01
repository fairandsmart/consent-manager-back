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
    <#include "theme.ftl">

    <#assign conditions=data.elements[0]>
    <@fetchMultiLangContent conditions></@fetchMultiLangContent>
    <#if langContent?is_hash>
        <title>${langContent.title}</title>
    <#else>
        <title><@readBundle "conditionsPageTitle" "missingValue"></@readBundle></title>
    </#if>
</head>

<body>
<#if langContent?is_hash>
    <form method="post" id="consent" name="consent" action="#" class="conditions-wrapper">
        <h1>${conditions.title}</h1>

        <div class="conditions">${langContent.body}</div>

        <#if !data.preview>
            <div class="buttons-wrapper">
                <input name="token" id="token" value="${data.token}" hidden/>
                <input name="${conditions.identifier}" id="choice" value="refused" hidden/>
                <button type="button" class="submit reject"
                        onclick="rejectConditions()">${langContent.rejectLabel}</button>
                <button type="button" class="submit accept"
                        onclick="acceptConditions()">${langContent.acceptLabel}</button>
            </div>
        </#if>
        <button type="submit" id="submit" hidden></button>
    </form>
<#else>
    <div class="conditions-wrapper">
        <h1><@writeError "missingValue"></@writeError></h1>
    </div>
</#if>

<script src="/assets/js/jquery/jquery-3.5.1.slim.min.js" crossorigin=""
        integrity="sha256-4+XzXVhsDmqanXGHaHvgh1gMQKX40OUvDEBTu8JcmNs="></script>
<script src="/assets/js/conditions.js"></script>
<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js" crossorigin=""
        integrity="sha256-uqJEaigsh7a1ncOLCy3CLi7FboUzLC6zNE5u5dRNVmI="></script>

</body>
</html>
