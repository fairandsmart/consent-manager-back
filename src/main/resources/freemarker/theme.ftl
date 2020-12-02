<#if data.theme?has_content>
    <input name="theme" value="${data.theme.identifier}" hidden/>
    <@fetchMultiLangContent data.theme></@fetchMultiLangContent>
    <#assign theme=langContent>

    <#if theme?is_hash && theme.css?has_content>
        <style>${theme.css}</style>
    </#if>
<#else>
    <#assign theme="">
</#if>
