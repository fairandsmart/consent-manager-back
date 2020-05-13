<#if data.header?has_content>
    <input name="header" value="${data.header.identifier}" hidden/>
    <#-- Cette ligne est inutile puisqu'elle est déjà appliquée dans logo.ftl, qui précède header.ftl dans tous les templates.
    Si cela venait à changer, il faudrait rétablir cette ligne.
    <@fetchMultiLangContent data.header></@fetchMultiLangContent> -->

    <div class="header">
        <h2><@valueOrError langContent.title "missingValue"></@valueOrError></h2>

        <p><@valueOrError langContent.body "missingValue"></@valueOrError></p>

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

        <#-- Data controller -->
        <#if langContent.showDataController>
            <#assign dataController=langContent.dataController>
            <#include "dataController.ftl">
        </#if>

        <#if langContent.showJurisdiction || langContent.showCollectionMethod || langContent.showDataController || langContent.showScope>
            <div class="block-wrapper">
                <h4><@readBundle "defaultAdditionalInfoTitle"></@readBundle></h4>

                <ul>
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
    </div>
</#if>