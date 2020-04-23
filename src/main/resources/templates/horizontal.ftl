<!DOCTYPE html>
<html>
<head>
    <title>Horizontal</title>

    <style type="text/css">
    <#include "css/consent.css">
    <#include "css/horizontal.css">
    </style>
</head>

<body>
    <form method="post" id="consent" action="">
        <div class="left">
            <input name="token" value="${data.token}" hidden/>

            <div class="header">
                <h1>Horizontal</h1>

                <input name="header" value="${data.header.entry.key}-${data.header.serial}" hidden/>
                <#assign content=data.header.getData(data.header.defaultLocale)>
                <#include data.header.contentType + ".ftl">
            </div>
        </div>

        <div class="right">
            <div class="close-wrapper">
                <button class="close-btn">×</button>
            </div>

            <#list data.elements as element>
                <#assign content=element.getData(element.defaultLocale)>
                <#include element.contentType + ".ftl">
            </#list>

            <div class="footer">
                <#if data.footer??>
                    <input name="footer" value="${data.footer.entry.key}-${data.footer.serial}" hidden/>
                    <#assign content=data.footer.getData(data.footer.defaultLocale)>
                    <#include data.footer.contentType + ".ftl">
                </#if>

                <div class="submit-container">
                    <button type="submit" class="submit">Valider</button>
                </div>
            </div>
        </div>
    </form>
</body>
</html>