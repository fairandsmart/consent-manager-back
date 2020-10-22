<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd" indent="yes" />
    <xsl:variable name="bundle">
        <labels>
            <key name="not-found">
                <value locale="default">Key not found</value>
                <value locale="en_EN">Key not found</value>
                <value locale="fr_FR">Clé introuvable</value>
            </key>
            <key name="title">
                <value locale="default">CONSENT RECEIPT</value>
                <value locale="en_EN">CONSENT RECEIPT</value>
                <value locale="fr_FR">REÇU DE CONSENTEMENT</value>
            </key>
            <key name="accept">
                <value locale="default">Accept</value>
                <value locale="en_EN">Accept</value>
                <value locale="fr_FR">Accepter</value>
            </key>
            <key name="date">
                <value locale="default">Date</value>
                <value locale="en_EN">Date</value>
                <value locale="fr_FR">Date</value>
            </key>
            <key name="expires">
                <value locale="default">Expires</value>
                <value locale="en_EN">Expires</value>
                <value locale="fr_FR">Expire le</value>
            </key>
            <key name="language">
                <value locale="default">Language</value>
                <value locale="en_EN">Language</value>
                <value locale="fr_FR">Langue</value>
            </key>
            <key name="language_fr_FR">
                <value locale="default">French (France)</value>
                <value locale="en_EN">French (France)</value>
                <value locale="fr_FR">Français (France)</value>
            </key>
            <key name="language_en_EN">
                <value locale="default">English (England)</value>
                <value locale="en_EN">English (England)</value>
                <value locale="fr_FR">Anglais (Royaume Unis)</value>
            </key>
            <key name="receipt_id">
                <value locale="default">Receipt Identifier</value>
                <value locale="en_EN">Receipt Identifier</value>
                <value locale="fr_FR">Identifiant du reçu</value>
            </key>
            <key name="data_collected">
                <value locale="default">Data collected</value>
                <value locale="en_EN">Data collected</value>
                <value locale="fr_FR">Données collectées</value>
            </key>
            <key name="data_retention">
                <value locale="default">Retention Period</value>
                <value locale="en_EN">Retention Period</value>
                <value locale="fr_FR">Durée de conservation</value>
            </key>
            <key name="data_usage">
                <value locale="default">Data Usage</value>
                <value locale="en_EN">Data Usage</value>
                <value locale="fr_FR">Utilisation</value>
            </key>
            <key name="data_purpose">
                <value locale="default">Data Purpose</value>
                <value locale="en_EN">Data Purpose</value>
                <value locale="fr_FR">Finalité</value>
            </key>
            <key name="subject_consent">
                <value locale="default">Subject Consent</value>
                <value locale="en_EN">Subject Consent</value>
                <value locale="fr_FR">Consentement</value>
            </key>
            <key name="subject_id">
                <value locale="default">Subject Id</value>
                <value locale="en_EN">Subject Id</value>
                <value locale="fr_FR">Identifiant utilisateur</value>
            </key>
            <key name="issuer_id">
                <value locale="default">Data Processor</value>
                <value locale="en_EN">Data Processor</value>
                <value locale="fr_FR">Gestionnaire des données</value>
            </key>
            <key name="data_controller_name">
                <value locale="default">Controller Name</value>
                <value locale="en_EN">Controller Name</value>
                <value locale="fr_FR">Responsable de traitement</value>
            </key>
            <key name="data_controller_details">
                <value locale="default">Controller Details</value>
                <value locale="en_EN">Controller Details</value>
                <value locale="fr_FR">Informations sur le responsable de traitement</value>
            </key>
            <key name="privacy_policy">
                <value locale="default">Privacy Policy</value>
                <value locale="en_EN">Privacy Policy</value>
                <value locale="fr_FR">Politique de confidentialité</value>
            </key>
            <key name="collection_method">
                <value locale="default">Collection Method</value>
                <value locale="en_EN">Collection Method</value>
                <value locale="fr_FR">Méthode de collecte</value>
            </key>
        </labels>
    </xsl:variable>
    <xsl:template name="translate">
        <xsl:param name="key"></xsl:param>
        <xsl:param name="locale"></xsl:param>
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

    <xsl:template match="/receipt" name="receipt" >
        <html>
            <head>
                <title><xsl:value-of select="headerNotice"/></title>
                <meta charset="UTF-8"/>
                <style>
                    body { font-family: Monaco, "DejaVu Sans Mono", "Lucida Console", "Andale Mono", monospace; font-size: medium; text-transform: uppercase; }
                    .spaced { margin-bottom: 18px; }
                    .receipt-title { font-weight: bold; }
                    .receipt-label { font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="receipt-title spaced">
                    <xsl:call-template name="translate">
                        <xsl:with-param name="key">title</xsl:with-param>
                        <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                    </xsl:call-template>
                </div>
                <div class="receipt-date">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">date</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="date"/>
                </div>
                <div class="receipt-date spaced">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">expires</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="expirationDate"/>
                </div>
                <div>
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">language</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:call-template name="translate">
                        <xsl:with-param name="key">language_<xsl:value-of select="locale"/></xsl:with-param>
                        <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                    </xsl:call-template>
                </div>
                <div class="spaced">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">receipt_id</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="transaction"/>
                </div>
                <div class="spaced">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">collection_method</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="collectionMethod"/>
                </div>

                <xsl:for-each select="consents/consent">
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_collected</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="current()/data"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_retention</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="current()/retention"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_usage</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="current()/usage"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_purpose</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:for-each select="current()/purposes/purpose">
                            <xsl:value-of select="current()"/>
                            <xsl:text> </xsl:text>
                        </xsl:for-each>
                    </div>
                    <div class="spaced consent-value">
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">subject_consent</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="current()/value"/>
                    </div>
                </xsl:for-each>

                <div class="spaced">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">subject_id</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="subject"/>
                </div>

                <div>
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">issuer_id</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="processor"/>
                </div>
                <xsl:if test="dataController and dataController/name and dataController/company">
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_controller_name</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="dataController/name"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_controller_details</xsl:with-param>
                                <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="dataController/company"/><xsl:text>: </xsl:text>
                        <xsl:value-of select="dataController/address"/><xsl:text>: </xsl:text>
                        <xsl:value-of select="dataController/email"/><xsl:text>: </xsl:text>
                        <xsl:value-of select="dataController/phoneNumber"/>
                    </div>
                </xsl:if>
                <div>
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">privacy_policy</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="privacyPolicyUrl"/>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>