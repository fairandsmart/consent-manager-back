<#include "logo.ftl">

<div class="processing-list">
    <div class="header">
        <h2 class="header-title">
            <@readBundle "receiptBodyTitle" "missingValue"></@readBundle>
        </h2>

        <p class="header-body">-</p>

        <div class="block-wrapper">
            <h4 class="controller-header" <#if data.preview>style="cursor: initial;"</#if>>
                <@readBundle "receiptHeaderTitle" "missingValue"></@readBundle>
            </h4>

            <ul class="processing-body controller-visible">
                <li>
                    <span class="list-label">
                        <@readBundle "subject_id" "missingValue"></@readBundle> :
                    </span>
                    <span class="list-value">
                        <@valueOrError receipt.subject "missingValue"></@valueOrError>
                    </span>
                </li>
                <li>
                    <span class="list-label">
                        <@readBundle "language" "missingValue"></@readBundle> :
                    </span>
                    <span class="list-value">
                        <#if receipt.language?has_content>
                            <@readBundle "language_"+receipt.language "missingValue"></@readBundle>
                        <#else>
                            <@writeError "missingValue"></@writeError>
                        </#if>
                    </span>
                </li>
                <li>
                    <span class="list-label">
                        <@readBundle "collection_method" "missingValue"></@readBundle> :
                    </span>
                    <span class="list-value">
                        <@valueOrError receipt.collectionMethod "missingValue"></@valueOrError>
                    </span>
                </li>
                <li>
                    <span class="list-label">
                        <@readBundle "receiptDate" "missingValue"></@readBundle> :
                    </span>
                    <span class="list-value">
                        <@valueOrError receipt.date "missingValue"></@valueOrError>
                    </span>
                </li>
                <li>
                    <span class="list-label">
                        <@readBundle "receiptExpirationDate" "missingValue"></@readBundle> :
                    </span>
                    <div>
                        <@valueOrError receipt.expirationDate "missingValue"></@valueOrError>
                    </div>
                </li>
                <li>
                    <span class="list-label">
                        <@readBundle "receipt_id" "missingValue"></@readBundle> :
                    </span>
                    <span class="list-value">
                        <@valueOrError receipt.transaction "missingValue"></@valueOrError>
                    </span>
                </li>
            </ul>
        </div>

        <div class="privacy-policy-link-wrapper">
            <a class="privacy-policy-link" <@linkHref receipt.updateUrl data.preview></@linkHref>>
                <@readBundle "receiptUpdateLink" "missingValue"></@readBundle>
            </a>
        </div>
    </div>

    <#list receipt.consents as consent>
        <div class="processing">
            <div class="processing-header">
                <h3>
                    <@valueOrError consent.title "missingValue"></@valueOrError>
                </h3>
                <div class="processing-response ${consent.value}">
                    <@readBundle consent.value "missingValue"></@readBundle>
                </div>
            </div>

            <div class="processing-info">
                <p>
                    <@valueOrError consent.data "missingValue"></@valueOrError>
                </p>
                <p>
                    <@valueOrError consent.retentionLabel "missingValue"></@valueOrError> <@valueOrError consent.retentionValue "missingValue"></@valueOrError> <@readBundle consent.retentionUnit "missingValue"></@readBundle>.
                </p>
                <p>
                    <@valueOrError consent.usage "missingValue"></@valueOrError>
                </p>
                <p>
                    <span><@readBundle "purpose_title"></@readBundle></span>
                    <span><#list consent.purposes as purpose><@readBundle purpose?lower_case></@readBundle><#sep>, </#list></span>
                </p>
            </div>

            <#if consent.containsSensitiveData || consent.containsMedicalData>
                <div class="block-wrapper">
                    <h4>
                        <@readBundle "defaultSensitiveDataTitle"></@readBundle>
                    </h4>

                    <ul class="processing-body">
                        <#if consent.containsSensitiveData>
                            <li>
                                <span class="list-value"><@readBundle "containsSensitiveData"></@readBundle></span>
                            </li>
                        </#if>
                        <#if consent.containsMedicalData>
                            <li>
                                <span class="list-value"><@readBundle "containsMedicalData"></@readBundle></span>
                            </li>
                        </#if>
                    </ul>
                </div>
            </#if>

            <#if consent.thirdParties?has_content>
                <div class="block-wrapper">
                    <h4><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                    <ul class="processing-body">
                        <#list consent.thirdParties as thirdParty>
                            <li>
                                <span class="list-label">${thirdParty.name} :</span>
                                <span class="list-value">${thirdParty.value}</span>
                            </li>
                        </#list>
                    </ul>
                </div>
            </#if>

        </div>
    </#list>
</div>

<div class="qr-code">
    <div>
        <img src="${receipt.updateUrlQrCode}" alt="QR code">
    </div>
</div>

<div class="privacy-policy-link-wrapper">
    <#if receipt.dataController?? && receipt.dataController.company?has_content>
        <p>
            <@readBundle "data_controller_name" "missingValue"></@readBundle> :
            <@valueOrError receipt.dataController.company "missingValue"></@valueOrError>
        </p>
    </#if>
    <p>
        <a class="privacy-policy-link" <@linkHref receipt.privacyPolicyUrl data.preview></@linkHref>>
            <@readBundle "privacy_policy" "missingValue"></@readBundle>
        </a>
    </p>
</div>
