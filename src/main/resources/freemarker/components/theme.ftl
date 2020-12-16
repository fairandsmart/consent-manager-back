<#if data.theme?has_content>
    <#if !data.preview>
        <input name="theme" value="${data.theme.identifier}" hidden/>
    </#if>
    <@fetchMultiLangContent data.theme></@fetchMultiLangContent>
    <#assign theme=langContent>

    <#if theme?is_hash && theme.css?has_content>
        <style>${theme.css}</style>
    </#if>
<#else>
    <#assign theme="">
</#if>
