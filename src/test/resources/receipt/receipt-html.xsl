<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.01//EN" doctype-system="http://www.w3.org/TR/html4/strict.dtd" indent="yes" />

    <xsl:param name="bundle"  select="document('./bundle.xml')"/>

    <xsl:template name="translate">
        <xsl:param name="key"></xsl:param>
        <xsl:param name="language"></xsl:param>
        <xsl:choose>
            <xsl:when test="$bundle/labels/key[@name = $key]">
                <xsl:choose>
                    <xsl:when test="$bundle/labels/key[@name = $key]/value[@language = $language]">
                        <xsl:value-of select="$bundle/labels/key[@name = $key]/value[@language = $language]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$bundle/labels/key[@name = $key]/value[@language = 'default']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="$bundle/labels/key[@name = 'not-found']/value[@language = $language]">
                        <xsl:value-of select="$bundle/labels/key[@name = 'not-found']/value[@language = $language]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$bundle/labels/key[@name = 'not-found']/value[@language = 'default']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="/receipt" name="page" >
        <html>
            <xsl:attribute name="lang">
                <xsl:value-of select="language"/>
            </xsl:attribute>
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
                <div class="receipt-title spaced">CONSENT RECEIPT</div>
                <div class="receipt-date">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">date</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="date"/>
                </div>
                <div class="receipt-date spaced">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">expires</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="expirationDate"/>
                </div>
                <div>
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">language</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:call-template name="translate">
                        <xsl:with-param name="key">language_<xsl:value-of select="language"/></xsl:with-param>
                        <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                    </xsl:call-template>
                </div>
                <div class="spaced">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">receipt_id</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="transaction"/>
                </div>
                <div class="spaced">
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">collection_method</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
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
                                <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="current()/data"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_retention</xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="current()/retention"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_usage</xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="current()/usage"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_purpose</xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
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
                                <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
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
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="subject"/>
                </div>

                <div>
                    <span class="receipt-label">
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">issuer_id</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
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
                                <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                            </xsl:call-template>
                            <xsl:text>: </xsl:text>
                        </span>
                        <xsl:value-of select="dataController/name"/>
                    </div>
                    <div>
                        <span class="receipt-label">
                            <xsl:call-template name="translate">
                                <xsl:with-param name="key">data_controller_details</xsl:with-param>
                                <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
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
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </span>
                    <xsl:value-of select="privacyPolicyUrl"/>
                </div>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>