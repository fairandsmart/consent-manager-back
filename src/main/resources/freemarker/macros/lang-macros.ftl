<#-- Langue du modèle -->
<#assign lang=language>

<#-- Crée une balise span contenant le message d'erreur récupéré dans le bundle avec la clé error_key -->
<#macro writeError error_key><span class="content-error">${bundle.getString("errorPrefix")} ${bundle.containsKey(error_key)?then(bundle.getString(error_key), error_key)}</span></#macro>

<#-- Affiche le contenu de var dans la langue du modèle si disponible, sinon affiche le message d'erreur associé à error_key dans le bundle -->
<#macro multiLang var={} error_key=""><#if var[lang]?has_content>${var[lang]?html}<#elseif error_key?has_content><@writeError error_key></@writeError></#if></#macro>

<#-- Affiche le contenu de var si disponible (non nul, non vide), sinon affiche le message d'erreur associé à error_key dans le bundle -->
<#macro valueOrError var="" error_key=""><#if var?has_content>${var?html}<#elseif error_key?has_content><@writeError error_key></@writeError></#if></#macro>

<#-- Affiche la valeur associée à key dans le bundle si disponible, sinon affiche le message d'erreur associé à error_key dans le bundle -->
<#macro readBundle key="" error_key=""><#if bundle.containsKey(key)>${bundle.getString(key)}<#elseif error_key?has_content><@writeError error_key></@writeError></#if></#macro>

<#-- Récupère les données associées à la langue du modèle dans var, sinon affiche le message d'erreur associé à la clé "missingValue" dans le bundle -->
<#macro fetchMultiLangContent var={}><#if var.hasLanguage(lang)><#assign langContent=var.getData(lang)><#else><#assign langContent=""></#if></#macro>
