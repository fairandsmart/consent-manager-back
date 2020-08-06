<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "macros/lang-macros.ftl">
    <#include "macros/elements-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <link rel="stylesheet" type="text/css" href="/assets/css/common.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/consent.css">
    <link rel="stylesheet" type="text/css" href="/assets/css/vertical.css">
    <#include "theme.ftl">

    <title><@multiLang data.optOutModel.subject></@multiLang></title>
</head>

<body class="fsc-body fsc-opt-out">
<div class="container">
    <div class="col-lg-8 offset-lg-2 col-md-12 fsc-wrapper">
        <h1 class="fsc-opt-out-title h2"><@multiLang data.optOutModel.title></@multiLang></h1>
        <div class="fsc-opt-out-body"><@multiLang data.optOutModel.body "consentOptOutModel.bodyError"></@multiLang></div>
        <div class="fsc-opt-out-button"><a class="btn btn-primary" href="${(data.optoutUrl?has_content)?then(data.optoutUrl, "#")}"><@multiLang data.optOutModel.buttonLabel "consentOptOutModel.buttonLabelError"></@multiLang></a></div>
        <#if hasMultiLang(data.optOutModel.footer)>
            <div class="fsc-opt-out-footer"><@multiLang data.optOutModel.footer></@multiLang></div>
        </#if>
        <#if hasMultiLang(data.optOutModel.signature)>
            <div class="fsc-opt-out-signature"><@multiLang data.optOutModel.signature></@multiLang></div>
        </#if>
    </div>
</div>
</body>
</html>