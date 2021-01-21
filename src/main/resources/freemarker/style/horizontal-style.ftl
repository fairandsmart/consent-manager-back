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

    .consent-form {
        display: flex;
        flex-direction: row;
        width: 100%;
    }

    .left {
        width: 40%;
        display: flex;
        flex-direction: column;
        overflow-y: auto;
    }

    .right {
        width: 60%;
        background-color: #eeeeee;
        display: flex;
        flex-direction: column;
    }

    @media screen and (max-width: 1000px) {
        .left, .right {
            height: 100vh;
        }
    }

    @media screen and (min-width: 1000px) {
        .consent-form {
            border: 1px solid #eeeeee;
            border-radius: 4px;
            box-shadow: 4px 4px 8px gray;
            width: 1000px;
        }

        .left, .right {
            max-height: 90vh;
        }
    }


    /* Header */

    .logo-container {
        margin: 24px 24px 0;
    }


    /* Content */

    .right .block-container {
        background-color: #e0e0e0;
    }


    /* Footer */

    .info-footer {
        border-top: 1px solid lightgray;
        margin: 0 36px;
    }
</style>
