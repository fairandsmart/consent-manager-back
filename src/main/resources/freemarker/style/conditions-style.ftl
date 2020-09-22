<style>
    body {
        margin: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100vh;
    }

    .conditions-wrapper {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 100%;
    }

    @media screen and (max-width: 800px) {
        .conditions-wrapper {
            height: 100vh;
        }
    }

    @media screen and (min-width: 800px) {
        .conditions-wrapper {
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

    .buttons-wrapper {
        text-align: center;
        margin: 24px 0;
    }

    .submit {
        padding: 4px 8px;
        width: 200px;
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
