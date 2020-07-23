<!DOCTYPE html>
<html lang="${data.locale}">
<head>
    <#include "macros/lang-macros.ftl">
    <#include "macros/elements-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <@fetchMultiLangContent data.conditions></@fetchMultiLangContent>
    <title>${langContent.title}</title>
</head>

<body>
    <h1>${langContent.title}</h1>

    <div>${langContent.body}</div>

    <#if data.isForm>
        <div class="buttons-wrapper">
            <button onclick="acceptConditions()">${langContent.acceptLabel}</button>
            <button onclick="rejectConditions()">${langContent.rejectLabel}</button>
        </div>
    </#if>
</body>
</html>