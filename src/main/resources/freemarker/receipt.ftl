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
    <div class="receipt-title"><@readBundle "receiptBodyTitle"></@readBundle></div>
    <div class="receipt-date spaced"><@readBundle "created"></@readBundle>: ${data.date}</div>
    <div class="receipt-date spaced"><@readBundle "expires"></@readBundle>: ${data.expirationDate}</div>

    <#assign localeLanguageBundleKey="language_" + data.locale>
    <div><span class="receipt-label"><@readBundle "language"></@readBundle>: </span><@readBundle localeLanguageBundleKey "unknown language key"></@readBundle></div>
    <div class="spaced"><span class="receipt-label"><@readBundle "receipt_id"></@readBundle>: </span>#${data.transaction}</div>

    <div class="spaced"><span class="receipt-label"><@readBundle "collection_method"></@readBundle>: </span><#if data.collectionMethod??>${data.collectionMethod}<#else>?</#if></div>

    <#list data.consents as consent>
        <div><span class="receipt-label"><@readBundle "data_collected"></@readBundle>: </span>${consent.data}</div>
        <#if consent.retention??><div><span class="receipt-label"><@readBundle "data_retention"></@readBundle>: </span>${consent.retention}</div></#if>
        <div><span class="receipt-label"><@readBundle "data_usage"></@readBundle>: </span>${consent.usage}</div>
        <div class="spaced consent-value"><span class="receipt-label"><@readBundle "subject_consent"></@readBundle>: </span><@readBundle consent.value></@readBundle></div>
    </#list>

    <div class="spaced"><span class="receipt-label"><@readBundle "subject_id"></@readBundle>: </span>${data.subject}</div>

    <div><span class="receipt-label"><@readBundle "issuer_id"></@readBundle>: </span>${data.processor}</div>
    <#if data.dataController?? && data.dataController.name?? && data.dataController.company??>
        <div><span class="receipt-label"><@readBundle "data_controller_name"></@readBundle>: </span>${data.dataController.name}</div>
        <div><span class="receipt-label"><@readBundle "data_controller_details"></@readBundle>: </span>
            ${data.dataController.company} ${data.dataController.address} ${data.dataController.email} ${data.dataController.phoneNumber}
        </div>
    </#if>
    <div><span class="receipt-label"><@readBundle "privacy_policy"></@readBundle>: </span>${data.privacyPolicyUrl}</div>

    <script src="/assets/js/iframeresizer/iframeResizer-4.0.4.contentWindow.min.js" crossorigin="" integrity="sha256-36C1/Kln8nS9OWK0+tTRIYQyhdp+eY117441VyJaj+o="></script>

    <script type="text/javascript">
    window.iFrameResizer = {
        readyCallback: function(){
            if ('parentIFrame' in window) {
                window.parentIFrame.sendMessage('sent', '*');
                window.parentIFrame.close();
            }
        }
    }
    </script>
</body>
</html>
