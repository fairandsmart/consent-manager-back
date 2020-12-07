<#include "theme-form-data.ftl">

<div class="left">
    <#include "../components/logo.ftl">
    <div class="content-fade"></div>

    <div class="left-content">
        <#include "../components/info-head.ftl">
    </div>

    <div class="content-fade fade-inverted"></div>
</div>

<div class="right">
    <div class="content-fade"></div>

    <div class="processing-list">
        <#list elements as element>
            <#assign elementContent=element>
            <#assign identifier="element/" + element.type + "/" + element?index>
            <#include "../components/" + element.type + ".ftl">
        </#list>

        <#include "../components/info-foot.ftl">
    </div>
</div>
