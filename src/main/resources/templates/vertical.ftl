<!DOCTYPE html>
<html>
<head>
    <title>Vertical</title>
</head>

<body>
<h1>This is the vertical layout</h1>

<div>
    <p>${data.headerContent.title}</p>
    <p>${data.headerContent.body}</p>
    <p>${data.headerContent.footer}</p>
</div>

<form method="post" id="consentForm">
    <#list data.treatments as treatment>
    <div>
        <label>we use ${treatment.data} during ${treatment.retention} for ${treatment.usage}:</label><input type="checkbox" name="plop">
    </div>
    </#list>
    <button type="submit">Valider</button>
</form>

<div>
    <p>${data.footerContent.title}</p>
    <p>${data.footerContent.body}</p>
    <p>${data.footerContent.footer}</p>
</div>

</body>
</html>