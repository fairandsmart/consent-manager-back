function formatSelector(key) {
    return "#" + key.replace(/([:.\[\],=@\/])/g, "\\$1");
}

function toggleAccordion(id) {
    const selector = formatSelector(id);
    const wasHidden = $(selector).hasClass("controller-hidden");
    $(selector).toggleClass("controller-open", wasHidden);
    $(selector).toggleClass("controller-hidden", !wasHidden);
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
        let valid = true;
        const values = consentForm.serializeArray();
        const preferencesAnswers = values.filter(entry => entry.name.startsWith("element/preference/") && !entry.name.endsWith("-optional"));
        values.filter(entry => entry.name.endsWith("-optional") && entry.value === "mandatory")
            .forEach(entry => {
                const answered = preferencesAnswers.some(e => entry.name.includes(e.name));
                if (!answered) {
                    valid = false;
                }
                const missingSelector = formatSelector(entry.name.replace("-optional", "-missing"));
                $(missingSelector).toggleClass("hidden", answered);
            });

        if (!valid) {
            e.preventDefault();
        }
    });
}
