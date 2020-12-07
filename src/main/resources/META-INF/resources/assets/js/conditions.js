function acceptConditions() {
    $("#choice")[0].prop("value", "accepted");
    $("#submit").trigger("click");
}

function rejectConditions() {
    $("#choice")[0].prop("value", "refused");
    $("#submit").trigger("click");
}
