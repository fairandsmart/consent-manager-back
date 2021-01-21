<#--
 #%L
 Right Consents / A Consent Manager Platform
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This program is free software: you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or (at your
 option) any later version.
 
 You accept that the Program was not created with a view to satisfy Your
 individual requirements. Therefore, you must ensure that the Program
 comply with Your needs, requirements and constraints. FAIR AND SMART
 represents and warrants that it holds, without any restriction or
 reservation, all the legal titles, authorizations and intellectual
 property rights granted in the context of the GPLv3 License. See the
 Additional Terms for more details.
 
 You should have received a copy of the GNU General Public License along
 with this program. If not, see <https://www.gnu.org/licenses/>.
 
 You should have received a copy of the Additional Terms along with this
 program. If not, see <https://www.fairandsmart.com/opensource/>.
 #L%
-->
<div class="block-container controller-container">
    <h4 class="block-title accordion-header" onclick="toggleAccordion('${dataControllerId}')">
        <@readBundle "defaultDataControllerTitle"></@readBundle> <span id="${dataControllerId}-symbol" class="accordion-symbol-open"></span>
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
        <#if dataController.phoneNumber?has_content>
            <li>
                <span class="list-label"><@readBundle "controllerPhoneLabel"></@readBundle></span>
                <span class="list-value">${dataController.phoneNumber?html}</span>
            </li>
        </#if>
    </ul>
</div>
