<div class="footer">
    <#if data.footer?has_content>
        <input name="footer" value="${data.footer.identifier}" hidden/>
        <@fetchMultiLangContent data.footer></@fetchMultiLangContent>
    </#if>

    <#if langContent.showAcceptAll?? && langContent.showAcceptAll>
        <div class="accept-all-container">
            <div class="accept-all-text">
                <#if langContent.customAcceptAllText?has_content>
                    ${langContent.customAcceptAllText}<#else><@readBundle "acceptAll" "missingValue"></@readBundle>
                </#if>
            </div>

            <@toggleSwitch "accept-all"></@toggleSwitch>
        </div>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit"><@readBundle "submit" "missingValue"></@readBundle></button>
    </div>
</div>