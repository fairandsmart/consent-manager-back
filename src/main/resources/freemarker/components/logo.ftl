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
<#if theme?? && theme?is_hash && theme.logoPosition?has_content>
    <#assign position=theme.logoPosition?html?lower_case>
<#else>
    <#assign position="center">
</#if>

<#if theme?? && theme?is_hash && theme.logoPath?has_content>
    <div class="logo-container" style="text-align: ${position};">
        <img class="logo" src="${theme.logoPath?html}" alt="${theme.logoAltText?html}">
    </div>
</#if>
