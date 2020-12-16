<div class="footer">
    <#if info?is_hash>
        <#if info.footer?has_content>
            <div class="footer-body">
                ${info.footer}
            </div>
        </#if>
    <#else>
        <div class="footer-body">
            <p><@writeError "missingLanguage"></@writeError></p>
        </div>
    </#if>

    <#if displayAcceptAll>
        <div class="accept-all-container">
            <div class="accept-all-text">
                <#if data.acceptAllText?has_content>
                    ${data.acceptAllText}
                <#else>
                    <@readBundle "acceptAll" "missingValue"></@readBundle>
                </#if>
            </div>

            <@toggleSwitch "accept-all"></@toggleSwitch>
        </div>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit" <#if data.preview>disabled style="pointer-events: none;"</#if>>
            <@readBundle "submit" "missingValue"></@readBundle>
        </button>
    </div>
</div>