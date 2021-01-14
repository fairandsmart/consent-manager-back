<#include "theme-form-data.ftl">

<div class="left">
    <#include "../components/logo.ftl">
    <#include "../components/info-head.ftl">
</div>

<div class="right">
    <div class="elements-list">
        <#list elements as element>
            <#assign elementContent=element>
            <#assign identifier="element/" + element.type + "/" + element?index>
            <#include "../components/" + element.type + ".ftl">
        </#list>

        <#include "../components/info-foot.ftl">
    </div>
</div>
