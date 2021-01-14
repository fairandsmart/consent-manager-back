<#if theme?? && theme?is_hash && theme.logoPosition?has_content>
    <#assign position=theme.logoPosition?html?lower_case>
<#else>
    <#assign position="center">
</#if>

<div class="logo-container" style="text-align: ${position};">
    <#if theme?? && theme?is_hash && theme.logoPath?has_content>
        <img class="logo" src="${theme.logoPath?html}" alt="${theme.logoAltText?html}">
    </#if>
</div>
