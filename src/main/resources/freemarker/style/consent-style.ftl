<style>
    /* Body */

    body {
        margin: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100vh;
    }

    .consent-form {
        background-color: white;
    }

    .consent-form h4, .consent-form p {
        margin: 4px 0;
    }


    /* Logo */

    .logo-wrapper {
        padding: 0 24px;
    }

    .logo {
        max-height: 100px;
    }


    /* Header */

    .header {
        margin: 24px;
    }

    .header .header-title {
        margin-top: 0;
        margin-bottom: 8px;
    }

    .header .header-body {
        color: rgba(0, 0, 0, 0.54);
        font-weight: bold;
    }

    .header .privacy-policy-link-wrapper {
        margin: 8px 0;
    }

    .header .privacy-policy-link-wrapper a {
        text-decoration: none;
        color: #2196F3;
    }

    .content-fade {
        background: linear-gradient(white, rgba(255, 255, 255, 0));
        height: 32px;
        margin: 0 16px -32px 16px;
        z-index: 10;
        overflow-y: hidden;
        pointer-events: none;
        flex-shrink: 0;
    }

    /* Content */

    .processing-list {
        overflow-y: auto;
        padding: 16px 24px;
        height: 100%;
    }

    .processing {
        padding: 6px 0 6px 12px;
    }

    .processing-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
    }

    .processing-header h3 {
        margin: 0;
    }

    .processing-body {
        color: rgba(0, 0, 0, 0.54);
    }

    .processing-body li:first-of-type {
        margin-top: 8px;
    }

    .block-wrapper {
        padding: 12px;
        margin: 12px 0;
        border-radius: 4px;
        background-color: #f5f5f5;
        text-align: left;
    }

    .block-wrapper h4 {
        margin: 0;
    }

    .block-wrapper ul {
        margin: 0;
        padding-left: 16px;
        overflow: hidden;
    }

    .controller-header {
        cursor: pointer;
        display: flex;
        justify-content: space-between;
    }

    .controller-hidden {
        max-height: 0;
        transition: 1s linear;
    }

    .controller-open {
        max-height: 200px;
        transition: 1s linear;
    }

    .item-wrapper {
        margin-bottom: 8px;
    }

    .purpose-container {
        text-align: center;
        margin-top: 8px !important;
    }

    .purpose {
        width: 32px;
        height: 32px;
    }


    /* Footer */

    .footer {
        padding: 12px 24px;
    }

    .processing-list .footer {
        margin-left: -24px;
        margin-right: -24px;
    }

    .footer-body {
        padding: 0 12px;
        margin-bottom: 8px;
        max-height: 100px;
        overflow-y: auto;
    }

    .accept-all-container {
        display: flex;
        justify-content: space-between;
    }

    .accept-all-text {
        font-weight: bold;
    }

    .submit-container {
        text-align: center;
        margin: 8px 0 4px 0;
    }

    .submit {
        padding: 4px 8px;
        width: 200px;
        height: 32px;
        font-size: large;
        cursor: pointer;
        border: none;
        border-radius: 20px;
        background-color: #2196F3;
        color: white;
        box-shadow: gray 2px 2px 6px;
    }
</style>
