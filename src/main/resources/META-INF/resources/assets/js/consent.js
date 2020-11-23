/* Handling button "Accept all" */
const acceptAll = document.getElementById("accept-all-switch");
const hasAcceptAll = acceptAll != null;
if (hasAcceptAll) {
    acceptAll.addEventListener("change", (e) => {
        const switches = document.getElementsByClassName("switch");
        if (e.target.checked) { /* Check all */
            for (let i = 0; i < switches.length - 1; i++) { // Last element is button "Accept all"
                const key = switches[i].children[0].id; // Checkbox input id
                document.getElementById(key).checked = true; // Check input
                document.getElementById(key + "-accepted").selected = true; // Select option "accepted"
                document.getElementById(key + "-refused").selected = false; // Deselect option "refused"
            }
        } else { /* Uncheck all */
            for (let j = 0; j < switches.length - 1; j++) { // Last element is button "Accept all"
                const key = switches[j].children[0].id; // Checkbox input id
                document.getElementById(key).checked = false; // Uncheck input
                document.getElementById(key + "-accepted").selected = false; // Deselect option "accepted"
                document.getElementById(key + "-refused").selected = true; // Select option "refused"
            }
        }
    });
}

/* Checking "Accept all" if all the toggle switches are checked */
function checkAcceptAll() {
    const switches = document.getElementsByClassName("switch");
    let allChecked = true;
    let i = 0;
    let key;
    while (allChecked && i < switches.length) {
        key = switches[i].children[0].id; // Checkbox input id
        if (key !== "accept-all") {
            allChecked = document.getElementById(key + "-accepted").selected;
        }
        i++;
    }
    if (allChecked) {
        document.getElementById("accept-all").checked = true; // Check input
        document.getElementById("accept-all-accepted").selected = true; // Select option "accepted"
        document.getElementById("accept-all-refused").selected = false; // Deselect option "refused"
    }
}

/* Handling hidden values when a user clicks on a toggle switch */
const switches = document.getElementsByClassName("switch");
for (let i = 0; i < switches.length; i++) {
    const key = switches[i].children[0].id; // Checkbox input id

    if (key !== "accept-all") { // Regular toggle switch
        document.getElementById(key).addEventListener("change", (e) => {
            const accepted = document.getElementById(key + "-accepted");
            const refused = document.getElementById(key + "-refused");
            if (e.target.checked) {
                accepted.selected = true;
                refused.selected = false;
            } else {
                accepted.selected = false;
                refused.selected = true;
            }

            if (hasAcceptAll) {
                const acceptAllButton = document.getElementById("accept-all");
                /* Uncheck "Accept all" if current toggle switch is unchecked */
                if (acceptAllButton.checked && !e.target.checked) {
                    acceptAllButton.checked = false; // Uncheck input
                    document.getElementById("accept-all-accepted").selected = false; // Deselect option "accepted"
                    document.getElementById("accept-all-refused").selected = true; // Select option "refused"
                } /* Check "Accept all" if current toggle switch is checked and all the other switches are checked */
                else if (!acceptAllButton.checked && e.target.checked) {
                    checkAcceptAll();
                }
            }
        });
    }
}

/* If all the previous values are "accepted", "Accept all" must be already checked */
if (hasAcceptAll) {
    checkAcceptAll();
}

function toggleAccordion(id) {
    if (document.getElementById(id).classList.contains("controller-hidden")) {
        document.getElementById(id).classList.replace("controller-hidden", "controller-open");
    } else {
        document.getElementById(id).classList.replace("controller-open", "controller-hidden");
    }
}
