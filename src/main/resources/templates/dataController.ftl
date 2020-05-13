<div class="block-wrapper">
    <h4><@readBundle "defaultDataControllerTitle"></@readBundle></h4>

    <ul>
        <#if dataController.name?has_content>
            <li><@readBundle "controllerNameLabel"></@readBundle> ${dataController.name}</li>
        </#if>
        <#if dataController.company?has_content>
            <li><@readBundle "controllerCompanyLabel"></@readBundle> ${dataController.company}</li>
        </#if>
        <#if dataController.address?has_content>
            <li><@readBundle "controllerAddressLabel"></@readBundle> ${dataController.address}</li>
        </#if>
        <#if dataController.email?has_content>
            <li><@readBundle "controllerEmailLabel"></@readBundle> ${dataController.email}</li>
        </#if>
        <#if dataController.phone?has_content>
            <li><@readBundle "controllerPhoneLabel"></@readBundle> ${dataController.phone}</li>
        </#if>
        <#if dataController.actingBehalfCompany>
            <li><@readBundle "controllerBehalfCompany"></@readBundle></li>
        </#if>
    </ul>
</div>