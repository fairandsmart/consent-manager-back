<#--
 #%L
 Right Consents Community Edition
 %%
 Copyright (C) 2020 - 2021 Fair And Smart
 %%
 This program is free software: you can redistribute it and/or modify it
 under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or (at your
 option) any later version.
 
 You accept that the Program was not created with a view to satisfy Your
 individual requirements. Therefore, you must ensure that the Program
 comply with Your needs, requirements and constraints. FAIR AND SMART
 represents and warrants that it holds, without any restriction or
 reservation, all the legal titles, authorizations and intellectual
 property rights granted in the context of the GPLv3 License. See the
 Additional Terms for more details.
 
 You should have received a copy of the GNU General Public License along
 with this program. If not, see <https://www.gnu.org/licenses/>.
 
 You should have received a copy of the Additional Terms along with this
 program. If not, see <https://www.fairandsmart.com/opensource/>.
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
