<#if conditions?is_hash>
    <h1>${conditions.title?html}</h1>

    <div class="conditions">${conditions.body}</div>

    <#if data.preview>
        <#assign acceptOptions="disabled style='pointer-events: none;'">
        <#assign refuseOptions="disabled style='pointer-events: none;'">
    <#else>
        <#assign acceptOptions="onclick='rejectConditions()'">
        <#assign refuseOptions="onclick='acceptConditions()'">
    </#if>

    <div class="buttons-wrapper">
        <button type="button" class="submit reject" ${refuseOptions}>
            <#if conditions.rejectLabel?has_content>
                ${conditions.rejectLabel?html}
            <#else>
                <@readBundle "refuse" "missingValue"></@readBundle>
            </#if>
        </button>
        <button type="button" class="submit accept" ${acceptOptions}>
            <#if conditions.acceptLabel?has_content>
                ${conditions.acceptLabel?html}
            <#else>
                <@readBundle "accept" "missingValue"></@readBundle>
            </#if>
        </button>
    </div>
<#else>
    <div class="conditions-wrapper">
        <h1><@writeError "missingValue"></@writeError></h1>
    </div>
</#if>
