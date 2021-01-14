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

    .logo-container {
        text-align: center;
        margin-top: 24px;
    }

    .info-header {
        text-align: center;
        margin: 0 0 8px 12px;
        border-bottom: 1px solid #eee;
    }


    /* Footer */

    .info-footer {
        background-color: #eeeeee;
    }

    .accept-all-container {
        padding: 0 12px;
    }
</style>