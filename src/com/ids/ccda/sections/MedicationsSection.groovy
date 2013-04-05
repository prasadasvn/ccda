package com.ids.ccda.sections

import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
class MedicationsSection {
    def map
    MarkupBuilder builder
    def medications = [:]
    def ATTRS = ["uid", "consumableUid", "code", "name", "dosageQuantity", "dosageUnit", "startDate", "stopDate", "instructions"  ]

    MedicationsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.medications = this.map.medications
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(root:"2.16.840.1.113883.10.20.22.2.1")
              templateId(root:"2.16.840.1.113883.10.20.22.2.1.1")
              code(code:"10160-0",
                   codeSystem:"2.16.840.1.113883.6.1",
                   codeSystemName:"LOINC",
                   displayName:"HISTORY OF MEDICATION USE"
              )
              title("Medications")
              generateNormativeText()
              medications.each { medication ->
                generateEntry(medication)
              }

          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            table(border:"1", width:"100%"){
                thead(){
                    tr(){
                        th("Drug Name")
                        th("RxNorm Code")
                        th("Start Date")
                        th("End Date")
                        th("Instructions")
                    }
                }
                medications.each{ medication ->
                    tr(){
                        td(){
                            content(ID:"medication-${medication.uid}"){medication.name}
                        }
                        td(medication.code)
                        td(medication.startDate)
                        td(medication.stopDate)
                        td(medication.instructions)
                    }
                }
            }
        }
    }

    def generateEntry( medication = [:]){
      builder.entry(){
          // MEDICATION ACTIVITY TEMPLATE
          substanceAdministration(classCode:"SBADM", moodCode:"EVN"){
              templateId(root:"2.16.840.1.113883.10.20.22.4.16")
              id(root: medication.uid) //dynamic
              text(){
                  reference(value:"medication-${medication.uid}"){ medication.name} //dynamic
              }
              statusCode(code:"completed") //TODO:research status code possible values
              effectiveTime("xsi:type":"IVL_TS"){
                  low(value: medication.startDate) //example shows YYYYMMDD //dynamic
                  high(value: medication.endDate) //example shows YYYYMMDD //dynamic
              }
              //if we can figure out period interval from dr. first, should-level
              /* effectiveTime("xsi:type":"PIVL_TS",
                              istitutionSpecified:"true",
                              operator:"A"){
                   period( value:"numeric time", unit:"unit of time abbreviation")//dynamic
               }  */
              //if we can map routes to the NCI thesaurus values, may-level
              /* routeCode( code:"C00000",
                          codeSystem:"2.16.840.1.113883.3.88.12.3221.8.7",
                          codeSystemName:"NCI Thesaurus",
                          displayName:"CONCEPT NAME"
               )   */
              doseQuantity( value:medication.dosageQuantity, unit:medication.dosageUnit)  //dynamic
              consumable(){     //MEDICATION INFORMATION TEMPLATE
                  manufacturedProduct(classCode:"MANU"){
                      templateId(root:"2.16.840.1.113883.10.20.22.4.23")
                      id(root: medication.consumableUid)//dynamic
                      manufacturedMaterial(){
                          code(cdoe:medication.code,     //RX Norm I believe  //dynamic
                                  codeSystem:"2.16.840.1.113883.6.88",
                                  displayName: medication.name){
                              originalText(){
                                  reference(value:"medication-${medication.uuid}") //dynamic
                              }
                          }
                      }
                  }
              }
              //INSTRUCTIONS
              entryRelationship(typeCode:"SUBJ", inversionInd:"true"){
                  act(classCode:"ACT", moodCode:"INT"){
                      templateId(root:"2.16.840.1.113883.10.20.22.4.20")
                      code("xsi:type":"CE",
                              code: "311401005",
                              codeSystem:"2.16.840.1.113883.11.20.9.34",
                              codeSystemName:"Patient Education")
                      text(){
                          reference(value:"instructions-${medication.uid}")
                          medication.instructions
                      }
                      statusCode(code:"completed")
                  }
              }

          }
      }
    }
}
