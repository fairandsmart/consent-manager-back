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
<#-- Template language -->
<#assign lang=language>

<#-- Returns a <span> with the error message associated to error_key in the bundle -->
<#macro writeError error_key><span class="content-error">${bundle.getString("errorPrefix")} ${bundle.containsKey(error_key)?then(bundle.getString(error_key), error_key)}</span></#macro>

<#-- Returns the sanitized content of var if present, else returns an error message -->
<#macro valueOrError var="" error_key=""><#if var?has_content>${var?html}<#elseif error_key?has_content><@writeError error_key></@writeError></#if></#macro>

<#-- Returns the string associated to key in the bundle, else returns an error message -->
<#macro readBundle key="" error_key=""><#if bundle.containsKey(key)>${bundle.getString(key)}<#elseif error_key?has_content><@writeError error_key></@writeError></#if></#macro>

<#-- Returns the data in the template language from var, else returns an error message -->
<#macro fetchMultiLangContent var={}><#if var.hasLanguage(lang)><#assign langContent=var.getData(lang)><#else><#assign langContent=""></#if></#macro>
