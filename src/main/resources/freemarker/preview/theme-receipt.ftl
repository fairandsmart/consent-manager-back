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
<#assign receipt={
    "transaction": "A1b2c3",
    "jurisdiction": "Sed sit",
    "language": "en",
    "themeInfo": {
        "logoPath": theme.logoPath,
        "logoAltText": theme.logoAltText,
        "logoPosition": theme.logoPosition
    },
    "date": "2020-12-07T13:22:18.624151Z[UTC]",
    "expirationDate": "2021-06-06T13:22:18.624151Z[UTC]",
    "processor": "Nam in mauris volutpat",
    "subject": "Lorem",
    "dataController": {
        "company": "Efficitur duir",
        "info": "Dignissim vitae donec",
        "address": "Augue faucibus eu etiam",
        "email": "justo.non@sed.com",
        "phoneNumber": "02 46 89 75 31"
    },
    "consents": [
        {
            "data": "Praesent purus tellus, auctor ultrices turpis vitae, cursus eleifend libero. In pulvinar tortor in diam scelerisque mollis.",
            "retention": {
                "label": "Nam ornare enim vel venenatis feugiat :",
                "value": 1,
                "unit": "YEAR",
                "fullText": "Nam ornare enim vel venenatis feugiat : 1 anno"
            },
            "title": "Fusce ipsum nisl",
            "usage": "Ut ultricies ultrices imperdiet. Pellentesque sed pharetra purus. Vestibulum sit amet ligula et lorem condimentum vestibulum ac ut turpis.",
            "controller": {
                "company": "Duis efficitur",
                "info": "Vitae donec dignissim",
                "address": "Faucibus eu etiam augue",
                "email": "non.sed@justo.com",
                "phoneNumber": "98 76 54 32 10"
            },
            "purposes": [ "CONSENT_CORE_SERVICE" ],
            "thirdParties": [],
            "value": "accepted",
            "containsSensitiveData": true,
            "containsMedicalData": true
        },
        {
            "data": "Phasellus molestie pulvinar risus vitae sollicitudin. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
            "retention": {
                "label": "Pellentesque elementum velit non luctus gravida :",
                "value": 2,
                "unit": "YEAR",
                "fullText": "Pellentesque elementum velit non luctus gravida : 2 anno"
            },
            "title": "Quisque sit amet",
            "usage": "Mauris magna massa, maximus vel ante eget, tempor condimentum mi. Aenean sit amet volutpat arcu. Donec gravida lacinia ultrices.",
            "controller": {},
            "purposes": [ "CONSENT_RESEARCH", "CONSENT_THIRD_PART_SHARING" ],
            "thirdParties": [
                {
                    "name": "Aliquam metus",
                    "value": "lacinia convallis"
                }
            ],
            "value": "refused",
            "containsSensitiveData": false,
            "containsMedicalData": false
        }
    ],
    "privacyPolicyUrl": "#",
    "collectionMethod": "Orci varius",
    "updateUrl": "#",
    "updateUrlQrCode": "/assets/img/themes/preview-qr-code.png"
}>

<#include "../components/receipt.ftl">
