<div class="footer">
    <#if footer?is_hash>
        <#if footer.body?has_content>
            <div class="footer-body">
                ${footer.body}
            </div>
        </#if>

        <#if footer.showAcceptAll>
            <div class="accept-all-container">
                <div class="accept-all-text">
                    <#if footer.customAcceptAllText?has_content>
                        ${footer.customAcceptAllText}<#else><@readBundle "acceptAll" "missingValue"></@readBundle>
                    </#if>
                </div>

                <@toggleSwitch "accept-all"></@toggleSwitch>
            </div>
        </#if>
    <#else>
        <div class="footer-body">
            <p><@writeError "missingLocale"></@writeError></p>
        </div>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit" disabled style="pointer-events: none;"><@readBundle "submit" "missingValue"></@readBundle></button>
    </div>
</div>