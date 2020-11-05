<div class="processing">
    <@fetchMultiLangContent element></@fetchMultiLangContent>

    <#if langContent?is_hash && langContent?has_content>
    <#-- Header -->
        <div class="processing-header">
            <h3><@valueOrError langContent.label "missingValue"></@valueOrError></h3>

            <#if langContent.valueType=="TOGGLE">
                <#assign isChecked=(!data.preview && data.previousValues[element.serial]?has_content
                    && data.previousValues[element.serial]==langContent.options[1])>
                <@toggleSwitch "${element.identifier}" isChecked></@toggleSwitch>
            </#if>
        </div>

    <#-- Data -->
        <#if langContent.description?has_content>
            <div class="item-wrapper">
                <p class="processing-body">${langContent.description}</p>
            </div>
        </#if>

        <#if langContent.valueType!="TOGGLE">
            <div class="item-wrapper">

                <#if langContent.valueType=="CHECKBOXES">
                    <#assign previousValues=(!data.preview && data.previousValues[element.serial]?has_content)
                    ?then(data.previousValues[element.serial]?split(","),[])>
                    <ul style="list-style-type: none;">
                        <#list langContent.options as option>
                            <li style="margin-top: 4px;">
                                <input type="checkbox" <#if previousValues?seq_contains(option)>checked</#if> id="${option}" value="${option}" name="${element.identifier}"><label for="${option}">${option}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if langContent.valueType=="RADIO_BUTTONS">
                    <#assign previousValue=(!data.preview && data.previousValues[element.serial]?has_content)
                    ?then(data.previousValues[element.serial], '')>
                    <ul style="list-style-type: none;">
                        <#list langContent.options as option>
                            <li style="margin-top: 4px;">
                                <input type="radio" <#if previousValue==option>checked</#if> id="${option}" value="${option}" name="${element.identifier}"><label for="${option}">${option}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if langContent.valueType=="LIST_SINGLE" || langContent.valueType=="LIST_MULTI">
                    <#assign previousValues=(!data.preview && data.previousValues[element.serial]?has_content)
                    ?then(data.previousValues[element.serial]?split(","),[])>
                    <select name="${element.identifier}" <#if langContent.valueType=="LIST_MULTI">multiple</#if>>
                        <#list langContent.options as option>
                            <option value="${option}" <#if previousValues?seq_contains(option)>selected</#if>>${option}</option>
                        </#list>
                    </select>
                </#if>

                <#if langContent.valueType=="FREE_TEXT">
                    <input type="text" id="${element.identifier}">
                </#if>

            </div>
        </#if>
    <#else>
        <p><@writeError "missingLanguage"></@writeError></p>
    </#if>
</div>
