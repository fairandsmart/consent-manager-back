<!DOCTYPE html>
<html>
<head>
    <#include "macros/lang-macros.ftl">

    <title><@readBundle "consentPageTitle" "missingValue"></@readBundle></title>

    <style type="text/css">
    <#include "css/common.css">
    <#include "css/consent.css">
    <#include "css/horizontal.css">
    </style>
</head>

<body>
    <form method="post" id="consent" action="">
        <input name="token" value="${data.token}" hidden/>

        <div class="left">
            <div class="logo-wrapper">
                <img class="logo" src="not_found" alt="logo">
            </div>

            <#include "header.ftl">
        </div>

        <div class="right">
            <div class="close-wrapper">
                <button class="close-btn">×</button>
            </div>

            <div class="treatments">
                <#list data.elements as element>
                    <@fetchMultiLangContent element></@fetchMultiLangContent>
                    <#include element.contentType + ".ftl">
                </#list>
            </div>

            <#include "footer.ftl">
        </div>
    </form>
</body>
</html>