<div>
    <span>Nous utilisons : ${content.data}</span>
    <span>Afin de  : ${content.usage}</span>
    <#if content.retention??>
        <span>Nous conservons ces données pendant une durée de  : ${content.retention}</span>
    </#if>
    <select name="${element.entry.key}-${element.serial}">
        <option value="accepted">oui</option>
        <option value="rejected">non</option>
    </select>
</div>