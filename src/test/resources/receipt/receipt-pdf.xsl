<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
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
    <xsl:template match="/receipt">
        <xsl:attribute name="lang">
            <xsl:value-of select="language"/>
        </xsl:attribute>
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="receipt">
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="receipt">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">title</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                    </fo:block>
                    <fo:block>
                       <xsl:call-template name="translate">
                            <xsl:with-param name="key">date</xsl:with-param>
                            <xsl:with-param name="language"><xsl:value-of select="language"/></xsl:with-param>
                        </xsl:call-template>
                        <xsl:text>: </xsl:text>
                    </fo:block>
                    <fo:block>
                        <xsl:value-of select="date"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>