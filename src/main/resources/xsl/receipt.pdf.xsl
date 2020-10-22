<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:param name="bundle"  select="document('./bundle.xml')"/>
    <xsl:template name="translate">
        <xsl:param name="key"></xsl:param>
        <xsl:param name="locale"></xsl:param>
        <xsl:choose>
            <xsl:when test="$bundle/labels/key[@name = $key]">
                <xsl:choose>
                    <xsl:when test="$bundle/labels/key[@name = $key]/value[@locale = $locale]">
                        <xsl:value-of select="$bundle/labels/key[@name = $key]/value[@locale = $locale]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$bundle/labels/key[@name = $key]/value[@locale = 'default']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:choose>
                    <xsl:when test="$bundle/labels/key[@name = 'not-found']/value[@locale = $locale]">
                        <xsl:value-of select="$bundle/labels/key[@name = 'not-found']/value[@locale = $locale]"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$bundle/labels/key[@name = 'not-found']/value[@locale = 'default']"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="/receipt">
        <xsl:attribute name="lang">
            <xsl:value-of select="locale/language"/>
        </xsl:attribute>
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="receipt"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin-top="2cm"
                                       margin-bottom="1cm"
                                       margin-left="2.5cm"
                                       margin-right="2.5cm">
                    <fo:region-body margin-top="1cm"/>
                    <fo:region-before extent="3cm"/>
                    <fo:region-after extent="1.5cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="receipt">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block-container position="absolute"
                                        border-color="#880000" border-style="solid" border-width=".3mm"
                                        top="-0.5cm" left="9cm" height="2cm" width="7.5cm" >
                    </fo:block-container>
                    <fo:block font-size="30pt"
                              space-after.optimum="200pt"
                              text-align="end">
                    </fo:block>
                    <fo:block>
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">title</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
                        </xsl:call-template>
                    </fo:block>
                    <fo:block>
                        <xsl:call-template name="translate">
                            <xsl:with-param name="key">date</xsl:with-param>
                            <xsl:with-param name="locale"><xsl:value-of select="locale"/></xsl:with-param>
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