<#if data.header?has_content>
    <input name="header" value="${data.header.entry.key}-${data.header.serial}" hidden/>
    <@fetchMultiLangContent data.header></@fetchMultiLangContent>

    <div class="header">
        <h2><@valueOrError langContent.title "missingValue"></@valueOrError></h2>

        <p><@valueOrError langContent.body "missingValue"></@valueOrError></p>

        <#if langContent.footer?has_content>
            <a href="${langContent.footer}"><@readBundle "readMore" "missingValue"></@readBundle></a>
        </#if>
    </div>
</#if>