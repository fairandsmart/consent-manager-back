<!DOCTYPE html>
<html lang="${data.locale}">
<head>
    <#include "macros/lang-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <link rel="stylesheet" type="text/css" href="/assets/css/common.css">

    <@fetchMultiLangContent data.elements[0]></@fetchMultiLangContent>
    <#if langContent?is_hash>
        <title>${langContent.title}</title>
    <#else>
        <title>Conditions</title>
    </#if>
</head>

<body>
    <#if langContent?is_hash>
        <div>${langContent.body}</div>

        <#if !data.preview>
            <div class="buttons-wrapper">
                <button onclick="acceptConditions()">${langContent.acceptLabel}</button>
                <button onclick="rejectConditions()">${langContent.rejectLabel}</button>
            </div>
        </#if>
    <#else>
        <h1><@writeError "missingValue"></@writeError></h1>
    </#if>
</body>
</html>