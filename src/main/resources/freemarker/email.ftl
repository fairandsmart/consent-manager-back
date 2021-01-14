<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "macros/lang-macros.ftl">
    <#include "macros/elements-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/email-style.ftl">
    <#include "components/theme.ftl">

    <@fetchMultiLangContent data.model></@fetchMultiLangContent>
    <#assign email=langContent>
    <title>${email.subject?html}</title>
</head>

<body>
    <#assign url=data.url?has_content?then(data.url?html,"")>
    <#include "components/email.ftl">
</body>
</html>