<#assign info={
    "type": "basicinfo",
    "title": "Etiam semper, risus consectetur",
    "header": "In tempus ligula non velit lobortis dignissim. Integer ultrices lectus dui, nec bibendum lectus egestas sed. Proin congue quam dapibus, fermentum nulla nec, hendrerit ligula.",
    "footer": "Etiam auctor cursus sem ut gravida. Mauris quam metus, dictum a ante quis, iaculis tempor dolor. Sed interdum vitae urna id ullamcorper.",
    "jurisdiction": "Aliquam",
    "showJurisdiction": true,
    "collectionMethod": "",
    "showCollectionMethod": false,
    "dataController": {
        "company": "Duis efficitur",
        "info": "Donec vitae dignissim",
        "address": "Etiam eu faucibus augue",
        "email": "sed.non@justo.com",
        "phoneNumber": "01 23 45 67 89"
    },
    "showDataController": true,
    "scope": "",
    "showScope": false,
    "shortNoticeLink": "previewUrlShort",
    "showShortNoticeLink": true,
    "privacyPolicyUrl": "previewUrl",
    "customPrivacyPolicyText": ""
}>
<#assign infoIdentifier="infos">
<#assign displayAcceptAll=true>
<#assign elements=[
    {
        "type": "processing",
        "title": "Proin hendrerit egestas",
        "data": "Pellentesque sagittis hendrerit leo, quis lacinia ante tincidunt ut. Mauris non bibendum leo. Praesent vel sapien imperdiet, rutrum urna quis, blandit urna.",
        "retention": {
            "label": "Maecenas eros tellus, fringilla vitae fermentum a, eleifend eu elit :",
            "value": 3,
            "unit": "YEAR",
            "fullText": "Maecenas eros tellus, fringilla vitae fermentum a, eleifend eu elit : 3 anno"
        },
        "usage": "Ut semper, erat non feugiat aliquam, augue arcu malesuada arcu, vitae vulputate sem velit vel eros. Fusce et ultrices justo, a varius felis. Integer id finibus turpis. Maecenas maximus at est et congue.",
        "purposes": [ "CONSENT_CORE_SERVICE" ],
        "containsSensitiveData": true,
        "containsMedicalData": false,
        "dataController": {},
        "showDataController": false,
        "thirdParties": []
    },
    {
        "type": "processing",
        "title": "Nam commodo turpis ut odio",
        "data": "Ut ultrices urna ut enim placerat porttitor. Pellentesque cursus lorem nec vulputate scelerisque. Aliquam erat volutpat.",
        "retention": {
            "label": "Pellentesque et pretium ligula :",
            "value": 2,
            "unit": "YEAR",
            "fullText": "Pellentesque et pretium ligula : 2 anno"
        },
        "usage": "Quisque consectetur metus lorem. In lobortis pharetra nibh. Aenean dui dui, vulputate non arcu ut, dignissim aliquet metus. Ut luctus lacus eu eros consequat, vel efficitur odio gravida.",
        "purposes": [ "CONSENT_THIRD_PART_SHARING" ],
        "containsSensitiveData": true,
        "containsMedicalData": false,
        "dataController": {
            "company": "Duis efficitur",
            "info": "Vitae donec dignissim",
            "address": "Faucibus eu etiam augue",
            "email": "non.sed@justo.com",
            "phoneNumber": "98 76 54 32 10"
        },
        "showDataController": false,
        "thirdParties": [
            {
                "name": "Nibh nec metus",
                "value": "vitae in lectus"
            }
        ]
    },
    {
        "type": "preference",
        "label": "Aenean aliquam faucibus sodales",
        "description": "Nunc commodo, mauris tempus blandit ultrices, velit tellus dignissim dolor, ac viverra ante tellus vel justo. Vestibulum id tempor magna.",
        "options": [ "Morbi in lacus", "Finibus ligula et", "Duis at semper libero" ],
        "valueType": "CHECKBOXES",
        "includeDefault": true,
        "defaultValues": [ "Duis at semper libero" ],
        "optional": false
    }
]>
