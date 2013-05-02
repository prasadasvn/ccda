package com.ids.ccda.sections

import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder


class ProceduresSection {
    public static final TITLE = "Procedures"
    public static final SECTION_CODE = [code:"47519-4", displayName:"HISTORY OF PROCEDURES"] + HL7_OID.LOINC
    public static final ALLERGY_INTOLERANCE_CODE = [code:"ASSERTION",codeSystem:"2.16.840.1.113883.5.4"]

    def map
    MarkupBuilder builder
    def procedures = [:]
    def ATTRS = ["uid",  "code", "name", "date", "bodySiteCode", "bodySiteName", "performer"  ]

    ProceduresSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.procedures = this.map.procedures
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(HL7_OID.PROCEDURES_SECTION_TEMPLATE_ID)
              code( SECTION_CODE )
              title( TITLE )
              generateNormativeText()
              procedures.each { procedure ->
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
                procedures.each{ procedure ->
                    tr(){
                        td(){ content(ID:"procedure-${procedure.uid}"){procedure.name}  }
                        td(procedure.date)
                    }
                }
            }
        }
    }

    def generateEntry( pro = [:]){
      builder.entry( typeCode:"DRIV"){
        //PROCEDURE ACTIVITY PROCEDURE
          procedure(classCode:"PROC", moodCode:"EVN"){
              templateId(HL7_OID.PROCEDURE_ACTIVITY_PROCEDURE_TEMPLATE_ID)
              id(root: pro.uid)    //dynamic
              code([code: pro.code, displayName: pro.name ] + HL7_OID.SNOMED ){ //dynamic
                   originalText(){ reference(value:"#procedure-${pro.uid}")  }
              }
              statusCode(code:"completed")//since this is a history, it would be completed, but could contain aborted,active,cancelled, or completed
              effectiveTime(value:pro.date)
              //targetSiteCode is should not shall, and I do not believe we have a way of getting this information for procedures
              /*targetSiteCode(code: pro.bodySiteCode,
                             displayName: pro.bodySiteName,
                             codeSystem:"2.16.840.1.113883.3.88.12.3221.8.9",
                             codeSystemName:"Body Site Value Set") */
              performer(){
                  assignedEntity(){
                      id(root:HL7_OID.NPI, pro.performer.npi)  //dynamic
                      addr(use:"PUB" ){
                          streetAddressLine(pro.performer.addressLine1)   //dynamic
                          streetAddressLine(pro.performer.addressLine2)  //dynamic
                          city(pro.performer.city)     //dynamic
                          state(pro.performer.state)    //dynamic
                          postalCode(pro.performer.zipCode)  //dynamic
                          country("US")
                      }
                      telecom( value: "tel:${pro.performer.phone}", use: "WP"  )   //dynamic
                  }
              }

          }

      }
    }
}
