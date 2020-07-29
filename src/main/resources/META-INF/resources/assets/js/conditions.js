function acceptConditions() {
    document.getElementById("choice").value = "accepted";
    document.getElementById("submit").click();
}

function rejectConditions() {
    document.getElementById("choice").value = "refused";
    document.getElementById("submit").click();
}
