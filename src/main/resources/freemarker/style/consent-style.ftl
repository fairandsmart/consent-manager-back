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

    .logo-container {
        padding: 0 24px;
    }


    /* Header */

    .info-header {
        margin: 24px;
    }

    .header-title {
        margin-top: 0;
        margin-bottom: 8px;
    }

    .header-body {
        color: rgba(0, 0, 0, 0.54);
        font-weight: bold;
    }

    .privacy-policy-container {
        margin: 8px 0;
        text-align: center;
    }

    .privacy-policy {
        text-decoration: none;
        color: #2196F3;
    }

    /* Content */

    .elements-list {
        overflow-y: auto;
        padding: 16px 24px 0 24px;
        height: 100%;
    }

    .element {
        padding: 6px 0 6px 12px;
    }

    .element.disabled {
        filter: grayscale(50%);
        opacity: .3;
        cursor: not-allowed;
    }

    .element-header {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;
    }

    .element-header .element-title {
        margin: 0;
    }

    .item-body, .block-body {
        color: rgba(0, 0, 0, 0.54);
    }

    .block-body li:first-of-type {
        margin-top: 8px;
    }

    .block-container {
        padding: 12px;
        margin: 12px 0;
        border-radius: 4px;
        background-color: #f5f5f5;
        text-align: left;
    }

    .block-container .block-title {
        margin: 0;
    }

    .block-container .block-body {
        margin: 0;
        padding-left: 16px;
        overflow: hidden;
        word-break: break-word;
    }

    .accordion-header {
        cursor: pointer;
        display: flex;
        justify-content: space-between;
    }

    .accordion-hidden {
        max-height: 0;
    }

    .accordion-visible {
        max-height: 500px;
        transition: .7s ease-in-out;
    }

    .accordion-symbol-open:before {
        content: "+";
    }

    .accordion-symbol-close:before {
        content: "-";
    }

    .item-container {
        margin-bottom: 8px;
    }

    .preference-error {
        color: red;
        font-style: italic;
    }

    .preference-error.hidden {
        display: none;
    }


    /* Footer */

    .info-footer {
        padding: 12px 24px;
    }

    .elements-list .info-footer {
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

    .submit-button {
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
