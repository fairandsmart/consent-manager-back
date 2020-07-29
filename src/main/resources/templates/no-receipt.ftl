<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "macros/lang-macros.ftl">
    <title><@readBundle "receiptPageTitle" "missingValue"></@readBundle></title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Monaco, "DejaVu Sans Mono", "Lucida Console", "Andale Mono", monospace; font-size: medium; text-transform: uppercase; }
        .spaced { margin-bottom: 18px; }
        .receipt-title { font-weight: bold; }
        .receipt-label { font-weight: bold; }
    </style>
</head>

<body>
    <div class="receipt-title"><@readBundle "noReceipt" "missingValue"></@readBundle></div>
</body>
</html>