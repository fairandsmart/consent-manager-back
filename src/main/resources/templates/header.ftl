<#if data.header?has_content>
    <input name="header" value="${data.header.entry.key}-${data.header.serial}" hidden/>
    <#-- Cette ligne est inutile puisqu'elle est déjà appliquée dans logo.ftl, qui précède header.ftl dans tous les templates.
    Si cela venait à changer, il faudrait rétablir cette ligne.
    <@fetchMultiLangContent data.header></@fetchMultiLangContent> -->

    <div class="header">
        <h2><@valueOrError langContent.title "missingValue"></@valueOrError></h2>

        <p><@valueOrError langContent.body "missingValue"></@valueOrError></p>

        <#if langContent.readMoreLink?has_content>
            <a href="${langContent.readMoreLink}">
                <#if langContent.customReadMoreText?has_content>
                    ${langContent.customReadMoreText}
                <#else>
                    <@readBundle "readMore" "missingValue"></@readBundle>
                </#if>
            </a>
        </#if>
    </div>
</#if>