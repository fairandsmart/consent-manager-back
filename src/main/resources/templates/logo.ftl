<div class="logo-wrapper">
    <#if data.header?has_content>
        <#-- CF header.ftl -->
        <@fetchMultiLangContent data.header></@fetchMultiLangContent>

        <#if langContent.logoPath?has_content>
            <img class="logo" src="${langContent.logoPath}" alt="${langContent.logoAltText}">
        </#if>
    </#if>
</div>