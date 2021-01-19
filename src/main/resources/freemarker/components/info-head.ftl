<#if info?is_hash>
    <#if !data.preview>
        <input name="info" value="${infoIdentifier}" hidden/>
    </#if>

    <div class="info-header">
        <h2 class="header-title"><@valueOrError info.title "missingValue"></@valueOrError></h2>

        <p class="header-body"><@valueOrError info.header "missingValue"></@valueOrError></p>

        <#-- Data controller -->
        <#if info.dataControllerVisible && info.dataController?has_content>
            <#assign dataController=info.dataController>
            <#assign dataControllerId=infoIdentifier + "-controller">
            <#include "data-controller.ftl">
        </#if>

        <#-- Other header data -->
        <#if info.jurisdictionVisible || info.collectionMethodVisible || info.scopeVisible || info.shortNoticeLinkVisible>
            <div class="block-container information-container">
                <h4 class="block-title accordion-header" onclick="toggleAccordion('header-infos')">
                    <@readBundle "defaultAdditionalInfoTitle"></@readBundle> <span id="header-infos-symbol" class="accordion-symbol-open"></span>
                </h4>

                <ul id="header-infos" class="block-body accordion-hidden">
                    <#if info.jurisdictionVisible>
                        <li>
                            <span class="list-label"><@readBundle "jurisdictionLabel"></@readBundle></span>
                            <span class="list-value"><@valueOrError info.jurisdiction "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if info.collectionMethodVisible>
                        <li>
                            <span class="list-label"><@readBundle "collectionMethodLabel"></@readBundle></span>
                            <span class="list-value"><@valueOrError info.collectionMethod "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if info.scopeVisible>
                        <li>
                            <span class="list-label"><@readBundle "scopeLabel"></@readBundle></span>
                            <span class="list-value"><@valueOrError info.scope "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if info.shortNoticeLinkVisible>
                        <#if info.shortNoticeLink?has_content>
                            <li>
                                <span class="list-label">
                                    <a <@linkHref info.shortNoticeLink?html data.preview></@linkHref>>
                                        <@readBundle "shortNoticeLinkLabel"></@readBundle>
                                    </a>
                                </span>
                            </li>
                        <#else>
                            <li>
                                <span class="list-label"><@readBundle "shortNoticeLinkLabel"></@readBundle></span>
                                <span class="list-value"><@writeError "missingValue"></@writeError></span>
                            </li>
                        </#if>
                    </#if>
                </ul>
            </div>
        </#if>

        <#if info.privacyPolicyUrl?has_content>
            <div class="privacy-policy-container">
                <a class="privacy-policy" <@linkHref info.privacyPolicyUrl?html data.preview></@linkHref>>
                    <#if info.customPrivacyPolicyText?has_content>
                        ${info.customPrivacyPolicyText?html}
                    <#else>
                        <@readBundle "privacyPolicyButton" "missingValue"></@readBundle>
                    </#if>
                </a>
            </div>
        </#if>
    </div>
<#else>
    <div class="info-header">
        <p><@writeError "missingLanguage"></@writeError></p>
    </div>
</#if>
