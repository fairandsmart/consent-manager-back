<#--
 #%L
 Right Consents / A Consent Manager Platform
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
    .receipt {
        position: relative;
        display: flex;
        flex-direction: column;
        width: 100%;
        background-color: white;
    }

    @media screen and (max-width: 800px) {
        .receipt {
            height: 100vh;
        }
    }

    @media screen and (min-width: 800px) {
        .receipt {
            margin: auto;
            border: 1px solid #eeeeee;
            border-radius: 4px;
            box-shadow: 4px 4px 8px grey;
            max-height: 90vh;
            width: 800px;
        }
    }

    .receipt h4, .receipt p {
        margin: 4px 0;
    }

    .processing-response {
        display: block;
        position: relative;
        text-transform: uppercase;
        font-weight: 300;
    }

    .processing-response.accepted:before {
        position: absolute;
        left: -24px;
        height: 15px;
        width: 15px;
        border-radius: 50%;
        background: #1B870BDD;
        content: "";
    }

    .processing-response.refused:before {
        position: absolute;
        left: -24px;
        height: 15px;
        width: 15px;
        border-radius: 50%;
        background: #9C1A1ACC;
        content: "";
    }

    .element .item-body {
        color: rgba(0, 0, 0, 0.74);
    }

    .qr-code-container {
        text-align: center;
        border-bottom: 1px solid #eee;
    }

    .qr-code {
        width: 150px;
    }
</style>
