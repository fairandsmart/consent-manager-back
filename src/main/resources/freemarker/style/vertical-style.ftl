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
