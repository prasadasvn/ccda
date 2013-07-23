package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.mixins.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

@Mixin(Comment)
class ResultsSectionTemplate implements BodySectionTemplate {
    public static final TITLE = "Results"
    public static final MAP_KEY = "results"
    public static final SECTION_CODE = [code:"30954-2",displayName:"RESULTS"] + HL7_OID.LOINC


    def ATTRS = [ "resultCode", "resultName", "status", "effectiveTime",
            "observationLoincCode", "observationSnomedCode", "observationName" ,
            "observationStatus",  "observationValue", "observationUnit"
            ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def results = [:]
    //pending results should use the status of active
    ResultsSectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.results = map.results  ?: [:]
        generate()
    }

    def generate(){
        builder.mkp.comment(comment())
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
                    def uid = docUid.secId(MAP_KEY,id)
                    tr{
                        td{ content(ID:"result-${uid}", result.name)  }
                    }
                }
            }
        }
    }

    def generateEntry(resultId, result = [:]){
      def uid = docUid.secId(MAP_KEY,resultId)
      builder.entry( typeCode:"DRIV"){
          // RESULTS ORGANIZER TEMPLATE
         organizer(class:"BATTERY", moodCode:"EVN"){
             templateId(root:"2.16.840.1.113883.10.20.22.4.1")
             id(root:uid)
             code("xsi:type":"CE",
                  code:result.resultCode, //dynamic
                  displayName: result.resultName, //dynamic
                  codeSystem: HL7_OID.SNOMED,
                  codeSystemName: "SNOMED CT")
             statusCode(code:result.status )//dynamic
             component(){
                 //RESULT OBSERVATION TEMPLATES  -- each organizer could have multiple observations, but in our use we only will return one observation per organizer to simplify our data structure
                     generateObservation(resultId, result)

             }
         }
      }
    }

    def generateObservation(resultId, result = [:]){
        builder.observation(classCode:"OBS", moodCode:"EVN"){
            templateId(root:"2.16.840.1.113883.10.20.22.4.2")
            id(docUid.resultObsId(resultId))
            if(result.observationSnomedCode && !result.observationLoincCode)
            {
             code(["xsi:type":"CE",
                  code: result.observationSnomedCode,
                  displayName: result.observationName] + HL7_OID.SNOMED)
            }
            else if(!result.observationSnomedCode && result.observationLoincCode)
            {
              code(["xsi:type":"CE",
                    code: result.observationLoincCode,
                    displayName: result.observationName] + HL7_OID.LOINC)
            }
            else { code(nullFlavor:"NI") }
           // text(){ reference(value:"#result-${docUid.secId(MAP_KEY,resultId)}") }
            statusCode(code:result.status)
            effectiveTime(result.effectiveTime)//dynamic
            value("xsi:type":"PQ",
                  value: result.observationValue,  //dynamic
                  unit: result.observationUnit    //dynamic
            )
            interpretationCode(code:"N",  codeSystem: HL7_OID.INTERPRETATION_RESULTS
            )



        }
    }
}
