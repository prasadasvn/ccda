package com.ids.ccda.sections

import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
class AllergiesSection {

    //TODO: make methods for effective time, statuses, and mapping of reaction

    def map
    def allergies = [:]
    MarkupBuilder builder
    def ALLERGY_ATTRS = [ "uid", "observationUid", "statusCode", "effectiveTimeLow", "effectiveTimeHigh", "reactionCode", "reactionName", "drugCode", "drugName" ]

    AllergiesSection( MarkupBuilder builder, map) {
        this.map = map
        this.allergies = map.allergies
        this.builder = builder
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(root:"2.16.840.1.113883.10.20.22.2.6.1")
              code(code:"48765-2",
                   codeSystem:"2.16.840.1.113883.6.1")
              title("ALLERGIES, ADVERSE REACTIONS, ALERTS")
              generateNormativeText()
              allergies.each { allergy ->
                generateEntry(allergy)
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
              allergies.each{ allergy ->
                  tr(){
                      td(allergy.drugName)
                      td(){
                          content(ID:"reaction-${allergy.uid}"){allergy.reactionName}
                      }
                      td(allergy.statusCode)
                  }
              }



          }
      }
    }

    def generateEntry(allergy = [:]){
        builder.entry(typeCode:"DRIV"){
                  act(classCode:"ACT", moodCode: "EVN"){
                      templateId(root:"2.16.840.1.113883.10.20.22.4.30")
                      id(root:allergy.uid)//not sure what this represents     //dyanmic
                      code(code:"48765-2",
                              codeSystem:"2.16.840.1.113883.6.1",
                              codeSystemName:"LOINC",
                              displayName:"Allergies, adverse reactions, alerts")
                      statusCode(code: allergy.statusCode) // from active, suspended, aborted, completed  //dyanmic
                      effectiveTime(low: allergy.effectiveTimeLow, high: allergy.effectiveTimeHigh) //if statusCode is active, then use low only, if completed, then high
                      entityRelationship( typeCode:"SUBJ"){
                          observeration(classCode:"OBS", moodCode:"EVN"){
                              templateId(root:"2.16.840.1.113883.10.20.22.4.7")
                              id(root:allergy.observationUid)//not sure what this represents     //dyanmic
                              code(code:"ASSERTION",codeSystem:"2.16.840.1.113883.5.4")
                              statusCode(code:"completed") //static
                              effectiveTime(low: allergy.effectiveTimeLow, high: allergy.effectiveTimeHigh) // if beginning is unknown (low), then null flavor UNK; if no longer a concern, may contain high //dyanmic
                              value( "xsi:type":"CD",
                                      code: allergy.reactionCode, //dyanmic, mapped from rcopia to either drug allergy or drug intolerance
                                      codeSystem:"2.16.840.1.113883.6.96",  //mapped from Rcopia reactions to list of SNOMED values
                                      codeSystemName:"SNOMED CT"){
                                  originalText(){
                                      reference(value:"reaction-${allergy.uid}")  //using the approach defined in CDA Release 2, section 4.3.5.1
                                  }
                              }
                              participant(typeCode:"CSM"){
                                  participantRole(classCode:"MANU"){
                                      playingEntity(classCode:"MMAT"){
                                          code(code: allergy.drugCode,    //dyanmic
                                                  displayName:allergy.drugName,   //dyanmic
                                                  codeSystem: "2.16.840.1.113883.6.88",
                                                  codeSystemName:"RxNorm"){
                                              originalText(){
                                                  reference(value:"reaction-${allergy.uid}")
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

}
