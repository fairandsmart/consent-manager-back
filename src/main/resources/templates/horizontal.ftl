<!DOCTYPE html>
<html>
<head>
    <#include "macros/lang-macros.ftl">
    <#include "macros/elements-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <link rel="stylesheet" href="/assets/css/common.css">
    <link rel="stylesheet" href="/assets/css/consent.css">
    <link rel="stylesheet" href="/assets/css/horizontal.css">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
    <form method="post" id="consent" action="">
        <input name="token" value="${data.token}" hidden/>

        <div class="left">
            <#include "logo.ftl">

            <#include "header.ftl">
        </div>

        <div class="right">
            <div class="close-wrapper">
                <button class="close-btn">×</button>
            </div>

            <div class="treatments">
                <#list data.elements as element>
                    <@fetchMultiLangContent element></@fetchMultiLangContent>
                    <#include element.contentType + ".ftl">
                </#list>
            </div>

            <#include "footer.ftl">
        </div>
    </form>
</body>

<script src="/assets/js/consent.js"></script>
<!--<script src="/assets/js/iframeresizer/iframeResizer-4.0.4.contentWindow.min.js"></script>-->
</html>