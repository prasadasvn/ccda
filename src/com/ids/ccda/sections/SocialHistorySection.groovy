package com.ids.ccda.sections

import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

class SocialHistorySection {
    public static final TITLE = "Social History"
    public static final SECTION_CODE = [code:"29762-2", displayName:"SOCIAL HISTORY"] + HL7_OID.LOINC
    public static final SMOKING_STATUS_OBSERVATION_CODE = [code:"ASSERTION"] + HL7_OID.ACT_CODE

    def map
    def socialHistoryElements = [:]
    MarkupBuilder builder
    def ATTRS = ["uid", "type", "dates"/*startDate,endDate*/,  "snomed" /*code,displayName */ ]


    SocialHistorySection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.socialHistoryElements = map.socialHistoryElements
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId( HL7_OID.SOCIAL_HISTORY_SECTION_TEMPLATE_ID )
              code( SECTION_CODE )
              title( TITLE )
              generateNormativeText()
              socialHistoryElements.each { socialHistoryElement ->
                generateEntry(socialHistoryElement)
              }
          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            table(border:"1", width:"100%"){
                thead(){
                    tr(){
                        th("Social History Element")
                        th("Description")
                        th("From Date")
                        th("To Date")
                    }
                }
                socialHistoryElements.each{ socialHistoryElement ->
                    tr(){
                        td(){ content(ID:"socialHistoryElement-${socialHistoryElement.uid}"){socialHistoryElement.type} }//dynamic
                        td(socialHistoryElement.snomed.displayName) //dynamic
                        td(socialHistoryElement.dates.startDate)  //dynamic
                        td(socialHistoryElement.dates.endDate)  //dynamic
                    }
                }
            }
        }
    }

    def generateEntry( socialHistoryElement = [:]){
      builder.entry( typeCode:"DRIV"){
          // Social history section template
          observation(classCode:"OBS", moodCode:"EVN"){
            //smoking status
            templateId(HL7_OID.SMOKING_STATUS_OBSERVATION_TEMPLATE_ID)
            code( SMOKING_STATUS_OBSERVATION_CODE )
            statusCode(code:"completed")
            generateEffectiveTime( socialHistoryElement.dates) //dynamic
            value(["xsi:type":"CD",
                   code:socialHistoryElement.snomed.code,
                   displayName: socialHistoryElement.snomed.displayName ] + HL7_OID.SNOMED
            )
        }
      }
    }

    private generateSmokingEffectiveTime( dates = [:]){
        /*
        If the patient is a smoker (77176002), the low element must be present
        If the patient is an ex-smoker (8517006), the low and high must be present

        */
        builder.effectiveTime(){
            if(dates.startDate){
                low(value:dates.startDate)
            }  else {
                low(nullFlavor: "NI")
            }
            if(dates.endDate){
                high(value:dates.endDate)
            } else {
                high(nullFlavor:"NI")
            }
        }
    }
}
