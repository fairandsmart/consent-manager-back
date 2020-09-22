<div class="logo-wrapper">
    <#if data.header?has_content>
        <@fetchMultiLangContent data.header></@fetchMultiLangContent>

        <#if langContent?is_hash && langContent.logoPath?has_content>
            <img class="logo" src="${langContent.logoPath}" alt="${langContent.logoAltText}">
        </#if>
    </#if>
</div>

<div class="content-fade"></div>
