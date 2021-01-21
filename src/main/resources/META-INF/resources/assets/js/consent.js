/*-
 * #%L
 * Right Consents, a Universal Consents & Preferences Management Platform.
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This file is part of Right Consents Community Edition.
 * 
 * Right Consents Community Edition is published by FAIR AND SMART under the
 * GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 * 
 * For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 * files, or see https://www.fairandsmart.com/opensource/.
 * #L%
 */
function formatSelector(key) {
    return "#" + key.replace(/([:.\[\],=@\/])/g, "\\$1");
}

function toggleAccordion(id) {
    let selector = formatSelector(id);
    const wasHidden = $(selector).hasClass("accordion-hidden");
    $(selector).toggleClass("accordion-visible", wasHidden);
    $(selector).toggleClass("accordion-hidden", !wasHidden);
    selector = formatSelector(id + "-symbol");
    $(selector).toggleClass("accordion-symbol-close", wasHidden);
    $(selector).toggleClass("accordion-symbol-open", !wasHidden);
}

function isPreference(selector) {
    return selector.includes("\\/preference\\/");
}

function getSwitchesSelectors() {
    return $(".switch").toArray().map(element => formatSelector(element.children[0].id));
}

function toggleSwitch(selector, isOn, updateCheckbox) {
    if (updateCheckbox) {
        $(selector).prop("checked", isOn ? "checked" : "");
    }
    $(selector + "-accepted").prop("selected", isOn);
    $(selector + "-refused").prop("selected", !isOn);
}

/* Checking "Accept all" if all the toggle switches are checked */
const acceptAllSelector = "#accept-all";

function checkAcceptAll() {
    const allChecked = getSwitchesSelectors()
        .filter(selector => selector !== acceptAllSelector && !isPreference(selector))
        .every(selector => $(selector + "-accepted").prop("selected"));
    if (allChecked) {
        toggleSwitch(acceptAllSelector, true, true);
    }
}

/* Handling button "Accept all" */
const acceptAll = $(acceptAllSelector + "-switch");
const hasAcceptAll = acceptAll.length > 0;
if (hasAcceptAll) {
    acceptAll.on("change", (e) => {
        toggleSwitch(acceptAllSelector, e.target.checked, false);

        getSwitchesSelectors().filter(selector => selector !== acceptAllSelector && !isPreference(selector))
            .forEach(selector => {
                toggleSwitch(selector, e.target.checked, true);
            });
    });
}

/* Handling preferences switches */
getSwitchesSelectors().filter(selector => isPreference(selector))
    .forEach(selector => $(selector).on("change", (e) => toggleSwitch(selector, e.target.checked, false)));

/* Handling processing switches */
getSwitchesSelectors().filter(selector => selector !== acceptAllSelector && !isPreference(selector))
    .forEach(selector => {
        $(selector).on("change", (e) => {
            toggleSwitch(selector, e.target.checked, false);

            if (hasAcceptAll) {
                const isAcceptAllChecked = $(acceptAllSelector + "-accepted").prop("selected");
                /* Uncheck "Accept all" if current toggle switch is unchecked */
                if (isAcceptAllChecked && !e.target.checked) {
                    toggleSwitch(acceptAllSelector, false, true);
                } /* Check "Accept all" if current toggle switch is checked and all the other switches are checked */
                else if (!isAcceptAllChecked && e.target.checked) {
                    checkAcceptAll();
                }
            }
        });
    });

/* If all the previous values are "accepted", "Accept all" must be already checked */
if (hasAcceptAll) {
    checkAcceptAll();
}

const consentForm = $("#consent");
if (consentForm.length > 0) {
    consentForm.submit(function (e) {
        let formValid = true;
        const values = consentForm.serializeArray();
        const preferencesAnswers = values.filter(entry => entry.name.startsWith("element/preference/") && !entry.name.endsWith("-optional"));
        values.filter(entry => entry.name.endsWith("-optional") && entry.value === "mandatory")
            .forEach(entry => {
                let fieldValid = true;
                const answerIndex = preferencesAnswers.findIndex(e => entry.name.includes(e.name));
                const answer = preferencesAnswers[answerIndex];
                const answered = answerIndex > -1;
                if (!answered || (typeof answer.value === 'string' && answer.value.length === 0)) {
                    formValid = false;
                    fieldValid = false;
                }
                const missingSelector = formatSelector(entry.name.replace("-optional", "-missing"));
                $(missingSelector).toggleClass("hidden", fieldValid);
            });

        if (!formValid) {
            e.preventDefault();
        }
    });
}
