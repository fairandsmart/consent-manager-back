<div class="treatment">
    <div>
        <#if langContent.title?has_content>
            <h3>${langContent.title}</h3>
        </#if>

        <p>Nous utilisons <b><@valueOrError langContent.data "missingValue"></@valueOrError></b> pour <b><@valueOrError langContent.usage "missingValue"></@valueOrError></b>.</p>
        <#if langContent.retention?has_content>
            <p>Nous conservons ces données pendant une durée de <b>${langContent.retention}</b>.</p>
        </#if>
    </div>

    <@toggleSwitch "treatment-${element.entry.key}-${element.serial}"></@toggleSwitch>
</div>