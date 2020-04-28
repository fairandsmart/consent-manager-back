<div class="footer">
    <#if data.footer??>
        <input name="footer" value="${data.footer.entry.key}-${data.footer.serial}" hidden/>
        <#assign fContent=data.footer.getData(data.footer.defaultLocale)>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit">Soumettre</button>
    </div>
</div>