<#if conditions?is_hash>
    <h1>${conditions.title}</h1>

    <div class="conditions">${conditions.body}</div>

    <div class="buttons-wrapper">
        <button type="button" class="submit reject" disabled style="pointer-events: none;">${conditions.rejectLabel}</button>
        <button type="button" class="submit accept" disabled style="pointer-events: none;">${conditions.acceptLabel}</button>
    </div>
<#else>
    <div class="conditions-wrapper">
        <h1><@writeError "missingValue"></@writeError></h1>
    </div>
</#if>
