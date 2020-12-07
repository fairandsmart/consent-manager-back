<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exslt="http://exslt.org/common" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
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
                <value language="default">Date</value>
                <value language="en">Date</value>
                <value language="fr">Date</value>
            </key>
            <key name="expires">
                <value language="default">Expires</value>
                <value language="en">Expires</value>
                <value language="fr">Expire le</value>
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
                <value language="default">Receipt Identifier</value>
                <value language="en">Receipt Identifier</value>
                <value language="fr">Identifiant du reçu</value>
            </key>
            <key name="data_collected">
                <value language="default">Data collected</value>
                <value language="en">Data collected</value>
                <value language="fr">Données collectées</value>
            </key>
            <key name="data_retention">
                <value language="default">Retention Period</value>
                <value language="en">Retention Period</value>
                <value language="fr">Durée de conservation</value>
            </key>
            <key name="data_usage">
                <value language="default">Data Usage</value>
                <value language="en">Data Usage</value>
                <value language="fr">Utilisation</value>
            </key>
            <key name="data_purpose">
                <value language="default">Data Purpose</value>
                <value language="en">Data Purpose</value>
                <value language="fr">Finalité</value>
            </key>
            <key name="subject_consent">
                <value language="default">Subject Consent</value>
                <value language="en">Subject Consent</value>
                <value language="fr">Consentement</value>
            </key>
            <key name="subject_id">
                <value language="default">Subject Id</value>
                <value language="en">Subject Id</value>
                <value language="fr">Identifiant utilisateur</value>
            </key>
            <key name="issuer_id">
                <value language="default">Data Processor</value>
                <value language="en">Data Processor</value>
                <value language="fr">Gestionnaire des données</value>
            </key>
            <key name="data_controller_name">
                <value language="default">Controller Name</value>
                <value language="en">Controller Name</value>
                <value language="fr">Responsable de traitement</value>
            </key>
            <key name="data_controller_details">
                <value language="default">Controller Details</value>
                <value language="en">Controller Details</value>
                <value language="fr">Informations sur le responsable de traitement</value>
            </key>
            <key name="privacy_policy">
                <value language="default">Privacy Policy</value>
                <value language="en">Privacy Policy</value>
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
            <key name="update_url">
                <value language="default">Update Consent Link</value>
                <value language="en">Update Consent Link</value>
                <value language="fr">Lien de modification</value>
            </key>
            <key name="update_url_link">
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
                <value language="default">Contains sensitive data</value>
                <value language="en">Contains sensitive data</value>
                <value language="fr">Contient des données sensibles</value>
            </key>
            <key name="contains_medical_data">
                <value language="default">Contains medical data</value>
                <value language="en">Contains medical data</value>
                <value language="fr">Contient des données médicales</value>
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
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="receipt" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="1cm" margin-left="2.5cm" margin-right="2.5cm">
                    <fo:region-body margin-top="1cm"/>
                    <fo:region-before extent="3cm"/>
                    <fo:region-after extent="1.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="receipt">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="20pt" font-weight="bold" text-align="center">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">title</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                        </xsl:call-template>
                    </fo:block>
                    <fo:block background-color="#f5f5f5" margin="20pt 0 10pt 0" padding="10pt">
                        <fo:block font-size="10pt" margin-top="10pt">
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">subject_id</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </fo:inline>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="subject"/>
                        </fo:block>
                        <fo:block font-size="10pt">
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">language</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </fo:inline>
                            <xsl:text>: </xsl:text>
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">language_<xsl:value-of select="$lang"/></xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                            </xsl:call-template>
                        </fo:block>
                        <fo:block font-size="10pt">
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">collection_method</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </fo:inline>
                            <xsl:text>: </xsl:text>
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">collection_method_<xsl:value-of select="collectionMethod"/></xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                            </xsl:call-template>
                        </fo:block>
                        <fo:block font-size="10pt">
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">date</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </fo:inline>
                            <xsl:text>: </xsl:text>
                            <xsl:call-template name="formatdate">
                                <xsl:with-param name="DateTimeStr" select="date"/>
                            </xsl:call-template>
                        </fo:block>
                        <fo:block font-size="10pt">
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">expires</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </fo:inline>
                            <xsl:text>: </xsl:text>
                            <xsl:call-template name="formatdate">
                                <xsl:with-param name="DateTimeStr" select="expirationDate"/>
                            </xsl:call-template>
                        </fo:block>
                        <fo:block font-size="10pt">
                            <fo:inline font-weight="bold">
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">receipt_id</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </fo:inline>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="transaction"/>
                        </fo:block>
                    </fo:block>


                    <xsl:if test="updateUrl">
                        <fo:block font-size="10pt" margin-top="5pt" text-align="center">
                            <fo:basic-link color="grey" text-decoration="underline">
                                <xsl:attribute name="external-destination"><xsl:value-of select="updateUrl"/></xsl:attribute>
                                <xsl:call-template name="translate">
                                    <xsl:with-param name="key">update_url_link</xsl:with-param>
                                    <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                </xsl:call-template>
                            </fo:basic-link>
                        </fo:block>
                    </xsl:if>

                    <xsl:for-each select="consents/consent">
                        <fo:table table-layout="fixed" width="100%" margin="10pt 0">
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell column-number="1">
                                        <fo:block font-size="14pt" font-weight="bold">
                                            <xsl:value-of select="current()/title"/>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell column-number="2" text-align="right" letter-spacing=".5pt" text-transform="uppercase" font-weight="300">
                                        <fo:block>
                                            <xsl:call-template name="translate">
                                                <xsl:with-param name="key"><xsl:value-of select="current()/value"/></xsl:with-param>
                                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                            </xsl:call-template>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                        <fo:block color="#3F3F3F" font-size="10pt" margin-bottom="5pt"><xsl:value-of select="current()/data"/></fo:block>
                        <fo:block color="#3F3F3F" font-size="10pt" margin-bottom="5pt"><xsl:value-of select="current()/retention"/></fo:block>
                        <fo:block color="#3F3F3F" font-size="10pt" margin-bottom="5pt"><xsl:value-of select="current()/usage"/></fo:block>

                        <xsl:for-each select="current()/purposes/purpose">
                            <fo:block>
                                <fo:external-graphic>
                                    <xsl:attribute name="src">
                                        <xsl:text>url('assets/img/purpose/</xsl:text>
                                        <xsl:value-of select="current()" />
                                        <xsl:text>.png')</xsl:text>
                                    </xsl:attribute>
                                </fo:external-graphic>
                            </fo:block>
                        </xsl:for-each>

                        <xsl:if test="current()/containsSensitiveData = 'true' or current()/containsMedicalData = 'true'">
                            <fo:block background-color="#f5f5f5" padding="10pt" margin="0 0 10pt 0" font-size="10pt">
                                <fo:block font-size="11pt" font-weight="bold" margin-bottom="3pt">
                                    <xsl:call-template name="translate">
                                        <xsl:with-param name="key">sensitive_data</xsl:with-param>
                                        <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                    </xsl:call-template>
                                </fo:block>
                                <xsl:if test="current()/containsSensitiveData = 'true'">
                                    <fo:block color="#4F4F4F">-
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">contains_sensitive_data</xsl:with-param>
                                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                    </fo:block>
                                </xsl:if>
                                <xsl:if test="current()/containsMedicalData = 'true'">
                                    <fo:block color="#4F4F4F">-
                                        <xsl:call-template name="translate">
                                            <xsl:with-param name="key">contains_medical_data</xsl:with-param>
                                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                        </xsl:call-template>
                                    </fo:block>
                                </xsl:if>
                            </fo:block>
                        </xsl:if>

                        <xsl:if test="current()/thirdParties">
                            <fo:block background-color="#f5f5f5" padding="10pt" margin="0 0 10pt 0" font-size="10pt">
                                <fo:block font-weight="bold" font-size="11pt" margin-bottom="3pt">
                                    <xsl:call-template name="translate">
                                        <xsl:with-param name="key">third_parties</xsl:with-param>
                                        <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                                    </xsl:call-template>
                                </fo:block>
                                <xsl:for-each select="current()/thirdParties/thirdParty">
                                    <fo:block color="#4F4F4F">-
                                        <fo:inline font-weight="bold"><xsl:value-of select="current()/name" /></fo:inline> :
                                        <xsl:value-of select="current()/value" />
                                    </fo:block>
                                </xsl:for-each>
                            </fo:block>
                        </xsl:if>
                        <fo:block width="100%" border-bottom="1px solid #CCC" margin-bottom="5pt"/>
                    </xsl:for-each>

                    <!--
                    <xsl:if test="updateUrlQrCode">
                        <fo:block margin-top="10pt">
                            <fo:external-graphic>
                                <xsl:attribute name="src">
                                    <xsl:text>url('</xsl:text>
                                    <xsl:value-of select="updateUrlQrCode"/>
                                    <xsl:text>')</xsl:text>
                                </xsl:attribute>
                            </fo:external-graphic>
                        </fo:block>
                    </xsl:if>
                    -->

                    <fo:block font-size="10pt">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">issuer_id</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                        <xsl:value-of select="processor"/>
                    </fo:block>
                    <xsl:if test="dataController and dataController/name and dataController/company">
                        <fo:block font-size="10pt" margin-top="10pt">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_controller_name</xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="dataController/name"/>
                        </fo:block>
                        <fo:block font-size="10pt">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_controller_details</xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="dataController/company"/><xsl:text>: </xsl:text>
                            <xsl:value-of select="dataController/address"/><xsl:text>: </xsl:text>
                            <xsl:value-of select="dataController/email"/><xsl:text>: </xsl:text>
                            <xsl:value-of select="dataController/phoneNumber"/>
                        </fo:block>
                    </xsl:if>
                    <xsl:if test="privacyPolicyUrl">
                        <fo:block font-size="10pt" margin-top="10pt">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">privacy_policy</xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="$lang"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                            <xsl:value-of select="privacyPolicyUrl"/>
                        </fo:block>
                    </xsl:if>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>