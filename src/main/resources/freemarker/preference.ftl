<div class="processing <#if data.elementsDependencies[element.serial]?has_content>dependent</#if>" <#if data.elementsDependencies[element.serial]?has_content>data-dependent-to="${data.elementsDependencies[element.serial]}"</#if>>
    <@fetchMultiLangContent element></@fetchMultiLangContent>
    <#assign hasPreviousValues=!data.preview && data.previousValues[element.serial]?has_content>
    <#assign hasDefaultValues=langContent.includeDefault && langContent.defaultValues?has_content>
    <#assign previousValues=hasPreviousValues?then(data.previousValues[element.serial]?split(","),[])>

    <#if langContent?is_hash && langContent?has_content>
        <input type="hidden" name="${element.identifier}-optional" id="${element.identifier}-optional"
               value="${langContent.optional?then('optional','mandatory')}">

    <#-- Header -->
        <div class="processing-header">
            <h3><@valueOrError langContent.label "missingValue"></@valueOrError></h3>

            <#if langContent.valueType=="TOGGLE">
                <#if hasPreviousValues>
                    <#assign isChecked=previousValues?seq_contains(langContent.options[1])>
                <#elseif hasDefaultValues>
                    <#assign isChecked=langContent.defaultValues?seq_contains(langContent.options[1])>
                <#else>
                    <#assign isChecked=false>
                </#if>
                <@toggleSwitch key="${element.identifier}" isChecked=isChecked acceptText=langContent.options[1]
                refuseText=langContent.options[0]></@toggleSwitch>
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
                    <ul style="list-style-type: none;">
                        <#list langContent.options as option>
                            <li style="margin-top: 4px;">
                                <#if hasPreviousValues>
                                    <#assign isChecked=previousValues?seq_contains(option)>
                                <#elseif hasDefaultValues>
                                    <#assign isChecked=langContent.includeDefault &&
                                        langContent.defaultValues?has_content && langContent.defaultValues?seq_contains(option)>
                                <#else>
                                    <#assign isChecked=false>
                                </#if>
                                <input type="checkbox" <#if isChecked>checked</#if>
                                       id="${option}" value="${option}" name="${element.identifier}">
                                <label for="${option}">${option}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if langContent.valueType=="RADIO_BUTTONS">
                    <ul style="list-style-type: none;">
                        <#list langContent.options as option>
                            <li style="margin-top: 4px;">
                                <#if hasPreviousValues>
                                    <#assign isChecked=previousValues?seq_contains(option)>
                                <#elseif hasDefaultValues>
                                    <#assign isChecked=langContent.includeDefault &&
                                    langContent.defaultValues?has_content && langContent.defaultValues?seq_contains(option)>
                                <#else>
                                    <#assign isChecked=false>
                                </#if>
                                <input type="radio" <#if isChecked>checked</#if> id="${option}" value="${option}" name="${element.identifier}">
                                <label for="${option}">${option}</label>
                            </li>
                        </#list>
                    </ul>
                </#if>

                <#if langContent.valueType=="LIST_SINGLE" || langContent.valueType=="LIST_MULTI">
                    <select name="${element.identifier}" <#if langContent.valueType=="LIST_MULTI">multiple</#if>>
                        <#list langContent.options as option>
                            <#if hasPreviousValues>
                                <#assign isChecked=previousValues?seq_contains(option)>
                            <#elseif hasDefaultValues>
                                <#assign isChecked=langContent.includeDefault &&
                                langContent.defaultValues?has_content && langContent.defaultValues?seq_contains(option)>
                            <#else>
                                <#assign isChecked=false>
                            </#if>
                            <option value="${option}" <#if isChecked>selected</#if>>${option}</option>
                        </#list>
                    </select>
                </#if>

                <#if langContent.valueType=="FREE_TEXT">
                    <#if hasPreviousValues>
                        <#assign previousInputValue=data.previousValues[element.serial]>
                    <#else>
                        <#assign previousInputValue="">
                    </#if>
                    <input type="text" id="${element.identifier}" name="${element.identifier}" value="${previousInputValue}">
                </#if>

                <#if langContent.valueType=="NONE">
                    <input type="hidden" id="${element.identifier}" name="${element.identifier}" value="">
                </#if>

            </div>
        </#if>

        <#if !langContent.optional>
            <div class="item-wrapper preference-error hidden" id="${element.identifier}-missing">
                <@readBundle "missingPreference" "missingValue"></@readBundle>
            </div>
        </#if>
    <#else>
        <p><@writeError "missingLanguage"></@writeError></p>
    </#if>
</div>
