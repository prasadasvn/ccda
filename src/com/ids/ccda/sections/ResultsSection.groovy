package com.ids.ccda.sections

import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

class ResultsSection {
    public static final TITLE = "Results"
    public static final SECTION_CODE = [code:"30954-2",displayName:"RESULTS"] + HL7_OID.LOINC
    public static final SECTION = "results"

    def map
    DocUid docUid
    MarkupBuilder builder
    def results = [:]
    def ATTRS = [ "snomed" /*code, displayName*/, "observations", "status"  ]
    def OBSERVATION_ATTRS = ["code", /*code, displayName, codeSystem, codeSystemName  -- should be LOINC for Labs and LOINC or SNOMED for others*/
            "status", "effectiveTime", "value" /*value, unit */  ]

    //pending results should use the status of active

    ResultsSection(builder, map =[:]) {
        this.map = map
        this.docUid = map.docUid
        this.results = map.results
        this.builder = builder
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(HL7_OID.RESULTS_SECTION_TEMPLATE_ID)
              code( SECTION_CODE )
              title(TITLE)
              generateNormativeText()
              results.each { id, result ->
                generateEntry(id, result)
              }
          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            table(border:"1", width:"100%"){
                thead(){
                    tr(){ td(){}  } //TODO:What needs to go here?
                }
                results.each{ id,result ->
                    def uid = docUid.secId(SECTION,id)
                    tr(){
                        td(){ content(ID:"result-${uid}"){result.name}  }
                    }
                }
            }
        }
    }

    def generateEntry(resultId, result = [:]){
      def uid = docUid.secId(SECTION,resultId)
      builder.entry( typeCode:"DRIV"){
          // RESULTS ORGANIZER TEMPLATE
         organizer(class:"BATTERY", moodCode:"EVN"){
             templateId(root:"2.16.840.1.113883.10.20.22.4.1")
             id(root:uid)
             code("xsi:type":"CE",
                  code:result.snomed.code, //dynamic
                  displayName: result.snomed.displayName, //dynamic
                  codeSystem: HL7_OID.SNOMED,
                  codeSystemName: "SNOMED CT")
             statusCode(code:result.status )//dynamic
             component(){
                 //RESULT OBSERVATION TEMPLATES
                 result.observations.each { o ->
                     generateObservation(uid, o)
                 }
             }
         }
      }
    }

    def generateObservation(resultUid, obsResult = [:]){
        builder.observation(classCode:"OBS", moodCode:"EVN"){
            templateId(root:"2.16.840.1.113883.10.20.22.4.2")
            id(root:UUID.randomUUID())

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
                               codeSystem: HL7_OID.INTERPRETATION_RESULTS
            )



        }
    }
}
