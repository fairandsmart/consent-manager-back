<!DOCTYPE html>
<html lang="${locale}">
<head>
    <#include "../macros/lang-macros.ftl">
    <#include "../macros/elements-macros.ftl">
    <#assign model=data.data>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "../style/common-style.ftl">
    <#if model.type=="basicinfo" || model.type=="treatment" || model.type=="preference">
        <#include "../style/consent-style.ftl">
        <#include "../style/vertical-style.ftl">
    </#if>
    <#if model.type=="theme">
        <#include "../style/consent-style.ftl">
        <#if data.orientation=="horizontal">
            <#include "../style/horizontal-style.ftl">
        <#else>
            <#include "../style/vertical-style.ftl">
        </#if>
        <style>${model.css}</style>
    </#if>
    <#if model.type=="conditions">
        <#include "../style/conditions-style.ftl">
    </#if>
    <#if model.type=="email">
        <#include "../style/email-style.ftl">
    </#if>

    <title><@readBundle "preview" "missingValue"></@readBundle></title>
</head>

<body>
<#if model.type=="conditions">
    <div class="conditions-wrapper">
        <#assign conditions=model>
        <#include "conditions.ftl">
    </div>
<#elseif model.type=="email">
    <div>
        <#assign email=model>
        <#include "email.ftl">
    </div>
<#else>
    <div class="consent-form">
        <#if model.type=="basicinfo">
            <#assign info=model>
            <#include "info-logo.ftl">
            <div class="treatments">
                <#include "info-head.ftl">
            </div>
            <#include "info-foot.ftl">
        </#if>
        <#if model.type=="treatment">
            <div class="treatments">
                <#assign element_content=model>
                <#assign identifier="treatment">
                <#include "treatment.ftl">
            </div>
        </#if>
        <#if model.type=="preference">
            <div class="treatments">
                <#assign element_content=model>
                <#assign identifier="preference">
                <#include "preference.ftl">
            </div>
        </#if>
        <#if model.type=="theme">
            <#assign theme=model>
            <#if data.orientation=="horizontal">
                <#include "theme-horizontal.ftl">
            <#else>
                <#include "theme-vertical.ftl">
            </#if>
        </#if>
    </div>
</#if>

<script src="/assets/js/consent.js"></script>
<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js" crossorigin=""
        integrity="sha256-uqJEaigsh7a1ncOLCy3CLi7FboUzLC6zNE5u5dRNVmI="></script>

</body>
</html>
