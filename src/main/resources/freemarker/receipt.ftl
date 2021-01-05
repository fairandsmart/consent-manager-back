<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "macros/lang-macros.ftl">
    <#include "macros/elements-macros.ftl">

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/consent-style.ftl">
    <#include "style/vertical-style.ftl">
    <#include "style/receipt-style.ftl">

    <title><@readBundle "receiptPageTitle" "missingValue"></@readBundle></title>
</head>

<body>
<div class="receipt">
    <#assign receipt=data>
    <#include "components/receipt.ftl">
</div>

<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js"></script>

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
