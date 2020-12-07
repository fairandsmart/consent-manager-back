<!DOCTYPE html>
<html lang="en">
<head>
    <#include "macros/lang-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">

    <title>An error occurred</title>
</head>

<body>

<h1><@writeError "error_" + data.type></@writeError></h1>

<h2>${data.status} - ${data.title}</h2>

<#if data.detail?has_content>
    <div>${data.detail}</div>
</#if>

<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js" crossorigin=""
        integrity="sha256-VnjX/dNthWqIpTji9AbZLghQx9fdOAw2t4nSgiWLxfM="></script>

</body>
