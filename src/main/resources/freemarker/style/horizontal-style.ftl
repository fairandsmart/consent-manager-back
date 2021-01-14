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