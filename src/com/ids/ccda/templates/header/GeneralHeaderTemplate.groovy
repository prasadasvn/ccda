package com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID
import com.ids.ccda.templates.sections.body.BodySectionTemplate
import groovy.xml.MarkupBuilder

class GeneralHeaderTemplate  implements HeaderTemplate {
    public static final CLINIC_SUMMARY = [code: "34133-9",  displayName: "Summarization of Episode Note"]
    public static final TEST_MAP = [ tenantId: "12345", patientId:"678912345", title: "A Clinical Summary From A Practice"]
    def ATTRS = [ "tenantId", "patientId", "title" ]
    DocUid docUid
    MarkupBuilder builder
    Map map

    GeneralHeaderTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        generate()
    }

    def generate(){
        builder.realmCode(code:"US")
        builder.typeId(HL7_OID.US_REALM_TYPE_ID)
        builder.templateId(HL7_OID.US_REALM_HEADER_TEMPLATE_ID)
        builder.templateId(HL7_OID.CCD_TEMPLATE_ID)
        builder.id(root: IDS_OID.IDS_ROOT,
                assigningAuthorityName:"Integrity EMR",
                extension: docUid,
                displayable: "true" )
        builder.code( CLINIC_SUMMARY + HL7_OID.LOINC )
        builder.title( map.title) //summary specific, practice specific
        builder.effectiveTime(generateEffectiveDate())  //time created
        builder.confidentialityCode( HL7_OID.DOCUMENT_CONFIDENTIALITY)
        builder.languageCode(code:"en-US")
    }

    private def generateEffectiveDate(){
        def date = map.effectiveDate ?: new Date()
        date.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED)
    }


}
