<xsl:stylesheet version="2.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:x="http://test.x.com/schema/x/v1"
                xmlns:a="http://test.a.com/schema/a/v1"
                xmlns:b="http://test.b.com/schema/b/v2"                
        >
    <xsl:param name="testParam"/>
    
    <xsl:template match="/eksporterFravaer">
        <a:ABWAbsenceRecord>

            <b:BatchId>GAT</b:BatchId>
            <b:ReportClient><xsl:value-of select="x:Fravaer/x:FirmaKode"/></b:ReportClient>
            
            <xsl:for-each select="x:Fravaer">
                <a:AbsenceRecord>
                    <a:ResourceId><xsl:value-of select="x:RessursId"/></a:ResourceId>
                    <a:Position><xsl:value-of select="x:StillingsId"/></a:Position>
                    <a:DateFrom><xsl:value-of select="substring(x:FraDatoTid, 0, 11)"/></a:DateFrom>
                    <a:DateTo><xsl:value-of select="substring(x:TilDatoTid, 0, 11)"/></a:DateTo>
                    <a:AbsenceCode><xsl:value-of select="x:FravaerKode"/></a:AbsenceCode>
                    <a:AbsenceReason/>
                    <a:AbsSeq>0</a:AbsSeq>
                    <a:CalendarFlag>0</a:CalendarFlag>
                    <a:DayType/>
                    <a:Description><xsl:value-of select="$testParam"/></a:Description>
                    <a:Dim1><xsl:value-of select="x:KostnadsSted"/></a:Dim1>
                    <a:EndDate><xsl:value-of select="current-date()"/></a:EndDate>
                    <a:Percentage><xsl:value-of select="x:Prosent"/></a:Percentage>
                    <a:RecordType/>
                    <a:RefNo1><xsl:value-of select="x:BilagsId"/></a:RefNo1>
                    <a:Reasoncode/>
                    <a:SequenceNo>0</a:SequenceNo>
                    <a:Status>N</a:Status>
                    <a:TimeFrom><xsl:value-of select="number(concat(hours-from-dateTime(x:FraDatoTid), format-number(minutes-from-dateTime(x:FraDatoTid), '00')))"/></a:TimeFrom>
                    <a:TimeTo><xsl:value-of select="number(concat(hours-from-dateTime(x:TilDatoTid), format-number(minutes-from-dateTime(x:TilDatoTid), '00')))"/></a:TimeTo>
                    <a:TotalHrs><xsl:value-of select="x:AntallTimer"/></a:TotalHrs>
                    <a:TransDate><xsl:value-of select="x:PosteringsDato"/></a:TransDate>
                </a:AbsenceRecord>
            </xsl:for-each>

        </a:ABWAbsenceRecord>
    </xsl:template>
</xsl:stylesheet>