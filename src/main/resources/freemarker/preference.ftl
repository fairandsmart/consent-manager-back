<div class="treatment">
    <@fetchMultiLangContent element></@fetchMultiLangContent>

    <#if langContent?is_hash && langContent?has_content>
    <#-- Header -->
        <div class="treatment-header">
            <h3><@valueOrError langContent.title "missingValue"></@valueOrError></h3>

            <#assign isChecked=(!data.preview && data.previousValues[element.serial]?has_content && data.previousValues[element.serial]=="accepted")>
            <@toggleSwitch "${element.identifier}" isChecked></@toggleSwitch>
        </div>

    <#-- Data -->
        <div class="item-wrapper">
            <p class="treatment-body"><@valueOrError langContent.body "missingValue"></@valueOrError></p>
        </div>
    <#else>
        <p><@writeError "missingLocale"></@writeError></p>
    </#if>
</div>