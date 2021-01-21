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
<#include "theme-form-data.ftl">

<div class="left">
    <#include "../components/logo.ftl">
    <#include "../components/info-head.ftl">
</div>

<div class="right">
    <div class="elements-list">
        <#list elements as element>
            <#assign elementContent=element>
            <#assign identifier="element/" + element.type + "/" + element?index>
            <#include "../components/" + element.type + ".ftl">
        </#list>

        <#include "../components/info-foot.ftl">
    </div>
</div>
