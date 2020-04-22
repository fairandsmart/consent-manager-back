<!DOCTYPE html>
<html>
<head>
    <title>Vertical</title>
</head>

<body>
<h1>This is the vertical layout</h1>

<div>
    <p>Header model serial:  ${data['header'].serial}</p>
    <p>Header model default language: ${data['header'].defaultLocale}</p>
    <p>Header model data type: ${data['header'].content['fr_FR'].data}</p>

</div>

<form method="post" id="consentForm">
    <#list data['elements'] as element>
    <div>
        <p>Element model serial:  ${element.serial}</p>
        <p>Element model default language: ${element.defaultLocale}</p>
    </div>
    </#list>
    <button type="submit">Valider</button>
</form>

<div>
    <p>Footer model serial:  ${data['footer'].serial}</p>
    <p>Footer model default language: ${data['footer'].defaultLocale}</p>
</div>

</body>
</html>