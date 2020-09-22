<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "macros/lang-macros.ftl">
    <title><@readBundle "receiptPageTitle" "missingValue"></@readBundle></title>
    <meta charset="UTF-8">
    <#include "style/receipt-style.ftl">
</head>

<body>
<div class="receipt-title"><@readBundle "noReceipt" "missingValue"></@readBundle></div>
</body>
</html>