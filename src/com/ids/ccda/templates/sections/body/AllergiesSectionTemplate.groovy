package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder
@Mixin(Comment)
class AllergiesSectionTemplate  implements BodySectionTemplate {
    //TODO: make methods for effective time, statuses, and mapping of reaction
    public static final TITLE = "ALLERGIES, ADVERSE REACTIONS, ALERTS"
    public static final MAP_KEY = "allergies"
    public static final SECTION_CODE = [code:"48765-2", displayName:"Allergies, adverse reactions, alerts"] + HL7_OID.LOINC
    public static final ALLERGY_INTOLERANCE_CODE = [code:"ASSERTION",codeSystem:"2.16.840.1.113883.5.4"]

    def ATTRS = [ "statusCode", "effectiveTimeLow", "effectiveTimeHigh", "reactionCode", "reactionName", "drugCode", "drugName" ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def allergies = [:]

    AllergiesSectionTemplate( Document doc ) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.allergies = this.map.allergies ?: [:]
        generate()
    }

    def generate(){
      builder.mkp.comment(comment())
      builder.component(){
          section(){
              templateId(HL7_OID.ALLERGIES_SECTION_TEMPLATE_ID)
              code( SECTION_CODE )
              title(TITLE)
              generateNormativeText()
              allergies.each { id,allergy ->
                generateEntry(id, allergy)
              }
          }
      }
    }

    def generateNormativeText(){
      builder.text(){
          table(border:"1", width:"100%"){
              thead(){
                  tr(){
                      th("Substance")
                      th("Reaction")
                      th("Status")
                  }
              }
              allergies.each{ id,allergy ->
                  def uid = docUid.secId(MAP_KEY,id)
                  tr{
                      td(allergy.drugName)
                      td{ content(ID:"reaction-${uid}",allergy.reactionName ) }
                      td(allergy.statusCode)
                  }
              }

          }
      }
    }

    def generateEntry(allergyId,allergy = [:]){
        def uid = docUid.secId(MAP_KEY,allergyId)
        builder.entry(typeCode:"DRIV"){

            act(classCode:"ACT", moodCode: "EVN"){
                //templateId(HL7_OID.ALLERGY_PROBLEM_ACT_TEMPLATE_ID)
                id(root:uid)    //dyanmic
                code( SECTION_CODE + HL7_OID.LOINC  )
                statusCode(code: allergy.statusCode) // from active, suspended, aborted, completed  //dyanmic
                effectiveTime(low: allergy.effectiveTimeLow, high: allergy.effectiveTimeHigh) //if statusCode is active, then use low only, if completed, then high
                entityRelationship( typeCode:"SUBJ"){
                    observeration(classCode:"OBS", moodCode:"EVN"){
                        templateId(HL7_OID.ALLERGY_INTOLERANCE_OBSERVATION_TEMPLATE_ID)
                        id(root:UUID.randomUUID())
                        code(ALLERGY_INTOLERANCE_CODE)
                        statusCode(code:"completed") //static
                        effectiveTime(low: allergy.effectiveTimeLow, high: allergy.effectiveTimeHigh) // if beginning is unknown (low), then null flavor UNK; if no longer a concern, may contain high //dyanmic
                        //VALUE - dyanmic, mapped from rcopia to either drug allergy or drug intolerance; mapped from Rcopia reactions to list of SNOMED values
                        value( ["xsi:type":"CD", code: allergy.reactionCode] + HL7_OID.SNOMED){
                            //originalText(){ reference(value:"reaction-${uid}")}  //using the approach defined in CDA Release 2, section 4.3.5.1
                        }
                        participant(typeCode:"CSM"){
                            participantRole(classCode:"MANU"){
                                playingEntity(classCode:"MMAT"){
                                    code([code: allergy.drugCode, displayName:allergy.drugName] + HL7_OID.RX_NORM ){
                                        //originalText(){ reference(value:"reaction-${uid}") }
                                    }
                                }
                            }
                        }

                    }

                }

            }
        }
    }

}
