<!DOCTYPE html>
<html>
<head>
    <title>Vertical</title>
</head>

<body>
    <h1>This is the vertical layout</h1>

<div>
    <p>Header model serial:  ${data.header.serial}</p>
    <p>Header model default language: ${data.header.defaultLocale}</p>
    <p>Header model content type: ${data.header.contentType}</p>

    <#assign currentHeader=data.header.getData(data.header.defaultLocale)>
    <p>${currentHeader.title}</p>
    <p>${currentHeader.body}</p>
    <p>${currentHeader.footer}</p>
</div>

<form method="post" id="consentForm">
    <#list data.elements as element>
    <div>
        <p>Element model serial:  ${element.serial}</p>
        <p>Element model default language: ${element.defaultLocale}</p>
        <p>Element model content type: ${element.contentType}</p>
    </div>
    </#list>
    <button type="submit">Valider</button>
</form>

<div>
    <p>Footer model serial:  ${data.footer.serial}</p>
    <p>Footer model default language: ${data.footer.defaultLocale}</p>
    <p>Footer model content type: ${data.footer.contentType}</p>

    <#assign currentFooter=data.footer.getData(data.footer.defaultLocale)>
    <p>${currentFooter.title}</p>
    <p>${currentFooter.body}</p>
    <p>${currentFooter.footer}</p>
</div>

</body>
</html>