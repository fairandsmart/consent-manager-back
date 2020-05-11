<div class="treatment">
    <@fetchMultiLangContent element></@fetchMultiLangContent>

    <#if langContent?has_content>
        <div class="treatment-header">
            <h3><@valueOrError langContent.treatmentTitle "missingValue"></@valueOrError></h3>
            <@toggleSwitch "treatment-${element.entry.key}-${element.serial}"></@toggleSwitch>
        </div>

        <#if langContent.dataTitle?has_content>
            <h4>${langContent.dataTitle}</h4>
        </#if>
        <p><@valueOrError langContent.dataBody "missingValue"></@valueOrError><p>

        <#if langContent.retentionBody?has_content>
            <#if langContent.retentionTitle?has_content>
                <h4>${langContent.retentionTitle}</h4>
            </#if>
            <p>${langContent.retentionBody}<p>
        </#if>

        <#if langContent.usageTitle?has_content>
            <h4>${langContent.usageTitle}</h4>
        </#if>
        <p><@valueOrError langContent.usageBody "missingValue"></@valueOrError><p>
    </#if>
</div>