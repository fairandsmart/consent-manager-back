<#if data.header?has_content>
    <input name="header" value="${data.header.identifier}" hidden/>
    <#-- Cette ligne est inutile puisqu'elle est déjà appliquée dans logo.ftl, qui précède header.ftl dans tous les templates.
    Si cela venait à changer, il faudrait rétablir cette ligne.
    <@fetchMultiLangContent data.header></@fetchMultiLangContent> -->

    <#if langContent?is_hash>
        <div class="header">
            <h2 class="header-title"><@valueOrError langContent.title "missingValue"></@valueOrError></h2>

            <p class="header-body"><@valueOrError langContent.body "missingValue"></@valueOrError></p>

            <#-- Data controller -->
            <#if langContent.showDataController>
                <#assign dataController=langContent.dataController>
                <#include "dataController.ftl">
            </#if>

            <#-- Other header data -->
            <#if langContent.showJurisdiction || langContent.showCollectionMethod || langContent.showScope || langContent.showShortNoticeLink>
                <div class="block-wrapper">
                    <h4><@readBundle "defaultAdditionalInfoTitle"></@readBundle></h4>
                    <ul>
                        <#if langContent.showJurisdiction>
                            <li><span class="list-label"><@readBundle "headerJurisdictionLabel"></@readBundle></span> <span class="list-value"><@valueOrError langContent.jurisdiction "missingValue"></@valueOrError></span></li>
                        </#if>
                        <#if langContent.showCollectionMethod>
                            <li><span class="list-label"><@readBundle "headerCollectionMethodLabel"></@readBundle></span> <span class="list-value"><@valueOrError langContent.collectionMethod "missingValue"></@valueOrError></span></li>
                        </#if>
                        <#if langContent.showScope>
                            <li><span class="list-label"><@readBundle "headerScopeLabel"></@readBundle></span> <span class="list-value"><@valueOrError langContent.scope "missingValue"></@valueOrError></span></li>
                        </#if>
                        <#if langContent.showShortNoticeLink>
                            <li><span class="list-label"><@readBundle "headerShortNoticeLinkLabel"></@readBundle></span> <span class="list-value"><@valueOrError langContent.shortNoticeLink "missingValue"></@valueOrError></span></li>
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
            <p><@writeError "missingLocale"></@writeError></p>
        </div>
    </#if>
</#if>
