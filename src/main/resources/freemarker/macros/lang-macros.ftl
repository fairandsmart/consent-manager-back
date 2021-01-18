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
