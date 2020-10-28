<style>
    /* Body */

    body {
        margin: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100vh;
    }

    /* Logo */

    .logo-wrapper {
        padding-bottom: 8px;
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
    }

    .header .header-body {
        color: rgba(0, 0, 0, 0.54);
        font-weight: bold;
    }

    .header .privacy-policy-link-wrapper {
        margin: 24px 0;
    }

    .header .privacy-policy-link-wrapper a {
        text-decoration: none;
        color: #2196F3;
    }

    .close-wrapper {
        position: absolute;
        top: 20px;
        right: 20px;
        z-index: 20;
    }

    .close-btn {
        cursor: pointer;
        border: none;
        background: none;
        color: black;
        font-size: larger;
        font-weight: bold;
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
        padding: 24px;
        height: 100%;
    }

    .processing {
        padding: 6px 0 6px 12px;
    }

    .processing-header {
        display: flex;
        justify-content: space-between;
    }

    .processing-body {
        color: rgba(0, 0, 0, 0.54);
    }

    .block-wrapper {
        padding: 12px;
        margin: 24px 0 16px 0;
        border-radius: 4px;
        background-color: #f5f5f5;
        text-align: left;
    }

    .block-wrapper h4 {
        margin-top: 0;
    }

    .block-wrapper ul {
        margin: 0;
    }

    .purpose-container {
        text-align: center;
    }

    .purpose {
        width: 32px;
        height: 32px;
    }


    /* Footer */

    .footer-body {
        margin: 12px 0;
        max-height: 100px;
        overflow-y: auto;
    }

    .accept-all-container {
        display: flex;
        justify-content: space-between;
    }

    .accept-all-text {
        font-weight: bold;
        margin: 12px 20px 12px 0;
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
