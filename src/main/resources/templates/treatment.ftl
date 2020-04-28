<div class="treatment">
    <div>
        <p>Nous utilisons <b>${content.data}</b> afin de <b>${content.usage}</b>.</p>
        <#if content.retention??>
            <p>Nous conservons ces données pendant une durée de <b>${content.retention}</b>.</p>
        </#if>
    </div>

    <#assign key="${element.entry.key}-${element.serial}">

    <label class="switch">
      <input type="checkbox" name="consent-${key}" id="consent-${key}" checked>
      <span class="slider"></span>
      <div class="text accept">Accepter</div>
      <div class="text refuse">Refuser</div>
    </label>
</div>