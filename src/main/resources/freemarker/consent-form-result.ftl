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
    <#include "style/consent-form-result-style.ftl">
    <#include "components/theme.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>

<div class="receipt-container">
    <div class="receipt-content">
        <h2><@readBundle "thankYou" "missingValue"></@readBundle></h2>
        <p><@readBundle "changesAcknowledged" "missingValue"></@readBundle></p>
    </div>
    <#if data.receiptURI?has_content>
        <div class="info-footer">
            <div class="submit-container">
                <a href="${data.receiptURI}" target="_self"><button type="submit" class="submit-button"><@readBundle "receiptButton" "missingValue"></@readBundle></button></a>
            </div>
        </div>
    </#if>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.2.11/iframeResizer.contentWindow.min.js" integrity="sha512-FOf4suFgz7OrWmBiyyWW48u/+6GaaAFSDHagh2EBu/GH/1+OQSYc0NFGeGeZK0gZ3vuU1ovmzVzD6bxmT4vayg==" crossorigin="anonymous"></script>

<script type="text/javascript">
    window.iFrameResizer = {
        readyCallback: function () {
            if ('parentIFrame' in window) {
                window.parentIFrame.sendMessage('sent', '*');
                window.parentIFrame.close();
            }
        }
    }
</script>
</body>
</html>
