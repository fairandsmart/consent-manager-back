<#if data.header??>
    <input name="header" value="${data.header.entry.key}-${data.header.serial}" hidden/>
    <#assign hContent=data.header.getData(data.header.defaultLocale)>

    <div class="header">
        <h3>${hContent.title}</h3>

        <p>${hContent.body}</p>

        <a href="http://www.google.com">${hContent.footer}</a>
    </div>
</#if>