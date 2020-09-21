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
    <link rel="stylesheet" type="text/css" href="/assets/css/consent.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/vertical.css">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
    <form id="consent" class="consent-form">
        <#if data.header??>
            <#include "../header-logo.ftl">
        </#if>

        <div class="treatments">
            <#if data.header??>
                <#include "../header.ftl">
            </#if>

            <#list data.elements as element>
                <@fetchMultiLangContent element></@fetchMultiLangContent>
                <#include "../" + element.entry.type + ".ftl">
            </#list>
        </div>

        <#if data.footer??>
            <#include "../footer.ftl">
        </#if>
    </form>

    <script src="/assets/js/consent.js"></script>
    <script src="/assets/js/iframeresizer/iframeResizer-4.0.4.contentWindow.min.js" crossorigin="" integrity="sha256-36C1/Kln8nS9OWK0+tTRIYQyhdp+eY117441VyJaj+o="></script>

</body>
</html>