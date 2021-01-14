<style>
    /* Body */

    html {
        font-family: Arial, Helvetica, sans-serif;
    }

    .logo {
        max-height: 100px;
    }

    .list-label {
        font-weight: bold;
    }


    /* Errors */

    .content-error {
        color: red;
        font-weight: bold;
    }


    /* Switches */

    .switch {
        position: relative;
        display: inline-block;
        width: 48px;
        height: 24px;
        margin: 0 0 20px 12px;
    }

    .switch input {
        opacity: 0;
        width: 0;
        height: 0;
    }

    .switch .switch-slider {
        position: absolute;
        cursor: pointer;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: #888888;
        -webkit-transition: .4s;
        transition: .4s;
        border-radius: 34px;
        box-shadow: gray 2px 2px 6px;
    }

    .switch .switch-slider:before {
        position: absolute;
        content: "";
        height: 20px;
        width: 20px;
        left: 2px;
        bottom: 2px;
        background-color: white;
        -webkit-transition: .4s;
        transition: .4s;
        border-radius: 50%;
    }

    .switch input:checked + .switch-slider {
        background-color: #2196F3;
    }

    .switch input:focus + .switch-slider {
        box-shadow: 0 0 1px #2196F3;
    }

    .switch input:checked + .switch-slider:before {
        -webkit-transform: translateX(24px);
        -ms-transform: translateX(24px);
        transform: translateX(24px);
    }

    .switch .switch-text {
        margin-top: 8px;
        text-align: center;
        font-size: small;
        width: 48px;
    }

    .switch .switch-text.accept {
        display: none;
        color: #2196F3;
    }

    .switch .switch-text.refuse {
        display: block;
        color: #888888;
    }

    .switch input:checked ~ .switch-text.refuse {
        display: none;
    }

    .switch input:checked ~ .switch-text.accept {
        display: block;
    }

    .switch .switch-select {
        display: none;
    }
</style>
