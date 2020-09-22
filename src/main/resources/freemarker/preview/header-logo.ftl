<div class="logo-wrapper">
    <#if header?is_hash && header.logoPath?has_content>
        <img class="logo" src="${header.logoPath}" alt="${header.logoAltText}">
    </#if>
</div>

<div class="content-fade"></div>
