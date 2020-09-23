<div class="treatment">
    <#if element_content?is_hash>
    <#-- Header -->
        <div class="treatment-header">
            <h3><@valueOrError element_content.title "missingValue"></@valueOrError></h3>

            <@toggleSwitch "${identifier}"></@toggleSwitch>
        </div>

    <#-- Data -->
        <div class="item-wrapper">
            <p class="treatment-body"><@valueOrError element_content.body "missingValue"></@valueOrError></p>
        </div>
    <#else>
        <p><@writeError "missingLocale"></@writeError></p>
    </#if>
</div>