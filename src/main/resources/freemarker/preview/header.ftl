<#if header?is_hash>
    <div class="header">
        <h2 class="header-title"><@valueOrError header.title "missingValue"></@valueOrError></h2>

        <p class="header-body"><@valueOrError header.body "missingValue"></@valueOrError></p>

        <#-- Data controller -->
        <#if header.showDataController>
            <#assign dataController=header.dataController>
            <#include "../data-controller.ftl">
        </#if>

        <#-- Other header data -->
        <#if header.showJurisdiction || header.showCollectionMethod || header.showScope || header.showShortNoticeLink>
            <div class="block-wrapper">
                <h4><@readBundle "defaultAdditionalInfoTitle"></@readBundle></h4>
                <ul>
                    <#if header.showJurisdiction>
                        <li><span class="list-label"><@readBundle "headerJurisdictionLabel"></@readBundle></span> <span
                                    class="list-value"><@valueOrError header.jurisdiction "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if header.showCollectionMethod>
                        <li><span class="list-label"><@readBundle "headerCollectionMethodLabel"></@readBundle></span>
                            <span class="list-value"><@valueOrError header.collectionMethod "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if header.showScope>
                        <li><span class="list-label"><@readBundle "headerScopeLabel"></@readBundle></span> <span
                                    class="list-value"><@valueOrError header.scope "missingValue"></@valueOrError></span>
                        </li>
                    </#if>
                    <#if header.showShortNoticeLink>
                        <#if header.shortNoticeLink?has_content>
                            <li><span class="list-label"><a href=""
                                                            style="pointer-events: none;"><@readBundle "headerShortNoticeLinkLabel"></@readBundle></a></span>
                            </li>
                        <#else>
                            <li><span class="list-label"><@readBundle "headerShortNoticeLinkLabel"></@readBundle></span>
                                <span class="list-value"><@writeError "missingValue"></@writeError></span></li>
                        </#if>
                    </#if>
                </ul>
            </div>
        </#if>

        <#if header.privacyPolicyUrl?has_content>
            <div class="privacy-policy-link-wrapper">
                <a class="privacy-policy-link" href="" style="pointer-events: none;">
                    <#if header.customPrivacyPolicyText?has_content>
                        ${header.customPrivacyPolicyText}
                    <#else>
                        <@readBundle "privacyPolicyButton" "missingValue"></@readBundle>
                    </#if>
                </a>
            </div>
        </#if>
    </div>
<#else>
    <div class="header">
        <p><@writeError "missingLocale"></@writeError></p>
    </div>
</#if>
