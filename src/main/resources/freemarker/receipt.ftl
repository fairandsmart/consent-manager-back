<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "macros/lang-macros.ftl">
    <title><@readBundle "receiptPageTitle" "missingValue"></@readBundle></title>
    <meta charset="UTF-8">
    <#include "style/receipt-style.ftl">
</head>

<body>
<div class="receipt-title"><@readBundle "receiptBodyTitle"></@readBundle></div>
<div class="receipt-date spaced"><@readBundle "created"></@readBundle>: ${data.date}</div>
<div class="receipt-date spaced"><@readBundle "expires"></@readBundle>: ${data.expirationDate}</div>

<#assign languageBundleKey="language_" + locale>
<div>
    <span class="receipt-label"><@readBundle "language"></@readBundle>: </span><@readBundle languageBundleKey "unknown language key"></@readBundle>
</div>
<div class="spaced"><span class="receipt-label"><@readBundle "receipt_id"></@readBundle>: </span>#${data.transaction}
</div>

<div class="spaced"><span
            class="receipt-label"><@readBundle "collection_method"></@readBundle>: </span><#if data.collectionMethod??>${data.collectionMethod}<#else>?</#if>
</div>

<#list data.consents as consent>
    <div><span class="receipt-label"><@readBundle "data_collected"></@readBundle>: </span>${consent.data}</div>
    <#if consent.retentionLabel??>
        <div>
            <span class="receipt-label"><@readBundle "data_retention"></@readBundle>: </span>
            ${consent.retentionLabel} ${consent.retentionValue} <@readBundle consent.retentionUnit "missingValue"></@readBundle>.
        </div></#if>
    <div><span class="receipt-label"><@readBundle "data_usage"></@readBundle>: </span>${consent.usage}</div>
    <div class="spaced consent-value"><span
                class="receipt-label"><@readBundle "subject_consent"></@readBundle>: </span><@readBundle consent.value></@readBundle>
    </div>
</#list>

<div class="spaced"><span class="receipt-label"><@readBundle "subject_id"></@readBundle>: </span>${data.subject}</div>

<div><span class="receipt-label"><@readBundle "issuer_id"></@readBundle>: </span>${data.processor}</div>
<#if data.dataController?? && data.dataController.name?? && data.dataController.company??>
    <div>
        <span class="receipt-label"><@readBundle "data_controller_name"></@readBundle>: </span>${data.dataController.name}
    </div>
    <div><span class="receipt-label"><@readBundle "data_controller_details"></@readBundle>: </span>
        ${data.dataController.company} ${data.dataController.address} ${data.dataController.email} ${data.dataController.phoneNumber}
    </div>
</#if>
<div><span class="receipt-label"><@readBundle "privacy_policy"></@readBundle>: </span><#if data.privacyPolicyUrl??>${data.privacyPolicyUrl}<#else>?</#if></div>

<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js" crossorigin=""
        integrity="sha256-uqJEaigsh7a1ncOLCy3CLi7FboUzLC6zNE5u5dRNVmI="></script>

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
