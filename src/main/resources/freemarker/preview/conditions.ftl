<#if conditions?is_hash>
    <h1>${conditions.title}</h1>

    <div class="conditions">${conditions.body}</div>

    <div class="buttons-wrapper">
        <button type="button" class="submit reject" disabled style="pointer-events: none;">
            <#if conditions.rejectLabel?has_content>
                ${conditions.rejectLabel}
            <#else>
                <@readBundle "refuse" "missingValue"></@readBundle>
            </#if>
        </button>
        <button type="button" class="submit accept" disabled style="pointer-events: none;">
            <#if conditions.acceptLabel?has_content>
                ${conditions.acceptLabel}
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
