<#if info?is_hash>
    <#if !data.preview>
        <input name="info" value="${infoIdentifier}" hidden/>
    </#if>

    <div class="header">
        <h2 class="header-title"><@valueOrError info.title "missingValue"></@valueOrError></h2>

        <p class="header-body"><@valueOrError info.header "missingValue"></@valueOrError></p>

        <#-- Data controller -->
        <#if info.showDataController && info.dataController?has_content>
            <#assign dataController=info.dataController>
            <#assign dataControllerId=infoIdentifier + "-controller">
            <#include "data-controller.ftl">
        </#if>

        <#-- Other header data -->
        <#if info.showJurisdiction || info.showCollectionMethod || info.showScope || info.showShortNoticeLink>
            <div class="block-wrapper">
                <h4 class="controller-header" onclick="toggleAccordion('header-infos')">
                    <@readBundle "defaultAdditionalInfoTitle"></@readBundle> <span>+</span>
                </h4>

                <ul id="header-infos" class="processing-body controller-hidden">
                    <#if info.showJurisdiction>
                        <li>
                            <span class="list-label"><@readBundle "jurisdictionLabel"></@readBundle></span>
                            <span class="list-value"><@valueOrError info.jurisdiction "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if info.showCollectionMethod>
                        <li>
                            <span class="list-label"><@readBundle "collectionMethodLabel"></@readBundle></span>
                            <span class="list-value"><@valueOrError info.collectionMethod "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if info.showScope>
                        <li>
                            <span class="list-label"><@readBundle "scopeLabel"></@readBundle></span>
                            <span class="list-value"><@valueOrError info.scope "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if info.showShortNoticeLink>
                        <#if info.shortNoticeLink?has_content>
                            <li>
                                <span class="list-label">
                                    <a <@linkHref info.privacyPolicyUrl data.preview></@linkHref>>
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
            <div class="privacy-policy-link-wrapper">
                <a class="privacy-policy-link" <@linkHref info.privacyPolicyUrl data.preview></@linkHref>>
                    <#if info.customPrivacyPolicyText?has_content>
                        ${info.customPrivacyPolicyText}
                    <#else>
                        <@readBundle "privacyPolicyButton" "missingValue"></@readBundle>
                    </#if>
                </a>
            </div>
        </#if>
    </div>
<#else>
    <div class="header">
        <p><@writeError "missingLanguage"></@writeError></p>
    </div>
</#if>
