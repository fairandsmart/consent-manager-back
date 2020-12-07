<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exslt="http://exslt.org/common" >
    <xsl:output method="html" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd" indent="yes" />
    <xsl:variable name="bundle">
        <labels>
            <key name="not-found">
                <value locale="default">Key not found</value>
                <value locale="en">Key not found</value>
                <value locale="fr">Clé introuvable</value>
            </key>
            <key name="title">
                <value locale="default">CONSENT RECEIPT</value>
                <value locale="en">CONSENT RECEIPT</value>
                <value locale="fr">REÇU DE CONSENTEMENT</value>
            </key>
            <key name="accept">
                <value locale="default">Accept</value>
                <value locale="en">Accept</value>
                <value locale="fr">Accepter</value>
            </key>
            <key name="accepted">
                <value locale="default">Accepted</value>
                <value locale="en">Accepted</value>
                <value locale="fr">Accepté</value>
            </key>
            <key name="refused">
                <value locale="default">Refused</value>
                <value locale="en">Refused</value>
                <value locale="fr">Refusé</value>
            </key>
            <key name="date">
                <value locale="default">Date</value>
                <value locale="en">Date</value>
                <value locale="fr">Date</value>
            </key>
            <key name="expires">
                <value locale="default">Expires</value>
                <value locale="en">Expires</value>
                <value locale="fr">Expire le</value>
            </key>
            <key name="language">
                <value locale="default">Language</value>
                <value locale="en">Language</value>
                <value locale="fr">Langue</value>
            </key>
            <key name="language_fr">
                <value locale="default">French (France)</value>
                <value locale="en">French (France)</value>
                <value locale="fr">Français (France)</value>
            </key>
            <key name="language_en">
                <value locale="default">English (England)</value>
                <value locale="en">English (England)</value>
                <value locale="fr">Anglais (Royaume Unis)</value>
            </key>
            <key name="receipt_id">
                <value locale="default">Receipt Identifier</value>
                <value locale="en">Receipt Identifier</value>
                <value locale="fr">Identifiant du reçu</value>
            </key>
            <key name="data_collected">
                <value locale="default">Data collected</value>
                <value locale="en">Data collected</value>
                <value locale="fr">Données collectées</value>
            </key>
            <key name="data_retention">
                <value locale="default">Retention Period</value>
                <value locale="en">Retention Period</value>
                <value locale="fr">Durée de conservation</value>
            </key>
            <key name="data_usage">
                <value locale="default">Data Usage</value>
                <value locale="en">Data Usage</value>
                <value locale="fr">Utilisation</value>
            </key>
            <key name="data_purpose">
                <value locale="default">Data Purpose</value>
                <value locale="en">Data Purpose</value>
                <value locale="fr">Finalité</value>
            </key>
            <key name="subject_consent">
                <value locale="default">Subject Consent</value>
                <value locale="en">Subject Consent</value>
                <value locale="fr">Consentement</value>
            </key>
            <key name="subject_id">
                <value locale="default">Subject Id</value>
                <value locale="en">Subject Id</value>
                <value locale="fr">Identifiant utilisateur</value>
            </key>
            <key name="issuer_id">
                <value locale="default">Data Processor</value>
                <value locale="en">Data Processor</value>
                <value locale="fr">Gestionnaire des données</value>
            </key>
            <key name="data_controller_name">
                <value locale="default">Controller Name</value>
                <value locale="en">Controller Name</value>
                <value locale="fr">Responsable de traitement</value>
            </key>
            <key name="data_controller_details">
                <value locale="default">Controller Details</value>
                <value locale="en">Controller Details</value>
                <value locale="fr">Informations sur le responsable de traitement</value>
            </key>
            <key name="privacy_policy">
                <value locale="default">Privacy Policy</value>
                <value locale="en">Privacy Policy</value>
                <value locale="fr">Politique de confidentialité</value>
            </key>
            <key name="collection_method">
                <value locale="default">Collection Method</value>
                <value locale="en">Collection Method</value>
                <value locale="fr">Méthode de collecte</value>
            </key>
            <key name="collection_method_WEBFORM">
                <value locale="default">Web form</value>
                <value locale="en">Web form</value>
                <value locale="fr">Formulaire web</value>
            </key>
            <key name="collection_method_OPERATOR">
                <value locale="default">Operator</value>
                <value locale="en">Operator</value>
                <value locale="fr">Opérateur</value>
            </key>
            <key name="update_url">
                <value locale="default">Update Consent Link</value>
                <value locale="en">Update Consent Link</value>
                <value locale="fr">Lien de modification</value>
            </key>
            <key name="update_url_link">
                <value locale="default">Click here to update your consent</value>
                <value locale="en">Click here to update your consent</value>
                <value locale="fr">Cliquer ici pour modifier votre consentement</value>
            </key>
            <key name="general_info">
                <value locale="default">Informations</value>
                <value locale="en">Informations</value>
                <value locale="fr">Informations générales</value>
            </key>
            <key name="sensitive_data">
                <value locale="default">About sensitive Data</value>
                <value locale="en">Sensitive Data</value>
                <value locale="fr">À propos des données sensibles</value>
            </key>
            <key name="contains_sensitive_data">
                <value locale="default">Contains sensitive data</value>
                <value locale="en">Contains sensitive data</value>
                <value locale="fr">Contient des données sensibles</value>
            </key>
            <key name="contains_medical_data">
                <value locale="default">Contains medical data</value>
                <value locale="en">Contains medical data</value>
                <value locale="fr">Contient des données médicales</value>
            </key>
            <key name="third_parties">
                <value locale="default">These data will be shared with:</value>
                <value locale="en">These data will be shared with:</value>
                <value locale="fr">Ces données seront transmises à :</value>
            </key>
        </labels>
    </xsl:variable>
    <xsl:template name="translate">
        <xsl:param name="key" />
        <xsl:param name="locale" />
        <xsl:choose>
            <xsl:when test="exslt:node-set($bundle)/labels/key[@name = $key]">
                <xsl:choose>
                    <xsl:when test="exslt:node-set($bundle)/labels/key[@name = $key]/value[@locale = $locale]">
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = $key]/value[@locale = $locale]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = $key]/value[@locale = 'default']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="exslt:node-set($bundle)/labels/key[@name = 'not-found']/value[@locale = $locale]">
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = 'not-found']/value[@locale = $locale]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="exslt:node-set($bundle)/labels/key[@name = 'not-found']/value[@locale = 'default']"/>
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

    <xsl:template match="/receipt" name="receipt" >
        <xsl:variable name="lang" select="language"/>
        <html>
            <head>
                <title>
                    <xsl:call-template name="translate">
                        <xsl:with-param name="key">title</xsl:with-param>
                        <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                    </xsl:call-template>
                </title>
                <meta charset="UTF-8"/>
                <style>
                    html {
                    font-family: Aria, Helvetica, sans-serif;
                    }

                    body {
                    margin: 0;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    height: 100vh;
                    }

                    .receipt {
                    position: relative;
                    display: flex;
                    flex-direction: column;
                    width: 100%;
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

                    .header {
                    text-align: center;
                    padding: 16px 24px;
                    border-bottom: 1px solid #eee;
                    }

                    .logo-wrapper {
                    text-align: center;
                    margin-top: 24px;
                    }

                    .header h2 {
                    margin-top: 0;
                    margin-bottom: 8px;
                    }
                    .header-body {
                    color: rgba(0, 0, 0, 0.54);
                    font-weight: bold;
                    margin: 4px 0;
                    }

                    .block-wrapper {
                    padding: 12px;
                    margin: 12px 0;
                    border-radius: 4px;
                    background-color: #f5f5f5;
                    text-align: left;
                    }
                    .block-wrapper h4 {
                    margin: 0;
                    }

                    .processing-list {
                    overflow-y: auto;
                    padding: 16px 24px;
                    height: 100%;
                    }
                    .content-fade {
                    background: linear-gradient(white, rgba(255, 255, 255, 0));
                    height: 32px;
                    margin: 0 16px -32px 16px;
                    z-index: 10;
                    overflow-y: hidden;
                    pointer-events: none;
                    flex-shrink: 0;
                    }

                    .content-fade, .header, .qr-code, .privacy-policy-link-wrapper {
                    flex-shrink: 0;
                    }

                    .processing-body {
                    color: rgba(0, 0, 0, 0.54);
                    padding-left: 16px;
                    }
                    .list-label {
                    font-weight: bold;
                    }

                    .privacy-policy-link {
                    text-decoration: none;
                    color: #2196F3;
                    }
                    .privacy-policy-link-wrapper {
                    text-align: center;
                    }
                    .processing {
                    padding: 24px;
                    }

                    .processing-header {
                    display: flex;
                    flex-direction: row;
                    flex-wrap: nowrap;
                    align-items: center;
                    justify-content: space-between;
                    }

                    .processing-header h3 {
                    margin: 0;
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

                    .processing-info p {
                    color: rgba(0, 0, 0, 0.74);
                    }

                    .qr-code {
                    text-align: center;
                    border-bottom: 1px solid #eee;
                    }

                    .qr-code img {
                    width: 150px;
                    }

                    .purpose-container {
                    text-align: center;
                    margin-top: 8px !important;
                    }
                    .purpose {
                    width: 32px;
                    height: 32px;
                    }
                </style>
                <xsl:if test="themePath">
                    <link rel="stylesheet" type="text/css">
                        <xsl:attribute name="href"><xsl:value-of select="themePath"/></xsl:attribute>
                    </link>
                </xsl:if>
            </head>
            <body>
                <div class="receipt">
                    <xsl:if test="logoPath">
                        <div class="logo-wrapper">
                            <xsl:element name="img">
                                <xsl:attribute name="src"><xsl:value-of select="logoPath"/></xsl:attribute>
                                <xsl:attribute name="href"><xsl:value-of select="logoPathAlt"/></xsl:attribute>
                            </xsl:element>
                        </div>
                    </xsl:if>
                    <div class="header">
                        <h2>
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">title</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                            </xsl:call-template>
                        </h2>
                        <p class="header-body">-</p>
                        <div class="block-wrapper">
                            <h4 class="controller-header">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">general_info</xsl:with-param>
                                    <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </h4>
                            <ul class="processing-body controller-open">
                                <li>
                                    <span class="list-label">
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">subject_id</xsl:with-param>
                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                        <xsl:text>: </xsl:text>
                                    </span>
                                    <span class="list-value"><xsl:value-of select="subject"/></span>
                                </li>
                                <li>
                                    <span class="list-label">
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">language</xsl:with-param>
                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                        <xsl:text>: </xsl:text>
                                    </span>
                                    <span class="list-value">
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">language_<xsl:value-of select="$lang"/></xsl:with-param>
                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                    </span>
                                </li>
                                <li>
                                    <span class="list-label">
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">collection_method</xsl:with-param>
                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                        <xsl:text>: </xsl:text>
                                    </span>
                                    <span class="list-value">
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">collection_method_<xsl:value-of select="collectionMethod"/></xsl:with-param>
                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                    </span>
                                </li>
                                <li>
                                    <span class="list-label"> <xsl:call-template name="translate">
                                        <xsl:with-param name="key">date</xsl:with-param>
                                        <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                    </xsl:call-template>
                                        <xsl:text>: </xsl:text>
                                    </span>
                                    <span class="list-value">
                                        <xsl:call-template name="formatdate">
                                            <xsl:with-param name="DateTimeStr" select="date"/>
                                        </xsl:call-template>
                                    </span>
                                </li>
                                <li>
                                    <span class="list-label">  <xsl:call-template name="translate">
                                        <xsl:with-param name="key">expires</xsl:with-param>
                                        <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                    </xsl:call-template>
                                        <xsl:text>: </xsl:text>
                                    </span>
                                    <span class="list-value">
                                        <xsl:call-template name="formatdate">
                                            <xsl:with-param name="DateTimeStr" select="expirationDate"/>
                                        </xsl:call-template>
                                    </span>
                                </li>
                                <li>
                                    <span class="list-label">
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">receipt_id</xsl:with-param>
                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                        <xsl:text>: </xsl:text>
                                    </span>
                                    <span class="list-value"><xsl:value-of select="transaction"/></span>
                                </li>
                            </ul>
                        </div>
                        <xsl:if test="updateUrl">
                            <div class="privacy-policy-link-wrapper">
                                <xsl:element name="a" >
                                    <xsl:attribute name="href"><xsl:value-of select="updateUrl"/></xsl:attribute>
                                    <xsl:attribute name="class">privacy-policy-link</xsl:attribute>
                                    <xsl:call-template name="translate">
                                        <xsl:with-param name="key">update_url_link</xsl:with-param>
                                        <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                    </xsl:call-template>
                                </xsl:element>
                            </div>
                        </xsl:if>
                    </div>
                    <div class="processing-list">
                        <xsl:for-each select="consents/consent">
                            <div class="processing">
                                <div class="processing-header">
                                    <h3><xsl:value-of select="current()/title"/></h3>
                                    <div>
                                        <xsl:attribute name="class">
                                            <xsl:if test="current()/value = 'accepted'">processing-response accepted </xsl:if>
                                            <xsl:if test="current()/value = 'refused'">processing-response refused </xsl:if>
                                        </xsl:attribute>
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key"><xsl:value-of select="current()/value"/></xsl:with-param>
                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                    </div>
                                </div>
                                <div class="processing-info">
                                    <p><xsl:value-of select="current()/data"/></p>
                                    <p><xsl:value-of select="current()/retention"/></p>
                                    <p><xsl:value-of select="current()/usage"/></p>
                                </div>
                                <div class="purpose-container">
                                    <xsl:for-each select="current()/purposes/purpose">
                                        <xsl:element name="img">
                                            <xsl:attribute name="class">purpose</xsl:attribute>
                                            <xsl:attribute name="src">/assets/img/purpose/<xsl:value-of select="current()" />.png</xsl:attribute>
                                        </xsl:element>
                                    </xsl:for-each>
                                </div>
                                <xsl:if test="current()/containsSensitiveData = 'true' or current()/containsMedicalData = 'true'">
                                    <div class="block-wrapper">
                                        <h4>
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">sensitive_data</xsl:with-param>
                                                <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </h4>
                                        <ul class="processing-body">
                                            <xsl:if test="current()/containsSensitiveData = 'true'">
                                                <li>
                                                    <span class="list-value">
                                                        <xsl:call-template name="translate">
                                                            <xsl:with-param name="key">contains_sensitive_data</xsl:with-param>
                                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                                        </xsl:call-template>
                                                    </span>
                                                </li>
                                            </xsl:if>
                                            <xsl:if test="current()/containsMedicalData = 'true'">
                                                <li>
                                                    <span class="list-value">
                                                        <xsl:call-template name="translate">
                                                            <xsl:with-param name="key">contains_medical_data</xsl:with-param>
                                                            <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                                        </xsl:call-template>
                                                    </span>
                                                </li>
                                            </xsl:if>
                                        </ul>
                                    </div>
                                </xsl:if>
                                <xsl:if test="current()/thirdParties">
                                    <div class="block-wrapper">
                                        <h4>
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key">third_parties</xsl:with-param>
                                                <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </h4>
                                        <ul class="processing-body">
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
                    <div class="qr-code">
                        <xsl:if test="updateUrlQrCode">
                            <div class="spaced">
                                <img>
                                    <xsl:attribute name="src">
                                        <xsl:value-of select="updateUrlQrCode"/>
                                    </xsl:attribute>
                                </img>
                            </div>
                        </xsl:if>
                    </div>
                    <div class="privacy-policy-link-wrapper">
                        <a class="privacy-policy-link">
                            <xsl:attribute name="href"><xsl:value-of select="privacyPolicyUrl"/></xsl:attribute>
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">privacy_policy</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="$lang"/></xsl:with-param>
                            </xsl:call-template>
                        </a>
                    </div>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>