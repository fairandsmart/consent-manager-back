<#if data.info?has_content>
    <@fetchMultiLangContent data.info></@fetchMultiLangContent>
    <#if langContent?is_hash && langContent.logoPath?has_content>
        <div class="logo-wrapper">
            <img class="logo" src="${langContent.logoPath}" alt="${langContent.logoAltText}">
        </div>
        <div class="content-fade"></div>
    </#if>
</#if>
