<!DOCTYPE html>
<html>
<head>
    <#include "macros/lang-macros.ftl">
    <#include "macros/elements-macros.ftl">

    <title>Receipt</title>
</head>

<body>
    <div class="receipt-title">CONSENT RECEIPT</div>
    <#assign date=data.timestamp?number_to_datetime/>
    <div>${date?string['dd.MM.yyyy HH:mm:ss ZZZ']}</div>

    <#assign localeLanguageBundleKey="language_" + data.locale>
    <div><span class="receipt-label"><@readBundle "language"></@readBundle>: </span> <@readBundle localeLanguageBundleKey "unknown language key"></@readBundle></div>
    <div><span class="receipt-label"><@readBundle "receipt_id"></@readBundle>: </span> ${data.transaction}</div>
    <div>
        <#list data.records as record>
            <p>Nous utilisons ${record.data} pendant ${record.retention} pour ${record.usage} : ${record.value}</p>
        </#list>
    </div>
</body>
</html>