<div class="block-container controller-container">
    <h4 class="block-title accordion-header" onclick="toggleAccordion('${dataControllerId}')">
        <@readBundle "defaultDataControllerTitle"></@readBundle> <span>+</span>
    </h4>

    <ul id="${dataControllerId}" class="block-body accordion-hidden">
        <#if dataController.company?has_content>
            <li>
                <span class="list-label"><@readBundle "controllerCompanyLabel"></@readBundle></span>
                <span class="list-value">${dataController.company?html}</span>
            </li>
        </#if>
        <#if dataController.info?has_content>
            <li>
                <span class="list-label"><@readBundle "controllerInfoLabel"></@readBundle></span>
                <span class="list-value">${dataController.info?html}</span>
            </li>
        </#if>
        <#if dataController.address?has_content>
            <li>
                <span class="list-label"><@readBundle "controllerAddressLabel"></@readBundle></span>
                <span class="list-value">${dataController.address?html}</span>
            </li>
        </#if>
        <#if dataController.email?has_content>
            <li>
                <span class="list-label"><@readBundle "controllerEmailLabel"></@readBundle></span>
                <span class="list-value">${dataController.email?html}</span>
            </li>
        </#if>
        <#if dataController.phone?has_content>
            <li>
                <span class="list-label"><@readBundle "controllerPhoneLabel"></@readBundle></span>
                <span class="list-value">${dataController.phone?html}</span>
            </li>
        </#if>
    </ul>
</div>
