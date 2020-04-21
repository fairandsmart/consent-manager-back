<!DOCTYPE html>
<html>
<head>
    <title>Horizontal</title>
</head>

<body>
    <h1>This is the horizontal layout</h1>

    <div>
        <#assign currentHeader=data.header.getData(data.header.defaultLocale)>
        <p>${currentHeader.title}</p>
        <p>${currentHeader.body}</p>
        <p>${currentHeader.footer}</p>
    </div>

    <form method="post" id="consentForm">
        <#list data.elements as element>
        <div>
            <label>we use ${element.data} during ${element.retention} for ${element.usage}:</label><input type="checkbox" name="plop">
        </div>
        </#list>
        <button type="submit">Valider</button>
    </form>

    <div>
        <#assign currentFooter=data.footer.getData(data.footer.defaultLocale)>
        <p>${currentFooter.title}</p>
        <p>${currentFooter.body}</p>
        <p>${currentFooter.footer}</p>
    </div>

</body>
</html>