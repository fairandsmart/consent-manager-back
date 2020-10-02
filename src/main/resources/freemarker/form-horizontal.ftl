<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "macros/lang-macros.ftl">
    <#include "macros/elements-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/consent-style.ftl">
    <#include "style/horizontal-style.ftl">
    <#include "theme.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
<form method="post" id="consent" action="#" class="consent-form">
    <input name="token" id="token" value="${data.token}" hidden/>

    <div class="left">
        <#include "header-logo.ftl">

        <div class="left-content">
            <#include "header.ftl">
        </div>

        <div class="content-fade fade-inverted"></div>
    </div>

    <div class="right">
        <div class="close-wrapper">
            <button type="button" class="close-btn">×</button>
        </div>

        <div class="content-fade"></div>

        <div class="treatments">
            <#list data.elements as element>
                <#include element.entry.type + ".ftl">
            </#list>
        </div>

        <#include "footer.ftl">
    </div>
</form>

<script src="/assets/js/consent.js"></script>
<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js" crossorigin=""
        integrity="sha256-uqJEaigsh7a1ncOLCy3CLi7FboUzLC6zNE5u5dRNVmI="></script>

</body>
</html>
