<?xml version="1.0" encoding="UTF-8" ?>
<!--
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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exslt="http://exslt.org/common" >
    <xsl:output method="html" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd" indent="yes" />
    <xsl:variable name="bundle">
        <labels>
            <key name="not-found">
                <value language="default">Key not found</value>
                <value language="en">Key not found</value>
                <value language="fr">Clé introuvable</value>
            </key>
            <key name="title">
                <value language="default">CONSENT RECEIPT</value>
                <value language="en">CONSENT RECEIPT</value>
                <value language="fr">REÇU DE CONSENTEMENT</value>
            </key>
            <key name="accept">
                <value language="default">Accept</value>
                <value language="en">Accept</value>
                <value language="fr">Accepter</value>
            </key>
            <key name="accepted">
                <value language="default">Accepted</value>
                <value language="en">Accepted</value>
                <value language="fr">Accepté</value>
            </key>
            <key name="refused">
                <value language="default">Refused</value>
                <value language="en">Refused</value>
                <value language="fr">Refusé</value>
            </key>
            <key name="date">
                <value language="default">Timestamp</value>
                <value language="en">Timestamp</value>
                <value language="fr">Horodatage</value>
            </key>
            <key name="expires">
                <value language="default">Consent and Receipt Expiration Date</value>
                <value language="en">Consent and Receipt Expiration Date</value>
                <value language="fr">Date d’expiration du consentement et du reçu</value>
            </key>
            <key name="expires_explanation">
                <value language="default">(After this date, the data controller must collect your consent again)</value>
                <value language="en">(After this date, the data controller must collect your consent again)</value>
                <value language="fr">(Date jusqu'à laquelle vous pouvez accéder et modifier votre consentement)</value>
            </key>
            <key name="language">
                <value language="default">Language</value>
                <value language="en">Language</value>
                <value language="fr">Langue</value>
            </key>
            <key name="language_fr">
                <value language="default">French (France)</value>
                <value language="en">French (France)</value>
                <value language="fr">Français (France)</value>
            </key>
            <key name="language_en">
                <value language="default">English (England)</value>
                <value language="en">English (England)</value>
                <value language="fr">Anglais (Royaume Unis)</value>
            </key>
            <key name="receipt_id">
                <value language="default">Consent Record ID</value>
                <value language="en">Consent Record ID</value>
                <value language="fr">Identifiant du reçu</value>
            </key>
            <key name="data_collected">
                <value language="default">PII Categories</value>
                <value language="en">PII Categories</value>
                <value language="fr">Données utilisées</value>
            </key>
            <key name="data_retention">
                <value language="default">Data Retention Duration</value>
                <value language="en">Data Retention Duration</value>
                <value language="fr">Durée de conservation des données</value>
            </key>
            <key name="data_usage">
                <value language="default">Purpose description</value>
                <value language="en">Purpose description</value>
                <value language="fr">Description de la finalité</value>
            </key>
            <key name="data_purpose">
                <value language="default">Purpose categories</value>
                <value language="en">Purpose categories</value>
                <value language="fr">Catégories de la finalité</value>
            </key>
            <key name="CONSENT_CORE_SERVICE">
                <value language="default">Core service</value>
                <value language="en">Core service</value>
                <value language="fr">Service principal</value>
            </key>
            <key name="CONSENT_IMPROVED_SERVICE">
                <value language="default">Improved service</value>
                <value language="en">Improved service</value>
                <value language="fr">Service amélioré</value>
            </key>
            <key name="CONSENT_MARKETING">
                <value language="default">Marketing</value>
                <value language="en">Marketing</value>
                <value language="fr">Marketing</value>
            </key>
            <key name="CONSENT_THIRD_PART_SHARING">
                <value language="default">Sharing with third parties</value>
                <value language="en">Sharing with third parties</value>
                <value language="fr">Partage à des tierces-parties</value>
            </key>
            <key name="CONSENT_RESEARCH">
                <value language="default">Research</value>
                <value language="en">Research</value>
                <value language="fr">Recherche</value>
            </key>
            <key name="subject_consent">
                <value language="default">Status</value>
                <value language="en">Status</value>
                <value language="fr">Réponse</value>
            </key>
            <key name="subject_id">
                <value language="default">PII Principal ID</value>
                <value language="en">PII Principal ID</value>
                <value language="fr">Identifiant de la personne concernée</value>
            </key>
            <key name="data_controller_name">
                <value language="default">PII Controller</value>
                <value language="en">PII Controller</value>
                <value language="fr">Responsable de traitement</value>
            </key>
            <key name="privacy_policy">
                <value language="default">Privacy Notice</value>
                <value language="en">Privacy Notice</value>
                <value language="fr">Politique de confidentialité</value>
            </key>
            <key name="collection_method">
                <value language="default">Collection Method</value>
                <value language="en">Collection Method</value>
                <value language="fr">Méthode de collecte</value>
            </key>
            <key name="collection_method_WEBFORM">
                <value language="default">Web form</value>
                <value language="en">Web form</value>
                <value language="fr">Formulaire web</value>
            </key>
            <key name="collection_method_OPERATOR">
                <value language="default">Operator</value>
                <value language="en">Operator</value>
                <value language="fr">Opérateur</value>
            </key>
            <key name="collection_method_EMAIL">
                <value language="default">Modification link in the notification email</value>
                <value language="en">Modification link in the notification email</value>
                <value language="fr">Lien de modification de l'email de notification</value>
            </key>
            <key name="collection_method_RECEIPT">
                <value language="default">Modification link in the receipt</value>
                <value language="en">Modification link in the receipt</value>
                <value language="fr">Lien de modification du reçu</value>
            </key>
            <key name="collection_method_USER_PAGE">
                <value language="default">User interface</value>
                <value language="en">User interface</value>
                <value language="fr">Interface utilisateur</value>
            </key>
            <key name="update_url">
                <value language="default">Click here to update your consent</value>
                <value language="en">Click here to update your consent</value>
                <value language="fr">Cliquer ici pour modifier votre consentement</value>
            </key>
            <key name="general_info">
                <value language="default">Informations</value>
                <value language="en">Informations</value>
                <value language="fr">Informations générales</value>
            </key>
            <key name="sensitive_data">
                <value language="default">About sensitive Data</value>
                <value language="en">Sensitive Data</value>
                <value language="fr">À propos des données sensibles</value>
            </key>
            <key name="contains_sensitive_data">
                <value language="default">The transmitted information contains sensitive data.</value>
                <value language="en">The transmitted information contains sensitive data.</value>
                <value language="fr">Les informations transmises contiennent des données sensibles.</value>
            </key>
            <key name="contains_medical_data">
                <value language="default">The transmitted information contains sensitive data, including medical data.</value>
                <value language="en">The transmitted information contains sensitive data, including medical data.</value>
                <value language="fr">Les informations transmises contiennent des données sensibles, dont des données de santé.</value>
            </key>
            <key name="third_parties">
                <value language="default">These data will be shared with:</value>
                <value language="en">These data will be shared with:</value>
                <value language="fr">Ces données seront transmises à :</value>
            </key>
        </labels>
    </xsl:variable>
    <xsl:template name="translate">
        <xsl:param name="key" />
        <xsl:param name="language" />
        <xsl:choose>
            <xsl:when test="exslt:node-set($bundle)/labels/key[@name = $key]">
                <xsl:choose>
                    <xsl:when test="exslt:node-set($bundle)/labels/key[@name = $key]/value[@language = $language]">
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = $key]/value[@language = $language]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = $key]/value[@language = 'default']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="exslt:node-set($bundle)/labels/key[@name = 'not-found']/value[@language = $language]">
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = 'not-found']/value[@language = $language]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = 'not-found']/value[@language = 'default']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="formatdate">
        <xsl:param name="DateTimeStr" />

        <xsl:variable name="datestr">
            <xsl:value-of select="substring-before($DateTimeStr,'T')" />
        </xsl:variable>

        <xsl:variable name="mm">
            <xsl:value-of select="substring($datestr,6,2)" />
        </xsl:variable>

        <xsl:variable name="dd">
            <xsl:value-of select="substring($datestr,9,2)" />
        </xsl:variable>

        <xsl:variable name="yyyy">
            <xsl:value-of select="substring($datestr,1,4)" />
        </xsl:variable>

        <xsl:value-of select="concat($mm,'/', $dd, '/', $yyyy)" />
    </xsl:template>

    <xsl:template match="/receipt" name="receipt">
        <xsl:variable name="lang" select="language"/>
        <html>
            <head>
                <title>
                    <xsl:call-template name="translate">
                        <xsl:with-param name="key">title</xsl:with-param>
                        <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                    </xsl:call-template>
                </title>
                <meta charset="UTF-8"/>
                <style>
                    html {
                        font-family: Arial, Helvetica, sans-serif;
                    }

                    .list-label {
                        font-weight: bold;
                    }

                    body {
                        margin: 0;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        height: 100vh;
                    }

                    .logo-container {
                        padding: 0 24px;
                        text-align: center;
                        margin-top: 24px;
                    }

                    .logo {
                        max-height: 100px;
                    }

                    .info-header {
                        margin: 24px;
                    }

                    .header-title {
                        margin-top: 0;
                        margin-bottom: 8px;
                    }

                    .header-body {
                        color: rgba(0, 0, 0, 0.54);
                        font-weight: bold;
                    }

                    .privacy-policy-container {
                        margin: 8px 0;
                        text-align: center;
                    }

                    .privacy-policy {
                        text-decoration: none;
                        color: #2196F3;
                    }

                    .elements-list {
                        overflow-y: auto;
                        padding: 16px 24px 0 24px;
                        height: 100%;
                    }

                    .element {
                        padding: 6px 0 6px 12px;
                    }

                    .element-header {
                        display: flex;
                        justify-content: space-between;
                        margin-bottom: 8px;
                    }

                    .element-header .element-title {
                        margin: 0;
                    }

                    .item-body, .block-body {
                        color: rgba(0, 0, 0, 0.54);
                    }

                    .block-body li:first-of-type {
                        margin-top: 8px;
                    }

                    .block-container {
                        padding: 12px;
                        margin: 12px 0;
                        border-radius: 4px;
                        background-color: #f5f5f5;
                        text-align: left;
                    }

                    .block-container .block-title {
                        margin: 0;
                    }

                    .block-container .block-body {
                        margin: 0;
                        padding-left: 16px;
                        overflow: hidden;
                        word-break: break-word;
                    }

                    .information-container .accordion-header {
                        cursor: initial;
                    }

                    .accordion-header {
                        cursor: pointer;
                        display: flex;
                        justify-content: space-between;
                    }

                    .accordion-hidden {
                        max-height: 0;
                    }

                    .accordion-visible {
                        max-height: 500px;
                        transition: .7s ease-in-out;
                    }

                    .accordion-symbol-open:before {
                        content: "+";
                    }

                    .accordion-symbol-close:before {
                        content: "-";
                    }

                    .item-container {
                        margin-bottom: 8px;
                    }

                    .info-header {
                        text-align: center;
                        margin: 0 0 8px 12px;
                        border-bottom: 1px solid #eee;
                    }

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

                    .header-title {
                        margin-top: 0;
                        margin-bottom: 8px;
                    }

                    .header-body {
                        margin: 4px 0;
                    }

                    .info-header, .qr-code-container, .privacy-policy-container {
                        flex-shrink: 0;
                    }

                    .item-body, .block-body {
                        padding-left: 16px;
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
                <xsl:if test="layout and layout/themePath">
                    <link rel="stylesheet" type="text/css">
                        <xsl:attribute name="href"><xsl:value-of select="layout/themePath"/></xsl:attribute>
                    </link>
                </xsl:if>
            </head>
            <body>
                <div class="receipt">
                    <xsl:if test="layout and layout/logoPath">
                        <div class="logo-container">
                            <xsl:attribute name="style">text-align: <xsl:value-of select="layout/logoPosition"/></xsl:attribute>
                            <img class="logo">
                                <xsl:attribute name="src"><xsl:value-of select="layout/logoPath"/></xsl:attribute>
                                <xsl:if test="layout/logoAltText">
                                    <xsl:attribute name="alt"><xsl:value-of select="layout/logoAltText"/></xsl:attribute>
                                </xsl:if>
                            </img>
                        </div>
                    </xsl:if>
                    <div class="elements-list">
                        <div class="info-header">
                            <h2 class="header-title">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">title</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </h2>
                            <div class="block-container information-container">
                                <h4 class="block-title accordion-header">
                                    <xsl:call-template name="translate">
                                        <xsl:with-param name="key">general_info</xsl:with-param>
                                        <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                    </xsl:call-template>
                                </h4>
                                <ul class="block-body accordion-visible">
                                    <li>
                                        <span class="list-label">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">subject_id</xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                            <xsl:text>: </xsl:text>
                                        </span>
                                        <span class="list-value"><xsl:value-of select="subject"/></span>
                                    </li>
                                    <li>
                                        <span class="list-label">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">language</xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                            <xsl:text>: </xsl:text>
                                        </span>
                                        <span class="list-value">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">language_<xsl:value-of select="$lang"/></xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="list-label">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">collection_method</xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                            <xsl:text>: </xsl:text>
                                        </span>
                                        <span class="list-value">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">collection_method_<xsl:value-of select="collectionMethod"/></xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="list-label">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">date</xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                            <xsl:text>: </xsl:text>
                                        </span>
                                        <span class="list-value">
                                            <xsl:call-template name="formatdate">
                                                <xsl:with-param name="DateTimeStr" select="date"/>
                                            </xsl:call-template>
                                        </span>
                                    </li>
                                    <xsl:if test="expirationDate">
                                        <li>
                                            <span class="list-label">
                                                <xsl:call-template name="translate">
                                                    <xsl:with-param name="key">expires</xsl:with-param>
                                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                                </xsl:call-template>
                                                <xsl:text>: </xsl:text>
                                            </span>
                                            <span class="list-value">
                                                <xsl:call-template name="formatdate">
                                                    <xsl:with-param name="DateTimeStr" select="expirationDate"/>
                                                </xsl:call-template>
                                            </span>
                                            <div>
                                                <xsl:call-template name="translate">
                                                    <xsl:with-param name="key">expires_explanation</xsl:with-param>
                                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                                </xsl:call-template>
                                            </div>
                                        </li>
                                    </xsl:if>
                                    <li>
                                        <span class="list-label">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">receipt_id</xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                            <xsl:text>: </xsl:text>
                                        </span>
                                        <span class="list-value"><xsl:value-of select="transaction"/></span>
                                    </li>
                                </ul>
                            </div>
                            <xsl:if test="updateUrl">
                                <div class="privacy-policy-container">
                                    <xsl:element name="a" >
                                        <xsl:attribute name="href"><xsl:value-of select="updateUrl"/></xsl:attribute>
                                        <xsl:attribute name="class">privacy-policy</xsl:attribute>
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">update_url</xsl:with-param>
                                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                    </xsl:element>
                                </div>
                            </xsl:if>
                        </div>
                        <xsl:for-each select="consents/consent">
                            <div class="element">
                                <div class="element-header">
                                    <h3 class="element-title"><xsl:value-of select="current()/title"/></h3>
                                    <div>
                                        <xsl:attribute name="class">
                                            <xsl:if test="current()/value = 'accepted'">processing-response accepted</xsl:if>
                                            <xsl:if test="current()/value = 'refused'">processing-response refused</xsl:if>
                                        </xsl:attribute>
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key"><xsl:value-of select="current()/value"/></xsl:with-param>
                                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                    </div>
                                </div>
                                <div class="item-container">
                                    <p class="item-body"><xsl:value-of select="current()/data"/></p>
                                    <p class="item-body"><xsl:value-of select="current()/retention/fullText"/></p>
                                    <p class="item-body"><xsl:value-of select="current()/usage"/></p>
                                    <p class="item-body">
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">receipt_id</xsl:with-param>
                                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                        <xsl:text>: </xsl:text>
                                        <xsl:for-each select="current()/purposes/purpose">
                                            <xsl:if test="position() > 1"><xsl:text>, </xsl:text></xsl:if>
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key"><xsl:value-of select="current()"/></xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </xsl:for-each>
                                    </p>
                                </div>
                                <xsl:if test="current()/containsSensitiveData = 'true'">
                                    <div class="block-container sensitive-container">
                                        <h4 class="block-title">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">sensitive_data</xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </h4>
                                        <ul class="block-body">
                                            <li>
                                                <span class="list-value">
                                                    <xsl:if test="current()/containsMedicalData = 'true'">
                                                        <xsl:call-template name="translate">
                                                            <xsl:with-param name="key">contains_medical_data</xsl:with-param>
                                                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:if>
                                                    <xsl:if test="current()/containsMedicalData = 'false'">
                                                        <xsl:call-template name="translate">
                                                            <xsl:with-param name="key">contains_sensitive_data</xsl:with-param>
                                                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                                        </xsl:call-template>
                                                    </xsl:if>
                                                </span>
                                            </li>
                                        </ul>
                                    </div>
                                </xsl:if>
                                <xsl:if test="current()/thirdParties">
                                    <div class="block-container third-parties-container">
                                        <h4 class="block-title">
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">third_parties</xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </h4>
                                        <ul class="block-body">
                                            <xsl:for-each select="current()/thirdParties/thirdParty">
                                                <li>
                                                    <span class="list-label"><xsl:value-of select="current()/name" /> : </span>
                                                    <span class="list-value"><xsl:value-of select="current()/value" /></span>
                                                </li>
                                            </xsl:for-each>
                                        </ul>
                                    </div>
                                </xsl:if>
                            </div>
                        </xsl:for-each>
                    </div>
                    <div class="qr-code-container">
                        <xsl:if test="updateUrlQrCode">
                            <div>
                                <img class="qr-code">
                                    <xsl:attribute name="src">
                                        <xsl:value-of select="updateUrlQrCode"/>
                                    </xsl:attribute>
                                </img>
                            </div>
                        </xsl:if>
                    </div>
                    <div class="privacy-policy-container">
                        <xsl:if test="dataController and dataController/company">
                            <p>
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">data_controller_name</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                                <xsl:text>: </xsl:text>
                                <xsl:value-of select="dataController/company"/>
                            </p>
                        </xsl:if>
                        <p>
                            <a class="privacy-policy">
                                <xsl:attribute name="href"><xsl:value-of select="privacyPolicyUrl"/></xsl:attribute>
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">privacy_policy</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </a>
                        </p>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
