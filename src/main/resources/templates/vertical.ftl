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

        <div class="close-wrapper">
            <button class="close-btn">×</button>
        </div>

        <div class="logo-wrapper">
            <img class="logo" src="not_found" alt="logo">
        </div>

        <div class="treatments">
            <#include "header.ftl">

            <#list data.elements as element>
                <#assign content=element.getData(element.defaultLocale)>
                <#include element.contentType + ".ftl">
            </#list>
        </div>

        <#include "footer.ftl">
    </form>
</body>
</html>