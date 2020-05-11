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
        <#assign consentValue=data.consents["treatment-" + element.entry.key + "-" + element.serial]>
        <p>Nous utilisons ${currentElement.dataBody} pendant ${currentElement.retentionBody} pour ${currentElement.usageBody} : <#if consentValue == "on">accepté<#else>refusé</#if></p>
        </#list>
    </div>
</body>
</html>