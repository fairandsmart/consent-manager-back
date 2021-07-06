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
<#assign theme=receipt.themeInfo>
<#include "logo.ftl">

<div class="elements-list">
    <div class="info-header">
        <h2 class="header-title">
            <@readBundle "receiptBodyTitle" "missingValue"></@readBundle>
        </h2>

        <div class="block-container information-container">
            <h4 class="block-title accordion-header" <#if data.preview>style="cursor: initial;"</#if>>
                <@readBundle "receiptHeaderTitle" "missingValue"></@readBundle>
            </h4>

            <ul class="block-body accordion-visible">
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
                            <@readBundle "language_"+receipt.language?html "missingValue"></@readBundle>
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

        <div class="privacy-policy-container">
            <a class="privacy-policy" <@linkHref receipt.updateUrl?html data.preview></@linkHref>>
                <@readBundle "receiptUpdateLink" "missingValue"></@readBundle>
            </a>
        </div>
    </div>

    <#list receipt.consents as consent>
        <div class="element">
            <div class="element-header">
                <h3 class="element-title">
                    <@valueOrError consent.title "missingValue"></@valueOrError>
                </h3>
                <#if consent.value?has_content>
                    <div class="processing-response ${consent.value?html}">
                        ${consent.value?html}
                    </div>
                <#else>
                    <@writeError "missingValue"></@writeError>
                </#if>
            </div>

            <div class="item-container">
                <p class="item-body">
                    <@valueOrError consent.data "missingValue"></@valueOrError>
                </p>
                <p class="item-body">
                    <@valueOrError consent.retention.label "missingValue"></@valueOrError> <@valueOrError consent.retention.value "missingValue"></@valueOrError> <@readBundle consent.retention.unit?html "missingValue"></@readBundle>.
                </p>
                <p class="item-body">
                    <@valueOrError consent.usage "missingValue"></@valueOrError>
                </p>
                <p class="item-body">
                    <span><@readBundle "purpose_title"></@readBundle></span>
                    <span><#list consent.purposes as purpose><@readBundle purpose?lower_case></@readBundle><#sep>, </#list></span>
                </p>
            </div>

            <#if consent.containsSensitiveData>
                <div class="block-container sensitive-container">
                    <h4 class="block-title"><@readBundle "defaultSensitiveDataTitle"></@readBundle></h4>

                    <ul class="block-body">
                        <li><span class="list-value">
                            <#if consent.containsMedicalData>
                                <@readBundle "containsMedicalData"></@readBundle>
                            <#else>
                                <@readBundle "containsSensitiveData"></@readBundle>
                            </#if>
                        </span></li>
                    </ul>
                </div>
            </#if>

            <#if consent.thirdParties?has_content>
                <div class="block-container third-parties-container">
                    <h4 class="block-title"><@readBundle "defaultThirdPartiesTitle"></@readBundle></h4>

                    <ul class="block-body">
                        <#list consent.thirdParties as thirdParty>
                            <li>
                                <span class="list-label"><@valueOrError thirdParty.name></@valueOrError> :</span>
                                <span class="list-value"><@valueOrError thirdParty.value></@valueOrError></span>
                            </li>
                        </#list>
                    </ul>
                </div>
            </#if>

        </div>
    </#list>
</div>

<div class="qr-code-container">
    <div>
        <img class="qr-code" src="${receipt.updateUrlQrCode}" alt="QR code">
    </div>
</div>

<div class="privacy-policy-container">
    <#if receipt.dataController?? && receipt.dataController.company?has_content>
        <p>
            <@readBundle "data_controller_name" "missingValue"></@readBundle> :
            <@valueOrError receipt.dataController.company "missingValue"></@valueOrError>
        </p>
    </#if>
    <p>
        <a class="privacy-policy" <@linkHref receipt.privacyPolicyUrl?html data.preview></@linkHref>>
            <@readBundle "privacy_policy" "missingValue"></@readBundle>
        </a>
    </p>
</div>
