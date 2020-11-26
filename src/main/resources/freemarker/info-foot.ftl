<div class="footer">
    <#if data.info?has_content>
        <@fetchMultiLangContent data.info></@fetchMultiLangContent>

        <#if langContent?is_hash>
            <#if langContent.footer?has_content>
                <div class="footer-body">
                    ${langContent.footer}
                </div>
            </#if>

            <#if data.showAcceptAll && (data.elements?size > 1)>
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
        <#else>
            <div class="footer-body">
                <p><@writeError "missingLanguage"></@writeError></p>
            </div>
        </#if>
    </#if>

    <div class="submit-container">
        <button type="submit" class="submit"
                <#if data.preview>disabled</#if>><@readBundle "submit" "missingValue"></@readBundle></button>
    </div>
</div>