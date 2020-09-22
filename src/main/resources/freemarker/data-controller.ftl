<div class="block-wrapper">
    <h4><@readBundle "defaultDataControllerTitle"></@readBundle></h4>

    <ul class="treatment-body">
        <#if dataController.name?has_content>
            <li><span class="list-label"><@readBundle "controllerNameLabel"></@readBundle></span> <span
                        class="list-value">${dataController.name}</span></li>
        </#if>
        <#if dataController.company?has_content>
            <li><span class="list-label"><@readBundle "controllerCompanyLabel"></@readBundle></span> <span
                        class="list-value">${dataController.company}</span></li>
        </#if>
        <#if dataController.address?has_content>
            <li><span class="list-label"><@readBundle "controllerAddressLabel"></@readBundle></span> <span
                        class="list-value">${dataController.address}</span></li>
        </#if>
        <#if dataController.email?has_content>
            <li><span class="list-label"><@readBundle "controllerEmailLabel"></@readBundle></span> <span
                        class="list-value">${dataController.email}</span></li>
        </#if>
        <#if dataController.phone?has_content>
            <li><span class="list-label"><@readBundle "controllerPhoneLabel"></@readBundle></span> <span
                        class="list-value">${dataController.phone}</span></li>
        </#if>
        <#if dataController.actingBehalfCompany>
            <li><span class="list-value"><@readBundle "controllerBehalfCompany"></@readBundle></span></li>
        </#if>
    </ul>
</div>