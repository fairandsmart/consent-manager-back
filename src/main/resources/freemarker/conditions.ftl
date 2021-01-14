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
        <title>${conditions.title?html}</title>
    <#else>
        <title><@readBundle "conditionsPageTitle" "missingValue"></@readBundle></title>
    </#if>
</head>

<body>
<#if conditions?is_hash>
    <form method="post" id="consent" name="consent" action="#" class="conditions-container">
        <#if !data.preview>
            <input name="token" id="token" value="${data.token}" hidden/>
            <input name="${data.elements[0].identifier}" id="choice" value="refused" hidden/>
            <button type="submit" id="submit" hidden></button>
        </#if>

        <#include "components/conditions.ftl">
    </form>
<#else>
    <div class="conditions-container">
        <h1><@writeError "missingValue"></@writeError></h1>
    </div>
</#if>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js" integrity="sha512-bLT0Qm9VnAYZDflyKcBaQ2gg0hSYNQrJ8RilYldYQ1FxQYoCLtUjuuRuZo+fjqhx/qtq/1itJ0C2ejDxltZVFg==" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.2.11/iframeResizer.contentWindow.min.js" integrity="sha512-FOf4suFgz7OrWmBiyyWW48u/+6GaaAFSDHagh2EBu/GH/1+OQSYc0NFGeGeZK0gZ3vuU1ovmzVzD6bxmT4vayg==" crossorigin="anonymous"></script>
<script src="/assets/js/conditions.js"></script>

</body>
</html>
