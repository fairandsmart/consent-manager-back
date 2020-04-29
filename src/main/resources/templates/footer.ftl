<div class="footer">
    <#if data.footer??>
        <input name="footer" value="${data.footer.entry.key}-${data.footer.serial}" hidden/>
        <@fetchMultiLangContent data.header></@fetchMultiLangContent>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit"><@readBundle "submit" "missingValue"></@readBundle></button>
    </div>
</div>