<#if data.header??>
    <input name="header" value="${data.header.entry.key}-${data.header.serial}" hidden/>
    <@fetchMultiLangContent data.header></@fetchMultiLangContent>

    <div class="header">
        <h2>${langContent.title}</h2>

        <p>${langContent.body}</p>

        <a href="${langContent.footer}"><@readBundle "readMore" "missingValue"></@readBundle></a>
    </div>
</#if>