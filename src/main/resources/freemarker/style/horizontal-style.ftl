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
