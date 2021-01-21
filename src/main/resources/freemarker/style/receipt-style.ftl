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
    .receipt {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 100%;
        background-color: white;
    }

    @media screen and (max-width: 800px) {
        .receipt {
            height: 100vh;
        }
    }

    @media screen and (min-width: 800px) {
        .receipt {
            margin: auto;
            border: 1px solid #eeeeee;
            border-radius: 4px;
            box-shadow: 4px 4px 8px grey;
            max-height: 90vh;
            width: 800px;
        }
    }

    .receipt h4, .receipt p {
        margin: 4px 0;
    }

    .processing-response {
        display: block;
        position: relative;
        text-transform: uppercase;
        font-weight: 300;
    }

    .processing-response.accepted:before {
        position: absolute;
        left: -24px;
        height: 15px;
        width: 15px;
        border-radius: 50%;
        background: #1B870BDD;
        content: "";
    }

    .processing-response.refused:before {
        position: absolute;
        left: -24px;
        height: 15px;
        width: 15px;
        border-radius: 50%;
        background: #9C1A1ACC;
        content: "";
    }

    .element .item-body {
        color: rgba(0, 0, 0, 0.74);
    }

    .qr-code-container {
        text-align: center;
        border-bottom: 1px solid #eee;
    }

    .qr-code {
        width: 150px;
    }
</style>
