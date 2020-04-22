<!DOCTYPE html>
<html>
<head>
    <title>Vertical</title>
</head>

<body>

    <h1>This is the vertical layout</h1>

    <form method="post" id="consent" action="">
        <input name="token" value="${data.token}" hidden/>

        <#if data.header??>
            <input name="header" value="${data.header.entry.key}-${data.header.serial}" hidden/>
            <#assign content=data.header.getData(data.header.defaultLocale)>
            <#include data.header.contentType + ".ftl">
        </#if>

        <#list data.elements as element>
            <#assign content=element.getData(element.defaultLocale)>
            <#include element.contentType + ".ftl">
        </#list>

        <#if data.footer??>
            <input name="footer" value="${data.footer.entry.key}-${data.footer.serial}" hidden/>
            <#assign content=data.footer.getData(data.footer.defaultLocale)>
            <#include data.footer.contentType + ".ftl">
        </#if>

        <button type="submit">Valider</button>
    </form>
</body>
</html>