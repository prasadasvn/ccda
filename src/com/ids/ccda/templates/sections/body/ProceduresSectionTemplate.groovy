package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.mixins.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

@Mixin(Comment)
class ProceduresSectionTemplate implements BodySectionTemplate{
    public static final TITLE = "Procedures"
    public static final MAP_KEY = "procedures"
    public static final SECTION_CODE = [code:"47519-4", displayName:"HISTORY OF PROCEDURES"] + HL7_OID.LOINC

    def ATTRS = [ "code", "name", "date", "bodySiteCode", "bodySiteName", "performer"  ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def procedures = [:]

    ProceduresSectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.procedures = map.procedures ?: [:]
        generate()
    }

    def generate(){
        builder.mkp.comment(comment())
        builder.component(){
          section(){
              templateId(HL7_OID.PROCEDURES_SECTION_TEMPLATE_ID)
              code( SECTION_CODE )
              title( TITLE )
              generateNormativeText()
              procedures.each { id, procedure ->
                generateEntry(procedure)
              }
          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            table(border:"1", width:"100%"){
                thead(){
                    tr(){
                        th("Procedure")
                        th("Date")
                    }
                }
                procedures.each{ id, procedure ->
                    def uid = docUid.secId(MAP_KEY,id)
                    tr{
                        td{ content(ID:"procedure-${uid}",procedure.name)  }
                        td(procedure.date)
                    }
                }
            }
        }
    }

    def generateEntry( procedureId, pro = [:]){
      def uid = docUid.secId(MAP_KEY,procedureId)
      builder.entry( typeCode:"DRIV"){
        //PROCEDURE ACTIVITY PROCEDURE
          procedure(classCode:"PROC", moodCode:"EVN"){
              templateId(HL7_OID.PROCEDURE_ACTIVITY_PROCEDURE_TEMPLATE_ID)
              id(root: uid)    //dynamic
              code([code: pro.code, displayName: pro.name ] + HL7_OID.SNOMED ){ //dynamic
                   //originalText(){ reference(value:"#procedure-${uid}")  }
              }
              statusCode(code:"completed")//since this is a history, it would be completed, but could contain aborted,active,cancelled, or completed
              effectiveTime(value:pro.date)
              //targetSiteCode is should not shall, and I do not believe we have a way of getting this information for procedures
              targetSiteCode(code: pro.bodySiteCode,
                             displayName: pro.bodySiteName,
                             codeSystem:"2.16.840.1.113883.3.88.12.3221.8.9",
                             codeSystemName:"Body Site Value Set")
              performer(){
                  assignedEntity(){
                      id(root:HL7_OID.NPI, pro.performer?.npi)  //dynamic
                      addr(use:"PUB" ){
                          streetAddressLine(pro.performer?.addressLine1)   //dynamic
                          streetAddressLine(pro.performer?.addressLine2)  //dynamic
                          city(pro.performer?.city)     //dynamic
                          state(pro.performer?.state)    //dynamic
                          postalCode(pro.performer?.zipCode)  //dynamic
                          country("US")
                      }
                      telecom( value: "tel:${pro.performer?.phone}", use: "WP"  )   //dynamic
                  }
              }

          }

      }
    }
}
