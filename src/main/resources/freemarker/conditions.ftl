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
<!DOCTYPE html>
<html lang="${language}">
<head>
    <#include "macros/lang-macros.ftl">

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Outil de gestion des consentements Fair&Smart">
    <meta name="author" content="Fair&Smart">

    <#include "style/common-style.ftl">
    <#include "style/conditions-style.ftl">
    <#include "components/theme.ftl">

    <@fetchMultiLangContent data.elements[0]></@fetchMultiLangContent>
    <#assign conditions=langContent>
    <#if conditions?is_hash>
        <title>${conditions.title?html}</title>
    <#else>
        <title><@readBundle "conditionsPageTitle" "missingValue"></@readBundle></title>
    </#if>
</head>

<body>
<#if conditions?is_hash>
    <form method="post" id="consent" name="consent" action="#" class="conditions-container">
        <#if !data.preview>
            <input name="token" id="token" value="${data.token}" hidden/>
            <input name="${data.elements[0].identifier}" id="choice" value="refused" hidden/>
            <button type="submit" id="submit" hidden></button>
        </#if>

        <#include "components/conditions.ftl">
    </form>
<#else>
    <div class="conditions-container">
        <h1><@writeError "missingValue"></@writeError></h1>
    </div>
</#if>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js" integrity="sha512-bLT0Qm9VnAYZDflyKcBaQ2gg0hSYNQrJ8RilYldYQ1FxQYoCLtUjuuRuZo+fjqhx/qtq/1itJ0C2ejDxltZVFg==" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/4.2.11/iframeResizer.contentWindow.min.js" integrity="sha512-FOf4suFgz7OrWmBiyyWW48u/+6GaaAFSDHagh2EBu/GH/1+OQSYc0NFGeGeZK0gZ3vuU1ovmzVzD6bxmT4vayg==" crossorigin="anonymous"></script>
<script src="/assets/js/conditions.js"></script>

</body>
</html>
