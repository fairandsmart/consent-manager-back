<!DOCTYPE html>
<html>
<head>
    <title>Vertical</title>
</head>

<body>
    <h1>This is the vertical layout</h1>

    <div>
        <#assign currentHeader=data.header.getData(data.header.defaultLocale)>
        <p>${currentHeader.title}</p>
        <p>${currentHeader.body}</p>
        <p>${currentHeader.footer}</p>
    </div>

    <form method="post" id="consentForm">
        <#list data.elements as element>
        <#assign elementData=element.getData(element.defaultLocale)>
        <div>
            <label>we use ${elementData.data} during ${elementData.retention} for ${elementData.usage}:</label><input type="checkbox" name="plop">
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