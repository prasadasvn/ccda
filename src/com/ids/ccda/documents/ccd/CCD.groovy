package com.ids.ccda.documents.ccd

import com.ids.ccda.Document
import com.ids.ccda.Generatable
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID
import com.ids.ccda.templates.header.AuthorTemplate
import com.ids.ccda.templates.header.CustodianTemplate
import com.ids.ccda.templates.header.GeneralHeaderTemplate
import com.ids.ccda.templates.header.ParticipantsTemplate
import com.ids.ccda.templates.header.RecordTargetTemplate
import com.ids.ccda.templates.header.ServiceEventTemplate
import com.ids.ccda.templates.sections.body.AllergiesSectionTemplate
import com.ids.ccda.templates.sections.body.ImmunizationsSectionTemplate
import com.ids.ccda.templates.sections.body.MedicationsAdministeredSectionTemplate
import com.ids.ccda.templates.sections.body.MedicationsSectionTemplate
import com.ids.ccda.templates.sections.body.PlanOfCareSectionTemplate
import com.ids.ccda.templates.sections.body.ProblemsSectionTemplate
import com.ids.ccda.templates.sections.body.ProceduresSectionTemplate
import com.ids.ccda.templates.sections.body.ResultsSectionTemplate
import com.ids.ccda.templates.sections.body.SocialHistorySectionTemplate
import com.ids.ccda.templates.sections.body.VitalSignsSectionTemplate
import groovy.xml.MarkupBuilder

class CCD implements Generatable{

    def static final HEADER_COMMENT = """
     ****************************************
       CDA Header
     ****************************************
    """
    def static final BODY_COMMENT = """
     ****************************************
       CDA Body
     ****************************************
    """

    Document document
    DocUid docUid
    MarkupBuilder builder
    Map map

    CCD(tenantId, patientId, map = [:] ) {
     docUid = new DocUid(clinicSummaryId(tenantId, patientId))
     document = new Document(docUid, map)
     this.map = map
     this.builder = document.builder
     generate()
    }

    def generate(){
     builder.ClinicalDocument(  "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
                                "xsi:schemaLocation":"urn:hl7-org:v3 ../../../CDA%20R2/cda-schemas-and-samples/infrastructure/cda/CDA.xsd",
                                "xmlns":"urn:hl7-org:v3",
                                "xmlns:cda" : "urn:hl7-org:v3",
                                "xmlns:sdtc" : "urn:hl7-org:sdtc"
     )   {
           mkp.comment(HEADER_COMMENT)
           new GeneralHeaderTemplate(document).builder
           new RecordTargetTemplate(document).builder
           new AuthorTemplate(document).builder
           new CustodianTemplate(document).builder
           new ParticipantsTemplate(document).builder
           new ServiceEventTemplate(document).builder
           mkp.comment(BODY_COMMENT)
           component{
               structuredBody{
                  new AllergiesSectionTemplate(document).builder
                  new ImmunizationsSectionTemplate(document).builder
                  new MedicationsSectionTemplate(document).builder
                  new MedicationsAdministeredSectionTemplate(document).builder
                  new PlanOfCareSectionTemplate(document).builder
                  new ProblemsSectionTemplate(document).builder
                  new ProceduresSectionTemplate(document).builder
                  new ResultsSectionTemplate(document).builder
                  new SocialHistorySectionTemplate(document).builder
                  new VitalSignsSectionTemplate(document).builder
               }
           }
       }
    }

    private def clinicSummaryId( tenantId, patientId, Date dateGenerated = new Date(), String idsClinicalSummaryOid = IDS_OID.IDS_CLINICAL_SUMMARY){
        return "${idsClinicalSummaryOid}.${tenantId}.${patientId}.${dateGenerated.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED)}"
    }


}
