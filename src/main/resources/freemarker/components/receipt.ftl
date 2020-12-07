<div class="receipt-title"><@readBundle "receiptBodyTitle"></@readBundle></div>
<div class="receipt-date spaced"><@readBundle "created"></@readBundle>: ${receipt.date}</div>
<div class="receipt-date spaced"><@readBundle "expires"></@readBundle>: ${receipt.expirationDate}</div>

<#assign languageBundleKey="language_" + language>
<div>
    <span class="receipt-label"><@readBundle "language"></@readBundle>: </span>
    <@readBundle languageBundleKey "unknown language key"></@readBundle>
</div>
<div class="spaced">
    <span class="receipt-label"><@readBundle "receipt_id"></@readBundle>: </span>
    #${receipt.transaction}
</div>

<div class="spaced">
    <span class="receipt-label"><@readBundle "collection_method"></@readBundle>: </span>
    <#if receipt.collectionMethod??>${receipt.collectionMethod}<#else>?</#if>
</div>

<#list receipt.consents as consent>
    <div>
        <span class="receipt-label"><@readBundle "data_collected"></@readBundle>: </span>
        ${consent.data}
    </div>
    <#if consent.retentionLabel??>
        <div>
            <span class="receipt-label"><@readBundle "data_retention"></@readBundle>: </span>
            ${consent.retentionLabel} ${consent.retentionValue} <@readBundle consent.retentionUnit "missingValue"></@readBundle>.
        </div>
    </#if>
    <div>
        <span class="receipt-label"><@readBundle "data_usage"></@readBundle>: </span>
        ${consent.usage}
    </div>
    <div class="spaced consent-value">
        <span class="receipt-label"><@readBundle "subject_consent"></@readBundle>: </span>
        <@readBundle consent.value></@readBundle>
    </div>
</#list>

<div class="spaced">
    <span class="receipt-label"><@readBundle "subject_id"></@readBundle>: </span>
    ${receipt.subject}
</div>

<#if receipt.dataController?? && receipt.dataController.name?? && receipt.dataController.company??>
    <div>
        <span class="receipt-label"><@readBundle "data_controller_name"></@readBundle>: </span>
        ${receipt.dataController.name}
    </div>
    <div>
        <span class="receipt-label"><@readBundle "data_controller_details"></@readBundle>: </span>
        ${receipt.dataController.company} ${receipt.dataController.address} ${receipt.dataController.email} ${receipt.dataController.phoneNumber}
    </div>
</#if>
<div>
    <span class="receipt-label"><@readBundle "privacy_policy"></@readBundle>: </span>
    <#if receipt.privacyPolicyUrl??>${receipt.privacyPolicyUrl}<#else>?</#if>
</div>