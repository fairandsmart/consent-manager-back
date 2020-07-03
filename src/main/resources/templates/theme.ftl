<#if data.theme?has_content>
    <input name="theme" value="${data.theme.identifier}" hidden/>
    <@fetchMultiLangContent data.theme></@fetchMultiLangContent>

    <#if langContent?is_hash && langContent.css?has_content>
        <style>${langContent.css}</style>
    </#if>
</#if>
