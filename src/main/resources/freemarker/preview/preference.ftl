<div class="processing">
    <#if element_content?is_hash>
    <#-- Header -->
        <div class="processing-header">
            <h3><@valueOrError element_content.label "missingValue"></@valueOrError></h3>

            <#if element_content.valueType=="TOGGLE">
                <#if element_content.includeDefault && element_content.defaultValues?has_content && element_content.options?size==2>
                    <#assign isChecked=element_content.defaultValues?seq_contains(element_content.options[1])>
                <#else>
                    <#assign isChecked=false>
                </#if>
                <@toggleSwitch key=identifier isChecked=isChecked acceptText=element_content.options[1]
                refuseText=element_content.options[0]></@toggleSwitch>
            </#if>
        </div>

    <#-- Data -->
        <#if element_content.description?has_content>
            <div class="item-wrapper">
                <p class="processing-body"><@valueOrError element_content.description "missingValue"></@valueOrError></p>
            </div>
        </#if>

        <#if element_content.valueType!="TOGGLE">
            <div class="item-wrapper">

                <#if element_content.valueType=="CHECKBOXES">
                    <ul style="list-style-type: none;">
                        <#list element_content.options as option>
                            <li style="margin-top: 4px;">
                                <input type="checkbox" value="${option}" name="${identifier}"
                                    <#if element_content.includeDefault && element_content.defaultValues?has_content
                                    && element_content.defaultValues?seq_contains(option)>checked</#if>>
                                <label for="${option}">${option}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if element_content.valueType=="RADIO_BUTTONS">
                    <ul style="list-style-type: none;">
                        <#list element_content.options as option>
                            <li style="margin-top: 4px;">
                                <input type="radio" value="${option}" name="${identifier}"
                                    <#if element_content.includeDefault && element_content.defaultValues?has_content
                                    && element_content.defaultValues?seq_contains(option)>checked</#if>>
                                <label for="${option}">${option}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if element_content.valueType=="LIST_SINGLE" || element_content.valueType=="LIST_MULTI">
                    <select name="${identifier}" <#if element_content.valueType=="LIST_MULTI">multiple</#if>>
                        <#list element_content.options as option>
                            <option value="${option}" <#if element_content.includeDefault && element_content.defaultValues?has_content
                            && element_content.defaultValues?seq_contains(option)>selected</#if>>${option}</option>
                        </#list>
                    </select>
                </#if>

                <#if element_content.valueType=="FREE_TEXT">
                    <input type="text" id="${identifier}">
                </#if>

                <#if element_content.valueType=="NONE">
                    <input type="hidden" id="${identifier}">
                </#if>

            </div>
        </#if>
    <#else>
        <p><@writeError "missingLanguage"></@writeError></p>
    </#if>
</div>