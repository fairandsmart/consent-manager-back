<#if conditions?is_hash>
    <h1><@valueOrError conditions.title "missingValue"></@valueOrError></h1>

    <div class="conditions">${conditions.body}</div>

    <#if data.preview>
        <#assign acceptOptions="disabled style='pointer-events: none !important;'">
        <#assign refuseOptions="disabled style='pointer-events: none !important;'">
    <#else>
        <#assign acceptOptions="onclick='rejectConditions()'">
        <#assign refuseOptions="onclick='acceptConditions()'">
    </#if>

    <div class="buttons-container">
        <button type="button" class="submit-button reject" ${refuseOptions}>
            <#if conditions.rejectLabel?has_content>
                ${conditions.rejectLabel?html}
            <#else>
                <@readBundle "refuse" "missingValue"></@readBundle>
            </#if>
        </button>
        <button type="button" class="submit-button accept" ${acceptOptions}>
            <#if conditions.acceptLabel?has_content>
                ${conditions.acceptLabel?html}
            <#else>
                <@readBundle "accept" "missingValue"></@readBundle>
            </#if>
        </button>
    </div>
<#else>
    <h1><@writeError "missingValue"></@writeError></h1>
</#if>
