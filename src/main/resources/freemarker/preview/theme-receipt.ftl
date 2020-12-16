<#assign receipt={
    "transaction": "A1b2c3",
    "jurisdiction": "Sed sit",
    "language": "en",
    "logoPath": theme.logoPath,
    "logoAltText": theme.logoAltText,
    "logoPosition": theme.logoPosition,
    "date": "2020-12-07T13:22:18.624151Z[UTC]",
    "expirationDate": "2021-06-06T13:22:18.624151Z[UTC]",
    "processor": "Nam in mauris volutpat",
    "subject": "Lorem",
    "dataController": {
        "actingBehalfCompany": false,
        "company": "Efficitur duir",
        "name": "Dignissim vitae donec",
        "address": "Augue faucibus eu etiam",
        "email": "justo.non@sed.com",
        "phoneNumber": "02 46 89 75 31"
    },
    "consents": [
        {
            "data": "Praesent purus tellus, auctor ultrices turpis vitae, cursus eleifend libero. In pulvinar tortor in diam scelerisque mollis.",
            "retentionLabel": "Nam ornare enim vel venenatis feugiat :",
            "retentionValue": 8,
            "retentionUnit": "WEEK",
            "title": "Fusce ipsum nisl",
            "usage": "Ut ultricies ultrices imperdiet. Pellentesque sed pharetra purus. Vestibulum sit amet ligula et lorem condimentum vestibulum ac ut turpis.",
            "controller": {
                "actingBehalfCompany": false,
                "company": "Duis efficitur",
                "name": "Vitae donec dignissim",
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
            "retentionLabel": "Pellentesque elementum velit non luctus gravida :",
            "retentionValue": 3,
            "retentionUnit": "YEAR",
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