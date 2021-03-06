<#--
 #%L
 Right Consents, a Universal Consents & Preferences Management Platform.
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This file is part of Right Consents Community Edition.
 
 Right Consents Community Edition is published by FAIR AND SMART under the
 GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 
 For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 files, or see https://www.fairandsmart.com/opensource/.
 #L%
-->
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
    <#include "style/horizontal-style.ftl">
    <#include "components/theme.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
<#if data.preview>
<div class="consent-form">
    <#else>
    <form method="post" id="consent" action="submit?t=${data.token}" class="consent-form">
        </#if>

        <#if data.info?? && data.info?has_content>
            <@fetchMultiLangContent data.info></@fetchMultiLangContent>
            <#assign info=langContent>
            <#assign infoIdentifier=data.info.identifier>
        <#else>
            <#assign info="">
            <#assign infoIdentifier="infos">
        </#if>
        <#assign displayAcceptAll=(data.acceptAllVisible && data.elements?size > 1)>

        <div class="left">
            <#include "components/logo.ftl">
            <#include "components/info-head.ftl">
        </div>

        <div class="right">
            <div class="elements-list">
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
        </div>

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
