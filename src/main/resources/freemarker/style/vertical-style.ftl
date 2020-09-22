<style>
    /* Body */

    .consent-form {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 100%;
    }

    @media screen and (max-width: 800px) {
        .consent-form {
            height: 100vh;
        }
    }

    @media screen and (min-width: 800px) {
        .consent-form {
            margin: auto;
            border: 1px solid #eeeeee;
            border-radius: 4px;
            box-shadow: 4px 4px 8px gray;
            max-height: 90vh;
            width: 800px;
        }
    }


    /* Header */

    .logo-wrapper {
        text-align: center;
        margin-top: 24px;
    }

    .header {
        text-align: center;
        margin: 0 0 18px 12px;
        border-bottom: 1px solid #eee;
    }


    /* Footer */

    .footer {
        padding: 12px 24px;
        background-color: #eeeeee;
    }

    .footer-body {
        padding: 0 12px;
    }

    .accept-all-container {
        padding: 0 12px;
    }

    .submit-container {
        text-align: center;
        margin: 8px 0 16px 0;
    }
</style>