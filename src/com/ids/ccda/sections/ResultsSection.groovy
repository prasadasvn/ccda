package com.ids.ccda.sections

import com.ids.ccda.OIDS
import groovy.xml.MarkupBuilder

class ResultsSection {
    def map
    MarkupBuilder builder
    def results = [:]
    def ATTRS = ["uid", "snomed" /*code, displayName*/, "observations", "status"  ]
    def OBSERVATION_ATTRS = ["uid", "code", /*code, displayName, codeSystem, codeSystemName  -- should be LOINC for Labs and LOINC or SNOMED for others*/
            "status", "effectiveTime", "value" /*value, unit */  ]

    //pending results should use the status of active

    ResultsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.results = this.map.results
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(root:"2.16.840.1.113883.10.20.22.2.3.1")
              code(code:"30954-2",
                   codeSystem:"2.16.840.1.113883.6.1",
                   codeSystemName:"LOINC",
                   displayName:"RESULTS"
              )
              title("Results")
              generateNormativeText()
              results.each { result ->
                generateEntry(result)
              }

          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            table(border:"1", width:"100%"){
                thead(){
                    tr(){

                    }
                }
                results.each{ result ->
                    tr(){
                        td(){
                            content(ID:"result-${result.uid}"){result.name}
                        }
                    }
                }
            }
        }
    }

    def generateEntry( result = [:]){
      builder.entry( typeCode:"DRIV"){
          // RESULTS ORGANIZER TEMPLATE
         organizaer(class:"BATTERY", moodCode:"EVN"){
             templateId(root:"2.16.840.1.113883.10.20.22.4.1")
             id(root:result.uuid)
             code("xsi:type":"CE",
                  code:result.snomed.code, //dynamic
                  displayName: result.snomed.displayName, //dynamic
                  codeSystem: OIDS.SNOMED,
                  codeSystemName: "SNOMED CT")
             statusCode(code:result.status )//dynamic
             component(){
                 //RESULT OBSERVATION TEMPLATES
                 result.observations.each { observation ->
                     generateObservation(result.uid, observation)
                 }
             }
         }
      }
    }

    def generateObservation(resultUid, obsResult = [:]){
        observation(classCode:"OBS", moodCode:"EVN"){
            templateId(root:"2.16.840.1.113883.10.20.22.4.2")
            id(root:obsResult.uid)  //dynamic

            code("xsi:type":"CE",
                    code:obsResult.code.code, //dynamic
                    displayName: obsResult.code.displayName, //dynamic
                    codeSystem: obsResult.code.codeSystem,   //dynamic
                    codeSystemName: obsResult.code.codeSystemName)    //dynamic
            text(){
                reference(value:"#result-${resultUid}")
            }
            statusCode(code:obsResult.status)
            effectiveTime(obsResult.effectiveTime)//dynamic
            value("xsi:type":"PQ",
                  value: obsResult.value.value,  //dynamic
                  unit: obsResult.value.unit    //dynamic
            )
            interpretationCode(code:obsResult.interpretationCode, //dynamic
                               codeSystem: OIDS.INTERPRETATION_RESULTS
            )



        }
    }
}
