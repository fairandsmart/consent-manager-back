<#assign receipt={
    "date": "2020-12-07T13:22:18.624151Z[UTC]",
    "expirationDate": "2021-06-06T13:22:18.624151Z[UTC]",
    "transaction": "A1b2c3",
    "collectionMethod": "Orci varius",
    "consents": [
        {
            "data": "Praesent purus tellus, auctor ultrices turpis vitae, cursus eleifend libero. In pulvinar tortor in diam scelerisque mollis.",
            "retentionLabel": "Nam ornare enim vel venenatis feugiat :",
            "retentionValue": 8,
            "retentionUnit": "WEEK",
            "usage": "Ut ultricies ultrices imperdiet. Pellentesque sed pharetra purus. Vestibulum sit amet ligula et lorem condimentum vestibulum ac ut turpis.",
            "value": "accepted"
        },
        {
            "data": "Phasellus molestie pulvinar risus vitae sollicitudin. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
            "retentionLabel": "Pellentesque elementum velit non luctus gravida :",
            "retentionValue": 3,
            "retentionUnit": "YEAR",
            "usage": "Mauris magna massa, maximus vel ante eget, tempor condimentum mi. Aenean sit amet volutpat arcu. Donec gravida lacinia ultrices.",
            "value": "refused"
        }
    ],
    "subject": "Lorem",
    "dataController": {
        "actingBehalfCompany": false,
        "company": "Efficitur duir",
        "name": "Dignissim vitae donec",
        "address": "Augue faucibus eu etiam",
        "email": "justo.non@sed.com",
        "phoneNumber": "02 46 89 75 31"
    },
    "privacyPolicyUrl": "lorem://ipsum.com"
}>

<#include "../components/receipt.ftl">
