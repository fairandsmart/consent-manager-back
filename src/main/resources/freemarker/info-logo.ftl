<div class="logo-wrapper">
    <#if data.info?has_content>
        <@fetchMultiLangContent data.info></@fetchMultiLangContent>

        <#if langContent?is_hash && langContent.logoPath?has_content>
            <img class="logo" src="${langContent.logoPath}" alt="${langContent.logoAltText}">
        </#if>
    </#if>
</div>

<div class="content-fade"></div>
