package com.ids.ccda.sections

import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
class MedicationsSection {
    public static final TITLE = "Medications"
    public static final SECTION_CODE = [code:"10160-0", displayName:"HISTORY OF MEDICATION USE"] + HL7_OID.LOINC


    def map
    MarkupBuilder builder
    def medications = [:]
    def ATTRS = ["uid", "code", "name", "dosageQuantity", "dosageUnit", "startDate", "stopDate", "instructions"  ]

    MedicationsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.medications = this.map.medications
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(HL7_OID.MEDICATIONS_SECTION_TEMPLATE_ID)
              code(SECTION_CODE)
              title(TITLE)
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
                        td(){content(ID:"medication-${medication.uid}"){medication.name} }
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
              templateId(HL7_OID.MEDICATIONS_ACTIVITY_TEMPLATE_ID)
              id(root: medication.uid) //dynamic
              text(){ reference(value:"medication-${medication.uid}"){ medication.name} }//dynamic
              statusCode(code:"completed") //status is in actuality communicated by the effectiveTime
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
                      templateId(HL7_OID.MEDICATION_INFORMATION_TEMPLATE_ID)
                      id(root: UUID.randomUUID())
                      manufacturedMaterial(){
                          code([code:medication.code,  displayName: medication.name] + HL7_OID.RX_NORM){ //dynamic
                              originalText(){ reference(value:"medication-${medication.uuid}") }//dynamic
                          }
                      }
                  }
              }
              //INSTRUCTIONS
              entryRelationship(typeCode:"SUBJ", inversionInd:"true"){
                  act(classCode:"ACT", moodCode:"INT"){
                      templateId(HL7_OID.INSTRUCTIONS_TEMPLATE_ID)
                      code(["xsi:type":"CE", code: "311401005"] + HL7_OID.PATIENT_EDUCATION_VALUE_SET)
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
