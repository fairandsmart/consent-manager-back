/* Gestion du bouton "Tout accepter" */
const acceptAll = document.getElementById("accept-all-switch");
const hasAcceptAll = acceptAll != null;
if (hasAcceptAll) {
    acceptAll.addEventListener("change", (e) => {
        const switches = document.getElementsByClassName("switch");
        if (e.target.checked) { /* Tout sélectionner */
            for (let i = 0; i < switches.length - 1; i++) { // Le dernier élément est forcément le bouton "Tout accepter"
                const key = switches[i].children[0].id; // Checkbox input id
                document.getElementById(key).checked = true; // Check input
                document.getElementById(key + "-accepted").selected = true; // Select option "accepted"
                document.getElementById(key + "-refused").selected = false; // Deselect option "refused"
            }
        } else { /* Tout désélectionner */
            for (let j = 0; j < switches.length - 1; j++) { // Le dernier élément est forcément le bouton "Tout accepter"
                const key = switches[j].children[0].id; // Checkbox input id
                document.getElementById(key).checked = false; // Uncheck input
                document.getElementById(key + "-accepted").selected = false; // Deselect option "accepted"
                document.getElementById(key + "-refused").selected = true; // Select option "refused"
            }
        }
    });
}

/* Gestion des select cachés au niveau des toggle switches
/!\ Ne fonctionne que pour les clics de la souris, PAS pour les changements d'état en pur JS /!\ */
const switches = document.getElementsByClassName("switch");
for (let i = 0; i < switches.length; i++) {
    const key = switches[i].children[0].id; // Checkbox input id

    if (key !== "accept-all") {
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
                /* Désélection du bouton "Tout accepter" si la checkbox courante est décochée alors "Tout accepter" était coché */
                if (acceptAllButton.checked && !e.target.checked) {
                    acceptAllButton.checked = false; // Uncheck input
                    document.getElementById("accept-all-accepted").selected = false; // Deselect option "accepted"
                    document.getElementById("accept-all-refused").selected = true; // Select option "refused"
                } /* Sélection du bouton "Tout accepter" si la checkbox courante est cochée alors que toutes les autres le sont aussi et que "Tout accepter" était décoché */
                else if (!acceptAllButton.checked && e.target.checked) {
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
                        acceptAllButton.checked = true; // Check input
                        document.getElementById("accept-all-accepted").selected = true; // Select option "accepted"
                        document.getElementById("accept-all-refused").selected = false; // Deselect option "refused"
                    }
                }
            }
        });
    }
}
