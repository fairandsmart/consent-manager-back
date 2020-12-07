<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exslt="http://exslt.org/common">
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>
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
                <value language="fr">(une fois cette date expirée, le responsable de traitement devra collecter à nouveau votre consentement)</value>
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
                <value language="default">English (United Kingdom)</value>
                <value language="en">English (United Kingdom)</value>
                <value language="fr">Anglais (Royaume-Uni)</value>
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
            <key name="YEAR">
                <value language="default">year(s)</value>
                <value language="en">year(s)</value>
                <value language="fr">an(s)</value>
            </key>
            <key name="MONTH">
                <value language="default">month(s)</value>
                <value language="en">month(s)</value>
                <value language="fr">mois</value>
            </key>
            <key name="WEEK">
                <value language="default">week(s)</value>
                <value language="en">week(s)</value>
                <value language="fr">semaine(s)</value>
            </key>
            <key name="purpose">
                <value language="default">Purpose</value>
                <value language="en">Purpose</value>
                <value language="fr">Finalité</value>
            </key>
            <key name="data_usage">
                <value language="default">Purpose description</value>
                <value language="en">Purpose description</value>
                <value language="fr">Description de la finalité</value>
            </key>
            <key name="data_purpose">
                <value language="default">Purpose category</value>
                <value language="en">Purpose category</value>
                <value language="fr">Catégorie de la finalité</value>
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
            <key name="update_url">
                <value language="default">Change your consent</value>
                <value language="en">Change your consent</value>
                <value language="fr">Modifier votre consentement</value>
            </key>
            <key name="collection_method">
                <value language="default">Collection Method</value>
                <value language="en">Collection Method</value>
                <value language="fr">Méthode de collecte</value>
            </key>
            <key name="WEBFORM">
                <value language="default">Webform</value>
                <value language="en">Webform</value>
                <value language="fr">Formulaire Web</value>
            </key>
            <key name="OPERATOR">
                <value language="default">Operator</value>
                <value language="en">Operator</value>
                <value language="fr">Opérateur</value>
            </key>
            <key name="EMAIL">
                <value language="default">Email</value>
                <value language="en">Email</value>
                <value language="fr">Email</value>
            </key>
        </labels>
    </xsl:variable>
    <xsl:template name="translate">
        <xsl:param name="key"></xsl:param>
        <xsl:param name="language"></xsl:param>
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

    <xsl:template match="/receipt" name="receipt" >
        <xsl:variable name="lang" select="language"/>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">title</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>&#10;</xsl:text>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">receipt_id</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>: </xsl:text>
        <xsl:value-of select="transaction"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">subject_id</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>: </xsl:text>
        <xsl:value-of select="subject"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">language</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>: </xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">language_<xsl:value-of select="$lang"/></xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">collection_method</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>: </xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key"><xsl:value-of select="collectionMethod"/></xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>&#10;</xsl:text>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">date</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>: </xsl:text><xsl:value-of select="date"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">expires</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>: </xsl:text><xsl:value-of select="expirationDate"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="translate">
            <xsl:with-param name="key">expires_explanation</xsl:with-param>
            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
        </xsl:call-template>
        <xsl:text>&#10;</xsl:text>
        <xsl:text>&#10;</xsl:text>
        <xsl:for-each select="consents/consent">
            <xsl:call-template name="translate">
                <xsl:with-param name="key">purpose</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="current()/title"/>
            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key">data_collected</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="current()/data"/>
            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key">data_retention</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="current()/retentionLabel"/><xsl:text> </xsl:text>
            <xsl:value-of select="current()/retentionValue"/><xsl:text> </xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key"><xsl:value-of select="current()/retentionUnit"/></xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template><xsl:text>.</xsl:text>
            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key">data_usage</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="current()/usage"/>
            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key">data_purpose</xsl:with-param>
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
            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key">subject_consent</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key"><xsl:value-of select="current()/value"/></xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>&#10;</xsl:text>
            <xsl:text>&#10;</xsl:text>
        </xsl:for-each>
        <xsl:text>&#10;</xsl:text>
        <xsl:if test="dataController and dataController/company">
            <xsl:call-template name="translate">
                <xsl:with-param name="key">data_controller_name</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="dataController/company"/>
        </xsl:if>
        <xsl:if test="privacyPolicyUrl">
            <xsl:text>&#10;</xsl:text>
            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key">privacy_policy</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="privacyPolicyUrl"/>
        </xsl:if>
        <xsl:if test="updateUrl">
            <xsl:text>&#10;</xsl:text>
            <xsl:text>&#10;</xsl:text>
            <xsl:call-template name="translate">
                <xsl:with-param name="key">update_url</xsl:with-param>
                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
            </xsl:call-template>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="updateUrl"/>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
