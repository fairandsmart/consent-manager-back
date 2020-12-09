<style>
    /* Body */

    html {
        font-family: Arial, Helvetica, sans-serif;
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

    .slider {
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

    .slider:before {
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

    input:checked + .slider {
        background-color: #2196F3;
    }

    input:focus + .slider {
        box-shadow: 0 0 1px #2196F3;
    }

    input:checked + .slider:before {
        -webkit-transform: translateX(24px);
        -ms-transform: translateX(24px);
        transform: translateX(24px);
    }

    .switch .text {
        margin-top: 8px;
        text-align: center;
        font-size: small;
        width: 48px;
    }

    .switch .text.accept {
        display: none;
        color: #2196F3;
    }

    .switch .text.refuse {
        display: block;
        color: #888888;
    }

    .switch input:checked ~ .text.refuse {
        display: none;
    }

    .switch input:checked ~ .text.accept {
        display: block;
    }

    .switch-select {
        display: none;
    }
</style>
