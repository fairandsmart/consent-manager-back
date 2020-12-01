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
    <#include "theme.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>
</head>

<body>

<div class="receipt-container">
    <div class="receipt-content">
        <h2><@readBundle "thankYou" "missingValue"></@readBundle></h2>
        <p><@readBundle "changesAcknowledged" "missingValue"></@readBundle></p>
    </div>
    <#if data.receiptURI?has_content>
        <div class="footer">
            <div class="submit-container">
                <a href="${data.receiptURI}" target="_self"><button type="submit" class="submit"><@readBundle "receiptButton" "missingValue"></@readBundle></button></a>
            </div>
        </div>
    </#if>
</div>


<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js" crossorigin=""
        integrity="sha256-VnjX/dNthWqIpTji9AbZLghQx9fdOAw2t4nSgiWLxfM="></script>

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
