<#--
 #%L
 Right Consents, a Universal Consents & Preferences Management Platform.
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This file is part of Right Consents Community Edition.
 
 Right Consents Community Edition is published by FAIR AND SMART under the
 GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 
 For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 files, or see https://www.fairandsmart.com/opensource/.
 #L%
-->
<#include "theme-form-data.ftl">

<#include "../components/logo.ftl">

<div class="elements-list">
    <#include "../components/info-head.ftl">

    <#list elements as element>
        <#assign elementContent=element>
        <#assign identifier="element/" + element.type + "/" + element?index>
        <#include "../components/" + element.type + ".ftl">
    </#list>

    <#include "../components/info-foot.ftl">
</div>
