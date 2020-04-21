<!DOCTYPE html>
<html>
<head>
    <title>Horizontal</title>
</head>

<body>
    <h1>This is the horizontal layout</h1>

    <div>
        <p>${headerContent.title}</p>
        <p>${headerContent.body}</p>
        <p>${headerContent.footer}</p>
    </div>

    <form method="post" id="consentForm">
        <#list treatments as treatment>
        <div>
            <label>${treatment.name} (${treatment.description}) :</label><input type="checkbox" name="${treatment.name}">
        </div>
        </#list>
        <button type="submit">Valider</button>
    </form>

    <div>
        <p>${footerContent.title}</p>
        <p>${footerContent.body}</p>
        <p>${footerContent.footer}</p>
    </div>

</body>
</html>