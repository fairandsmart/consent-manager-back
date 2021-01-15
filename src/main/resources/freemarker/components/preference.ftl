<div class="element">
    <#assign hasPreviousValues=!data.preview && data.previousValues[element.serial]?has_content>
    <#assign hasDefaultValues=elementContent.includeDefault && elementContent.defaultValues?has_content>
    <#assign previousValues=hasPreviousValues?then(data.previousValues[element.serial]?split(","),[])>

    <#if elementContent?is_hash>
        <#-- Header -->
        <#if !data.preview>
            <input type="hidden" name="${identifier}-optional" id="${identifier}-optional"
                   value="${elementContent.optional?then('optional','mandatory')}">
        </#if>

        <#-- Header -->
        <div class="element-header">
            <h3 class="element-title"><@valueOrError elementContent.label "missingValue"></@valueOrError></h3>

            <#if elementContent.valueType=="TOGGLE">
                <#if hasPreviousValues>
                    <#assign isChecked=previousValues?seq_contains(elementContent.options[1])>
                <#elseif hasDefaultValues>
                    <#assign isChecked=elementContent.defaultValues?seq_contains(elementContent.options[1])>
                <#else>
                    <#assign isChecked=false>
                </#if>
                <@toggleSwitch key="${identifier}" isChecked=isChecked acceptText=elementContent.options[1]
                refuseText=elementContent.options[0]></@toggleSwitch>
            </#if>
        </div>

        <#-- Data -->
        <#if elementContent.description?has_content>
            <div class="item-container">
                <p class="item-body">${elementContent.description?html}</p>
            </div>
        </#if>

        <#if elementContent.valueType!="TOGGLE">
            <div class="item-container">

                <#if elementContent.valueType=="CHECKBOXES">
                    <ul style="list-style-type: none;">
                        <#list elementContent.options as option>
                            <li style="margin-top: 4px;">
                                <#if hasPreviousValues>
                                    <#assign isChecked=previousValues?seq_contains(option)>
                                <#elseif hasDefaultValues>
                                    <#assign isChecked=elementContent.includeDefault &&
                                    elementContent.defaultValues?has_content && elementContent.defaultValues?seq_contains(option)>
                                <#else>
                                    <#assign isChecked=false>
                                </#if>
                                <input type="checkbox" <#if isChecked>checked</#if>
                                       id="${option?html}" value="${option?html}" name="${identifier?html}">
                                <label for="${option?html}">${option?html}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if elementContent.valueType=="RADIO_BUTTONS">
                    <ul style="list-style-type: none;">
                        <#list elementContent.options as option>
                            <li style="margin-top: 4px;">
                                <#if hasPreviousValues>
                                    <#assign isChecked=previousValues?seq_contains(option)>
                                <#elseif hasDefaultValues>
                                    <#assign isChecked=elementContent.includeDefault &&
                                    elementContent.defaultValues?has_content && elementContent.defaultValues?seq_contains(option)>
                                <#else>
                                    <#assign isChecked=false>
                                </#if>
                                <input type="radio" <#if isChecked>checked</#if> id="${option?html}" value="${option?html}" name="${identifier?html}">
                                <label for="${option?html}">${option?html}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if elementContent.valueType=="LIST_SINGLE" || elementContent.valueType=="LIST_MULTI">
                    <select name="${identifier}" <#if elementContent.valueType=="LIST_MULTI">multiple</#if>>
                        <#list elementContent.options as option>
                            <#if hasPreviousValues>
                                <#assign isChecked=previousValues?seq_contains(option)>
                            <#elseif hasDefaultValues>
                                <#assign isChecked=elementContent.includeDefault &&
                                elementContent.defaultValues?has_content && elementContent.defaultValues?seq_contains(option)>
                            <#else>
                                <#assign isChecked=false>
                            </#if>
                            <option value="${option?html}" <#if isChecked>selected</#if>>${option?html}</option>
                        </#list>
                    </select>
                </#if>

                <#if elementContent.valueType=="FREE_TEXT">
                    <#if hasPreviousValues>
                        <#assign previousInputValue=data.previousValues[element.serial]>
                    <#else>
                        <#assign previousInputValue="">
                    </#if>
                    <input type="text" id="${identifier?html}" name="${identifier?html}" value="${previousInputValue?html}">
                </#if>

            </div>
        </#if>

        <#if !elementContent.optional>
            <div class="item-container preference-error hidden" id="${identifier}-missing">
                <@readBundle "missingPreference" "missingValue"></@readBundle>
            </div>
        </#if>
    <#else>
        <p><@writeError "missingLanguage"></@writeError></p>
    </#if>
</div>
