<div class="footer">
    <#if data.footer?has_content>
        <input name="footer" value="${data.footer.identifier}" hidden/>
        <@fetchMultiLangContent data.footer></@fetchMultiLangContent>
    </#if>

    <#if langContent.body?has_content>
        <div class="footer-body">
            ${langContent.body}
        </div>
    </#if>

    <#if langContent.showAcceptAll>
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
        <#if data.preview>
            <button type="submit" class="submit" disabled><@readBundle "submit" "missingValue"></@readBundle></button>
        <#else>
            <button type="submit" class="submit" (click)="submitConsent()"><@readBundle "submit" "missingValue"></@readBundle></button>
        </#if>
    </div>
</div>