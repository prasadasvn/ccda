package com.ids.ccda.sections

import com.ids.ccda.OIDS
import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
class ProceduresSection {
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
              templateId(root:"2.16.840.1.113883.10.20.22.2.7")
              templateId(root:"2.16.840.1.113883.10.20.22.2.7.1")
              code(code:"47519-4",
                   codeSystem:"2.16.840.1.113883.6.1",
                   codeSystemName:"LOINC",
                   displayName:"HISTORY OF PROCEDURES"
              )
              title("Procedures")
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
                        td(){
                            content(ID:"procedure-${procedure.uid}"){procedure.name}
                        }
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
              templateId(root:"2.16.840.1.113883.10.20.22.4.14")
              id(root: pro.uid)    //dynamic
              code(code: pro.code,  //dynamic
                   displayName: pro.name, //dynamic
                   codeSystem: "2.16.840.1.113883.6.96",
                   codeSystemName:"SNOMED CT"){
                   originalText(){
                       reference(value:"#procedure-${pro.uid}")
                   }
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
                      id(root:OIDS.NPI, pro.performer.npi)  //dynamic
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
