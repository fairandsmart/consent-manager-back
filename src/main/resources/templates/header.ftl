<#if data.header?has_content>
    <input name="header" value="${data.header.identifier}" hidden/>
    <#-- Cette ligne est inutile puisqu'elle est déjà appliquée dans logo.ftl, qui précède header.ftl dans tous les templates.
    Si cela venait à changer, il faudrait rétablir cette ligne.
    <@fetchMultiLangContent data.header></@fetchMultiLangContent> -->

    <div class="header">
        <h2><@valueOrError langContent.title "missingValue"></@valueOrError></h2>

        <p><@valueOrError langContent.body "missingValue"></@valueOrError></p>

        <#if langContent.footer?has_content>
            <a href="${langContent.footer}"><@readBundle "readMore" "missingValue"></@readBundle></a>
        </#if>
    </div>
</#if>