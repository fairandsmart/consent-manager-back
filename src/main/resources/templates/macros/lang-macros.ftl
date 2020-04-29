<#assign lang=locale>
<#assign defLang="en_GB">

<#macro multiLang var={} error_msg="">
    <#if var[lang]?has_content>
        ${var[lang]}
    <#-- <#elseif var[defLang]?has_content>
        ${var[defLang]} -->
    <#elseif error_msg?has_content>
        <span class="fsc-content-error">
            ${bundle.getString("errorPrefix")} ${bundle.containsKey(error_msg)?then(bundle.getString(error_msg), error_msg)}
        </span>
    </#if>
</#macro>

<#macro readBundle key="" error_msg="">
    <#if bundle.containsKey(key)>
        ${bundle.getString(key)}
    <#elseif error_msg?has_content>
        <span class="fsc-content-error">
            ${bundle.getString("errorPrefix")} ${bundle.containsKey(error_msg)?then(bundle.getString(error_msg), error_msg)}
        </span>
    </#if>
</#macro>

<#macro fetchMultiLangContent var={}>
    <#if var.hasLocale(lang)>
        <#assign langContent=var.getData(lang)>
    <#-- <#elseif var[defLang]?has_content>
        <#assign langContent=var[defLang]> -->
    <#else>
        <#assign langContent="error">
        <span class="fsc-content-error">
            ${bundle.getString("errorPrefix")} ${bundle.getString("missingValue")}
        </span>
    </#if>
</#macro>