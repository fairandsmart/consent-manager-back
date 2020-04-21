<!DOCTYPE html>
<html>
<head>
    <title>Receipt</title>
</head>

<body>
    <h1>Receipt</h1>

    <div>
        <#list treatments as item>
        <p>${item.name} : <#if treatmentsConsents.get(index) == 1>accepté<#else>refusé</#if></p>
        </#list>
    </div>
</body>
</html>