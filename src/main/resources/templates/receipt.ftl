<!DOCTYPE html>
<html>
<head>
    <title>Receipt</title>
</head>

<body>
    <h1>Receipt</h1>

    <div>
        <#list data.elements as element>
        <#assign currentElement=element.getData(element.defaultLocale)>
        <#assign consentValue=data.consents[element.entry.key]>
        <p>using ${currentElement.data} during ${currentElement.retention} for ${currentElement.usage}: <#if consentValue == 1>accepted<#else>refused</#if></p>
        </#list>
    </div>
</body>
</html>