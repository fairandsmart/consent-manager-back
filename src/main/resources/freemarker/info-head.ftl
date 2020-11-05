<#if data.info?has_content>
    <input name="info" value="${data.info.identifier}" hidden/>
    <@fetchMultiLangContent data.info></@fetchMultiLangContent>

    <#if langContent?is_hash>
        <div class="header">
            <h2 class="header-title"><@valueOrError langContent.title "missingValue"></@valueOrError></h2>

            <p class="header-body"><@valueOrError langContent.header "missingValue"></@valueOrError></p>

            <#-- Data controller -->
            <#if langContent.showDataController>
                <#assign dataController=langContent.dataController>
                <#include "data-controller.ftl">
            </#if>

            <#-- Other header data -->
            <#if langContent.showJurisdiction || langContent.showCollectionMethod || langContent.showScope || langContent.showShortNoticeLink>
                <div class="block-wrapper">
                    <h4><@readBundle "defaultAdditionalInfoTitle"></@readBundle></h4>
                    <ul>
                        <#if langContent.showJurisdiction>
                            <li><span class="list-label"><@readBundle "jurisdictionLabel"></@readBundle></span>
                                <span class="list-value"><@valueOrError langContent.jurisdiction "missingValue"></@valueOrError></span>
                            </li>
                        </#if>
                        <#if langContent.showCollectionMethod>
                            <li>
                                <span class="list-label"><@readBundle "collectionMethodLabel"></@readBundle></span>
                                <span class="list-value"><@valueOrError langContent.collectionMethod "missingValue"></@valueOrError></span>
                            </li>
                        </#if>
                        <#if langContent.showScope>
                            <li><span class="list-label"><@readBundle "scopeLabel"></@readBundle></span> <span
                                        class="list-value"><@valueOrError langContent.scope "missingValue"></@valueOrError></span>
                            </li>
                        </#if>
                        <#if langContent.showShortNoticeLink>
                            <#if langContent.shortNoticeLink?has_content>
                                <li><span class="list-label"><a href="${langContent.shortNoticeLink}"
                                                                <#if data.preview>style="pointer-events: none;"</#if>><@readBundle "shortNoticeLinkLabel"></@readBundle></a></span>
                                </li>
                            <#else>
                                <li>
                                    <span class="list-label"><@readBundle "shortNoticeLinkLabel"></@readBundle></span>
                                    <span class="list-value"><@writeError "missingValue"></@writeError></span></li>
                            </#if>
                        </#if>
                    </ul>
                </div>
            </#if>

            <#if langContent.privacyPolicyUrl?has_content>
                <div class="privacy-policy-link-wrapper">
                    <a class="privacy-policy-link" href="${langContent.privacyPolicyUrl}" <#if data.preview>style="pointer-events: none;"</#if>>
                        <#if langContent.customPrivacyPolicyText?has_content>
                            ${langContent.customPrivacyPolicyText}
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
</#if>
