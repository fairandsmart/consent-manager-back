<#include "theme-form-data.ftl">

<#include "../components/logo.ftl">
<div class="content-fade"></div>

<div class="processing-list">
    <#include "../components/info-head.ftl">

    <#list elements as element>
        <#assign elementContent=element>
        <#assign identifier="element/" + element.type + "/" + element?index>
        <#include "../components/" + element.type + ".ftl">
    </#list>

    <#include "../components/info-foot.ftl">
</div>
