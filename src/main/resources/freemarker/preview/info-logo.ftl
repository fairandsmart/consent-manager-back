<div class="logo-wrapper">
    <#if info?is_hash && info.logoPath?has_content>
        <img class="logo" src="${info.logoPath}" alt="${info.logoAltText}">
    </#if>
</div>

<div class="content-fade"></div>
