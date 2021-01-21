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
<#assign info={
    "type": "basicinfo",
    "title": "Etiam semper, risus consectetur",
    "header": "In tempus ligula non velit lobortis dignissim. Integer ultrices lectus dui, nec bibendum lectus egestas sed. Proin congue quam dapibus, fermentum nulla nec, hendrerit ligula.",
    "footer": "Etiam auctor cursus sem ut gravida. Mauris quam metus, dictum a ante quis, iaculis tempor dolor. Sed interdum vitae urna id ullamcorper.",
    "jurisdiction": "Aliquam",
    "jurisdictionVisible": true,
    "collectionMethod": "",
    "collectionMethodVisible": false,
    "dataController": {
        "company": "Duis efficitur",
        "info": "Donec vitae dignissim",
        "address": "Etiam eu faucibus augue",
        "email": "sed.non@justo.com",
        "phoneNumber": "01 23 45 67 89"
    },
    "dataControllerVisible": true,
    "scope": "",
    "scopeVisible": false,
    "shortNoticeLink": "previewUrlShort",
    "shortNoticeLinkVisible": true,
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
        "dataControllerVisible": false,
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
        "containsSensitiveData": false,
        "containsMedicalData": false,
        "dataController": {
            "company": "Duis efficitur",
            "info": "Vitae donec dignissim",
            "address": "Faucibus eu etiam augue",
            "email": "non.sed@justo.com",
            "phoneNumber": "98 76 54 32 10"
        },
        "dataControllerVisible": false,
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
