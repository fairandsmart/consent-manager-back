console.log("todo");

/* Gestion du bouton "Tout accepter" */
var acceptAll = document.getElementById("accept-all-switch");
const hasAcceptAll = acceptAll != null && acceptAll != undefined;
if (hasAcceptAll) {
    acceptAll.addEventListener("change", (e) => {
        var switches = document.getElementsByClassName("switch");
        if (e.target.checked) { /* Tout sélectionner */
            for (var i = 0; i < switches.length - 1; i++) { // Le dernier élément est forcément le bouton "Tout accepter"
                const key = switches[i].children[0].id; // Checkbox input id
                document.getElementById(key).checked = true; // Check input
                document.getElementById(key + "-accepted").selected = true; // Select option "accepted"
                document.getElementById(key + "-refused").selected = false; // Deselect option "refused"
            }
        } else { /* Tout désélectionner */
            for (var i = 0; i < switches.length - 1; i++) { // Le dernier élément est forcément le bouton "Tout accepter"
                const key = switches[i].children[0].id; // Checkbox input id
                document.getElementById(key).checked = false; // Uncheck input
                document.getElementById(key + "-accepted").selected = false; // Deselect option "accepted"
                document.getElementById(key + "-refused").selected = true; // Select option "refused"
            }
        }
    });
}

/* Gestion des select cachés au niveau des toggle switches
/!\ Ne fonctionne que pour les clics de la souris, PAS pour les changements d'état en pur JS /!\ */
var switches = document.getElementsByClassName("switch");
for (var i = 0; i < switches.length; i++) {
    const key = switches[i].children[0].id; // Checkbox input id

    document.getElementById(key).addEventListener("change", (e) => {
        var accepted = document.getElementById(key + "-accepted");
        var refused = document.getElementById(key + "-refused");
        if (e.target.checked) {
            accepted.selected = true;
            refused.selected = false;
        } else {
            accepted.selected = false;
            refused.selected = true;
        }

        /* Désélection du bouton "Tout accepter" si la checkbox courante est décochée alors "Tout accepter" était coché */
        if (hasAcceptAll) {
            var acceptAllButton = document.getElementById("accept-all");
            if (acceptAllButton.checked && !e.target.checked) {
                acceptAllButton.checked = false; // Uncheck input
                document.getElementById("accept-all-accepted").selected = false; // Deselect option "accepted"
                document.getElementById("accept-all-refused").selected = true; // Select option "refused"
            }
        }
    });
};
