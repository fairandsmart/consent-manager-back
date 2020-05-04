<div class="footer">
    <#if data.footer?has_content>
        <input name="footer" value="${data.footer.entry.key}-${data.footer.serial}" hidden/>
        <@fetchMultiLangContent data.header></@fetchMultiLangContent>
    </#if>

    <#if data.showAcceptAll?? && data.showAcceptAll>
        <div class="accept-all-container">
            <div>
                <p><@readBundle "acceptAll" "missingValue"></@readBundle></p>
            </div>

            <label class="switch">
              <input type="checkbox" name="treatments-all" id="treatments-all">
              <span class="slider"></span>
              <div class="text accept"><@readBundle "accept" "missingValue"></@readBundle></div>
              <div class="text refuse"><@readBundle "refuse" "missingValue"></@readBundle></div>
            </label>
        </div>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit"><@readBundle "submit" "missingValue"></@readBundle></button>
    </div>
</div>