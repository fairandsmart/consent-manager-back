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

    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta charset="UTF-8">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/consent-form-style.ftl">
    <#include "components/theme.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>

<div class="main-container">
    <div class="main-content">
        <h2><@readBundle "thankYou" "missingValue"></@readBundle></h2>
        <p><@readBundle "changesAcknowledged" "missingValue"></@readBundle></p>
        <#if data.context.callback?has_content>
            <p><@readBundle "callbackLoading" "missingValue"></@readBundle></p>
        </#if>
    </div>
    <div class="info-footer">
        <div class="submit-container">
            <a id="receipt-link" href="${data.receipt}?t=${data.token}" target="_self"><button type="submit" class="submit-button"><@readBundle "txReceiptLink" "missingValue"></@readBundle></button></a>
            <a id="breed-link" href="${data.breed}?t=${data.token}" target="_self"><button type="submit" class="submit-button"><@readBundle "txCreateLink" "missingValue"></@readBundle></button></a>
            <#if data.context.callback?has_content>
                <a id="callback-link" href="${data.create}?t=${data.token}" target="_self"><button type="submit" class="submit-button"><@readBundle "txCallbackLink" "missingValue"></@readBundle></button></a>
            </#if>
            <#if data.cpp?has_content>
                <a id="cpp-link" href="${data.cpp}?t=${data.token}" target="_self"><button type="submit" class="submit-button"><@readBundle "cppLink" "missingValue"></@readBundle></button></a>
            </#if>
        </div>
    </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.2.11/iframeResizer.contentWindow.min.js" integrity="sha512-FOf4suFgz7OrWmBiyyWW48u/+6GaaAFSDHagh2EBu/GH/1+OQSYc0NFGeGeZK0gZ3vuU1ovmzVzD6bxmT4vayg==" crossorigin="anonymous"></script>
<script src="/assets/js/callback.js" onload="init('${data.context.iframeOrigin!""}', '${data.context.callback!""}')"></script>

</body>
</html>
