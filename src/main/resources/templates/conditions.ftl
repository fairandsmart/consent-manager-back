<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "macros/lang-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <link rel="stylesheet" type="text/css" href="/assets/css/common.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/conditions.css">

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
        <div class="conditions">${langContent.body}</div>

        <#if !data.preview>
            <div class="buttons-wrapper">
                <input name="token" id="token" value="${data.token}" hidden/>
                <input name="${conditions.identifier}" id="choice" value="refused" hidden/>
                <button type="button" class="submit reject" onclick="rejectConditions()">${langContent.rejectLabel}</button>
                <button type="button" class="submit accept" onclick="acceptConditions()">${langContent.acceptLabel}</button>
            </div>
            <button type="submit" id="submit" hidden></button>
        </#if>
    </form>
<#else>
    <div class="conditions-wrapper">
        <h1><@writeError "missingValue"></@writeError></h1>
    </div>
</#if>
</body>

<script src="/assets/js/conditions.js"></script>
</html>