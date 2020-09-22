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
    <#include "style/vertical-style.ftl">
    <#include "theme.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
<form method="post" id="consent" action="#" class="consent-form">
    <input name="token" id="token" value="${data.token}" hidden/>

    <div class="close-wrapper">
        <button type="button" class="close-btn">×</button>
    </div>

    <#include "header-logo.ftl">

    <div class="treatments">
        <#include "header.ftl">

        <#list data.elements as element>
            <#include element.entry.type + ".ftl">
        </#list>
    </div>

    <#include "footer.ftl">
</form>

<script src="/assets/js/consent.js"></script>
<script src="/assets/js/iframeresizer/iframeResizer-4.0.4.contentWindow.min.js" crossorigin=""
        integrity="sha256-36C1/Kln8nS9OWK0+tTRIYQyhdp+eY117441VyJaj+o="></script>

</body>
</html>
