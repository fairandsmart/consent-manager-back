<!DOCTYPE html>
<html lang="${language}">
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
    <#include "components/theme.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
<#if data.preview>
<div class="consent-form">
    <#else>
    <form method="post" id="consent" action="#" class="consent-form">
        <input name="token" id="token" value="${data.token}" hidden/>
        </#if>

        <#if data.info?? && data.info?has_content>
            <@fetchMultiLangContent data.info></@fetchMultiLangContent>
            <#assign info=langContent>
            <#assign infoIdentifier=data.info.identifier>
        <#else>
            <#assign info="">
            <#assign infoIdentifier="infos">
        </#if>
        <#assign displayAcceptAll=(data.showAcceptAll && data.elements?size > 1)>

        <#include "components/logo.ftl">
        <div class="content-fade"></div>

        <div class="processing-list">
            <#include "components/info-head.ftl">

            <#list data.elements as element>
                <@fetchMultiLangContent element></@fetchMultiLangContent>
                <#assign elementContent=langContent>
                <#assign identifier=element.identifier>
                <#include "components/" + element.entry.type + ".ftl">
            </#list>

            <#if !data.footerOnTop>
                <#include "components/info-foot.ftl">
            </#if>
        </div>

        <#if data.footerOnTop>
            <#include "components/info-foot.ftl">
        </#if>

        <#if data.preview>
</div>
<#else>
    </form>
</#if>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js" integrity="sha512-bLT0Qm9VnAYZDflyKcBaQ2gg0hSYNQrJ8RilYldYQ1FxQYoCLtUjuuRuZo+fjqhx/qtq/1itJ0C2ejDxltZVFg==" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.2.11/iframeResizer.contentWindow.min.js" integrity="sha512-FOf4suFgz7OrWmBiyyWW48u/+6GaaAFSDHagh2EBu/GH/1+OQSYc0NFGeGeZK0gZ3vuU1ovmzVzD6bxmT4vayg==" crossorigin="anonymous"></script>
<script src="/assets/js/consent.js"></script>

</body>
</html>
