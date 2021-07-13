<#--
 #%L
 Right Consents, a Universal Consents & Preferences Management Platform.
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This file is part of Right Consents Community Edition.
 
 Right Consents Community Edition is published by FAIR AND SMART under the
 GNU GENERAL PUBLIC LICENCE Version 3 (GPLv3) and a set of additional terms.
 
 For more information, please see the “LICENSE” and “LICENSE.FAIRANDSMART”
 files, or see https://www.fairandsmart.com/opensource/.
 #L%
-->
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

    .switch-wrapper {
        display: flex;
        justify-content: center;
    }

    .switch {
        position: relative;
        display: inline-block;
        width: 48px;
        height: 24px;
        margin: 0 0 20px 12px;
    }

    .switch-wrapper input {
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

    .switch-text {
        text-align: center;
        width: 48px;
        color: black;
    }

    .switch .switch-text {
        margin-top: 8px;
        font-size: small;
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

    .switch-inline {
        text-align: center;
        max-width: 64px;
    }

    .switch-inline.accept {
        margin-left: 12px;
    }
</style>
