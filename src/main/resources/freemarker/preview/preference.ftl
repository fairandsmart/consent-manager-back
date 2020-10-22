<div class="treatment">
    <#if element_content?is_hash>
    <#-- Header -->
        <div class="treatment-header">
            <h3><@valueOrError element_content.label "missingValue"></@valueOrError></h3>

            <#if element_content.valueType=="TOGGLE">
                <@toggleSwitch key=identifier acceptText=element_content.options[1] refuseText=element_content.options[0]></@toggleSwitch>
            </#if>
        </div>

    <#-- Data -->
        <#if element_content.description?has_content>
            <div class="item-wrapper">
                <p class="treatment-body"><@valueOrError element_content.description "missingValue"></@valueOrError></p>
            </div>
        </#if>

        <#if element_content.valueType!="TOGGLE">
            <div class="item-wrapper">

                <#if element_content.valueType=="CHECKBOXES">
                    <ul style="list-style-type: none;">
                        <#list element_content.options as option>
                            <li style="margin-top: 4px;">
                                <input type="checkbox" value="${option}" name="${identifier}">${option}
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if element_content.valueType=="RADIO_BUTTONS">
                    <ul style="list-style-type: none;">
                        <#list element_content.options as option>
                            <li style="margin-top: 4px;">
                                <input type="radio" value="${option}" name="${identifier}">${option}
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if element_content.valueType=="LIST_SINGLE" || element_content.valueType=="LIST_MULTI">
                    <select name="${identifier}" <#if element_content.valueType=="LIST_MULTI">multiple</#if>>
                        <#list element_content.options as option>
                            <option value="${option}">${option}</option>
                        </#list>
                    </select>
                </#if>

                <#if element_content.valueType=="FREE_TEXT">
                    <input type="text" id="${identifier}">
                </#if>

            </div>
        </#if>
    <#else>
        <p><@writeError "missingLocale"></@writeError></p>
    </#if>
</div>