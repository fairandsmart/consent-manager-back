<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "../macros/lang-macros.ftl">
    <#include "../macros/elements-macros.ftl">
    <#assign model=data.data>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "../style/common-style.ftl">
    <#if model.type=="basicinfo" || model.type=="processing" || model.type=="preference">
        <#include "../style/consent-style.ftl">
        <#include "../style/vertical-style.ftl">
    <#elseif model.type=="theme">
        <#if data.previewType=="EMAIL">
            <#include "../style/email-style.ftl">
        <#else>
            <#include "../style/consent-style.ftl">
            <#if data.orientation=="HORIZONTAL">
                <#include "../style/horizontal-style.ftl">
            <#else>
                <#include "../style/vertical-style.ftl">
            </#if>
            <#if data.previewType=="RECEIPT">
                <#include "../style/receipt-style.ftl">
            </#if>
        </#if>
        <style>${model.css}</style>
    <#elseif model.type=="conditions">
        <#include "../style/conditions-style.ftl">
    <#elseif model.type=="email">
        <#include "../style/email-style.ftl">
    </#if>

    <title><@readBundle "preview" "missingValue"></@readBundle></title>
</head>

<body>
<#if model.type=="conditions">
    <div class="conditions-wrapper">
        <#assign conditions=model>
        <#include "../components/conditions.ftl">
    </div>
<#elseif model.type=="email">
    <div>
        <#assign email=model>
        <#assign url=(email?is_hash && email.buttonLabel?has_content)?then("previewUrl","")>

        <#if email?is_hash>
            <div class="metadata-list">
                <div class="metadata">
                    <label class="metadata-label"><@readBundle "email_sender" "missingValue"></@readBundle></label>
                    <div class="metadata-content"><@valueOrError email.sender "missingValue"></@valueOrError></div>
                </div>
                <div class="metadata">
                    <label class="metadata-label"><@readBundle "email_subject" "missingValue"></@readBundle></label>
                    <div class="metadata-content"><@valueOrError email.subject "missingValue"></@valueOrError></div>
                </div>
            </div>
        </#if>

        <#include "../components/email.ftl">
    </div>
<#elseif model.type=="theme">
    <#assign theme=model>
    <#if data.previewType=="FORM">
    <div class="consent-form">
        <#if data.orientation=="HORIZONTAL">
            <#include "theme-horizontal.ftl">
        <#else>
            <#include "theme-vertical.ftl">
        </#if>
    </div>
    <#elseif data.previewType=="RECEIPT">
        <div class="receipt">
            <#include "theme-receipt.ftl">
        </div>
    <#elseif data.previewType=="EMAIL">
        <#include "theme-email.ftl">
    </#if>
<#else>
    <div class="consent-form">

        <#if model.type=="basicinfo">
            <#assign info=model>
            <#assign infoIdentifier="infos">
            <#assign displayAcceptAll=false>
            <div class="processing-list">
                <#include "../components/info-head.ftl">
            </div>
            <#include "../components/info-foot.ftl">
        <#elseif model.type=="processing">
            <div class="processing-list">
                <#assign elementContent=model>
                <#assign identifier="processing">
                <#include "../components/processing.ftl">
            </div>
        <#elseif model.type=="preference">
            <div class="processing-list">
                <#assign elementContent=model>
                <#assign identifier="preference">
                <#include "../components/preference.ftl">
            </div>
        </#if>

    </div>
</#if>

<script src="/assets/js/jquery/jquery-3.5.1.slim.min.js"></script>
<script src="/assets/js/iframe-resizer-4.2.11/iframeResizer.contentWindow.min.js"></script>
<script src="/assets/js/consent.js"></script>

</body>
</html>
