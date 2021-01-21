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
    body {
        margin: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100vh;
    }

    .conditions-container {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 100%;
        background-color: white;
    }

    @media screen and (max-width: 800px) {
        .conditions-container {
            height: 100vh;
        }
    }

    @media screen and (min-width: 800px) {
        .conditions-container {
            margin: auto;
            border: 1px solid #eeeeee;
            border-radius: 4px;
            box-shadow: 4px 4px 8px gray;
            max-height: 90vh;
            width: 800px;
        }
    }

    h1 {
        margin-left: 24px;
    }

    .conditions {
        overflow-y: auto;
        padding: 0 24px 24px 24px;
        margin-top: 24px;
        height: 100%;
    }

    .buttons-container {
        text-align: center;
        margin: 24px 0;
    }

    .submit-button {
        padding: 4px 8px;
        min-width: 100px;
        height: 32px;
        font-size: large;
        cursor: pointer;
        border: none;
        border-radius: 20px;
        box-shadow: gray 2px 2px 6px;
    }

    .accept {
        background-color: #1B870B;
        color: white;
    }

    .reject {
        background-color: #9C1A1A;
        color: white;
    }
</style>
