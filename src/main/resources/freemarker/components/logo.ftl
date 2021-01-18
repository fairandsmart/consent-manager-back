<#if theme?? && theme?is_hash && theme.logoPosition?has_content>
    <#assign position=theme.logoPosition?html?lower_case>
<#else>
    <#assign position="center">
</#if>

<#if theme?? && theme?is_hash && theme.logoPath?has_content>
    <div class="logo-container" style="text-align: ${position};">
        <img class="logo" src="${theme.logoPath?html}" alt="${theme.logoAltText?html}">
    </div>
</#if>
