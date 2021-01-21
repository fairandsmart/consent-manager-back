/*-
 * #%L
 * Right Consents Community Edition
 * %%
 * Copyright (C) 2020 - 2021 Fair And Smart
 * %%
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * You accept that the Program was not created with a view to satisfy Your
 * individual requirements. Therefore, you must ensure that the Program
 * comply with Your needs, requirements and constraints. FAIR AND SMART
 * represents and warrants that it holds, without any restriction or
 * reservation, all the legal titles, authorizations and intellectual
 * property rights granted in the context of the GPLv3 License. See the
 * Additional Terms for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <https://www.gnu.org/licenses/>.
 * 
 * You should have received a copy of the Additional Terms along with this
 * program. If not, see <https://www.fairandsmart.com/opensource/>.
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
