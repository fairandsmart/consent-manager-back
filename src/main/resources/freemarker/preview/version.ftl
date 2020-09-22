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
    <#if model.type=="header" || model.type=="treatment" || model.type=="footer">
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
        <#if model.type=="header">
            <#assign header=model>
            <#include "header-logo.ftl">
            <div class="treatments">
                <#include "header.ftl">
            </div>
        </#if>
        <#if model.type=="treatment">
            <div class="treatments">
                <#assign treatment=model>
                <#assign identifier="treatment">
                <#include "treatment.ftl">
            </div>
        </#if>
        <#if model.type=="footer">
            <style>
                .footer {
                    position: absolute;
                    bottom: 0;
                }
            </style>
            <#assign footer=model>
            <#include "footer.ftl">
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
<script src="/assets/js/iframeresizer/iframeResizer-4.0.4.contentWindow.min.js" crossorigin=""
        integrity="sha256-36C1/Kln8nS9OWK0+tTRIYQyhdp+eY117441VyJaj+o="></script>

</body>
</html>
