<div class="treatment">
    <div>
        <#if langContent.title ??>
            <h3>${langContent.title}</h3>
        </#if>

        <p>Nous utilisons <b>${langContent.data}</b> pour <b>${langContent.usage}</b>.</p>
        <#if langContent.retention??>
            <p>Nous conservons ces données pendant une durée de <b>${langContent.retention}</b>.</p>
        </#if>
    </div>

    <#assign key="${element.entry.key}-${element.serial}">

    <label class="switch">
      <input type="checkbox" name="consent-${key}" id="consent-${key}" checked>
      <span class="slider"></span>
      <div class="text accept"><@readBundle "accept" "missingValue"></@readBundle></div>
      <div class="text refuse"><@readBundle "refuse" "missingValue"></@readBundle></div>
    </label>
</div>