<#if data.header?has_content>
    <input name="header" value="${data.header.identifier}" hidden/>
    <#-- Cette ligne est inutile puisqu'elle est déjà appliquée dans logo.ftl, qui précède header.ftl dans tous les templates.
    Si cela venait à changer, il faudrait rétablir cette ligne.
    <@fetchMultiLangContent data.header></@fetchMultiLangContent> -->

    <#if langContent?is_hash>
        <div class="header">
            <h2><@valueOrError langContent.title "missingValue"></@valueOrError></h2>

            <p class="treatment-body"><@valueOrError langContent.body "missingValue"></@valueOrError></p>

            <#-- Data controller -->
            <#if langContent.showDataController>
                <#assign dataController=langContent.dataController>
                <#include "dataController.ftl">
            </#if>

            <#-- Other header data -->
            <#if langContent.showJurisdiction || langContent.showCollectionMethod || langContent.showScope || langContent.showShortNoticeLink>
                <div class="block-wrapper">
                    <h4><@readBundle "defaultAdditionalInfoTitle"></@readBundle></h4>

                    <ul class="treatment-body">
                        <#if langContent.showJurisdiction>
                            <li><@readBundle "headerJurisdictionLabel"></@readBundle> <@valueOrError langContent.jurisdiction "missingValue"></@valueOrError></li>
                        </#if>

                        <#if langContent.showCollectionMethod>
                            <li><@readBundle "headerCollectionMethodLabel"></@readBundle> <@valueOrError langContent.collectionMethod "missingValue"></@valueOrError></li>
                        </#if>

                        <#if langContent.showScope>
                            <li><@readBundle "headerScopeLabel"></@readBundle> <@valueOrError langContent.scope "missingValue"></@valueOrError></li>
                        </#if>

                        <#if langContent.showShortNoticeLink>
                            <li><@readBundle "headerShortNoticeLinkLabel"></@readBundle> <@valueOrError langContent.shortNoticeLink "missingValue"></@valueOrError></li>
                        </#if>
                    </ul>
                </div>
            </#if>

            <#if langContent.privacyPolicyUrl?has_content>
                <p>
                    <a href="${langContent.privacyPolicyUrl}">
                        <#if langContent.customPrivacyPolicyText?has_content>
                            ${langContent.customPrivacyPolicyText}
                        <#else>
                            <@readBundle "privacyPolicyButton" "missingValue"></@readBundle>
                        </#if>
                    </a>
                </p>
            </#if>
        </div>
    <#else>
        <div class="header">
            <p><@writeError "missingLocale"></@writeError></p>
        </div>
    </#if>
</#if>
