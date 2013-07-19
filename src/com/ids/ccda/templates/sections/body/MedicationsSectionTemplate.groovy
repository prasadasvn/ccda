package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

@Mixin(Comment)
class MedicationsSectionTemplate  implements BodySectionTemplate {
    public static final TITLE = "Medications"
    public static final MAP_KEY = "medications"
    public static final SECTION_CODE = [code:"10160-0", displayName:"HISTORY OF MEDICATION USE"] + HL7_OID.LOINC

    def ATTRS = ["code", "name", "routeCode", "routeName", "dosageQuantity", "dosageUnit", "startDate", "stopDate", "instructions"  ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def medications = [:]

    MedicationsSectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.medications = map.medications ?: [:]
        generate()
    }

    def generate(){
      builder.mkp.comment(comment())
      builder.component(){
          section(){
              templateId(HL7_OID.MEDICATIONS_SECTION_TEMPLATE_ID)
              code(SECTION_CODE)
              title(TITLE)
              generateNormativeText()
              medications.each { id, medication ->
                generateEntry(id, medication)
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
                medications.each{ id, medication ->
                    def uid = docUid.secId(MAP_KEY,id)
                    tr(){
                        td{content(ID:"medication-${uid}",medication.name) }
                        td(medication.code)
                        td(medication.startDate)
                        td(medication.stopDate)
                        td(medication.instructions)
                    }
                }
            }
        }
    }

    def generateEntry( medicationId,medication = [:]){
      def uid = docUid.secId(MAP_KEY,medicationId)
      builder.entry(){
          // MEDICATION ACTIVITY TEMPLATE
          substanceAdministration(classCode:"SBADM", moodCode:"EVN"){
              templateId(HL7_OID.MEDICATIONS_ACTIVITY_TEMPLATE_ID)
              id(root: uid) //dynamic
              //text(){ reference(value:"medication-${uid}"){ medication.name} }//dynamic
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
              routeCode( [code: medication.routeCode, displayName:medication.routeName] + HL7_OID.NCI_THESAURUS)
              doseQuantity( value:medication.dosageQuantity, unit:medication.dosageUnit)  //dynamic
              consumable(){     //MEDICATION INFORMATION TEMPLATE
                  manufacturedProduct(classCode:"MANU"){
                      templateId(HL7_OID.MEDICATION_INFORMATION_TEMPLATE_ID)
                      id(root: docUid.medInfoId(medicationId) )
                      manufacturedMaterial(){
                          code([code:medication.code,  displayName: medication.name] + HL7_OID.RX_NORM){ //dynamic
                              //originalText(){ reference(value:"medication-${uid}") }//dynamic
                          }
                      }
                  }
              }
              //INSTRUCTIONS
              entryRelationship(typeCode:"SUBJ", inversionInd:"true"){
                  act(classCode:"ACT", moodCode:"INT"){
                      templateId(HL7_OID.INSTRUCTIONS_TEMPLATE_ID)
                      code(["xsi:type":"CE", code: "409073007", displayName: "Education"] + HL7_OID.SNOMED)
                      text(){
                          //reference(value:"instructions-${uid}")
                          medication.instructions
                      }
                      statusCode(code:"completed")
                  }
              }

          }
      }
    }
}
