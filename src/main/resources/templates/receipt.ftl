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
        <p>Nous utilisons ${currentElement.data} pendant ${currentElement.retention} pour ${currentElement.usage} : <#if consentValue == 1>accepté<#else>refusé</#if></p>
        </#list>
    </div>
</body>
</html>