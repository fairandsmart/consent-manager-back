<!DOCTYPE html>
<html>
<head>
    <title>Vertical</title>

    <style type="text/css">
    <#include "css/consent.css">
    <#include "css/vertical.css">
    </style>
</head>

<body>
    <form method="post" id="consent" action="">
        <input name="token" value="${data.token}" hidden/>

        <div class="header">
            <div class="close-wrapper">
                <h1>Vertical</h1>

                <button class="close-btn">×</button>
            </div>

            <#if data.header??>
                <div>
                    <input name="header" value="${data.header.entry.key}-${data.header.serial}" hidden/>
                    <#assign content=data.header.getData(data.header.defaultLocale)>
                    <#include data.header.contentType + ".ftl">
                </div>
            </#if>
        </div>

        <div class="treatments">
            <#list data.elements as element>
                <#assign content=element.getData(element.defaultLocale)>
                <#include element.contentType + ".ftl">
            </#list>
        </div>

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
    </form>
</body>
</html>