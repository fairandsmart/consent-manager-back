<div class="treatment">
    <div>Nous utilisons <b>${content.data}</b> afin de <b>${content.usage}</b>.
    <#if content.retention??>
        Nous conservons ces données pendant une durée de <b>${content.retention}</b>.
    </#if>
    </div>

    <#assign key="${element.entry.key}-${element.serial}">
    <div class="switch-container">
        <div class="switch" name="${key}">
            <input type="radio" class="switch-input" name="consent-${key}" value="rejected" id="rejected-${key}" checked>
            <label for="rejected-${key}" class="switch-label switch-label-off">Non</label>
            <input type="radio" class="switch-input" name="consent-${key}" value="accepted" id="accepted-${key}">
            <label for="accepted-${key}" class="switch-label switch-label-on">Oui</label>
            <span class="switch-selection"></span>
        </div>
    </div>
</div>