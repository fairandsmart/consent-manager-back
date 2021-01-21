<#--
 #%L
 Right Consents / A Consent Manager Platform
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This program is free software: you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or (at your
 option) any later version.
 
 You accept that the Program was not created with a view to satisfy Your
 individual requirements. Therefore, you must ensure that the Program
 comply with Your needs, requirements and constraints. FAIR AND SMART
 represents and warrants that it holds, without any restriction or
 reservation, all the legal titles, authorizations and intellectual
 property rights granted in the context of the GPLv3 License. See the
 Additional Terms for more details.
 
 You should have received a copy of the GNU General Public License along
 with this program. If not, see <https://www.gnu.org/licenses/>.
 
 You should have received a copy of the Additional Terms along with this
 program. If not, see <https://www.fairandsmart.com/opensource/>.
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
