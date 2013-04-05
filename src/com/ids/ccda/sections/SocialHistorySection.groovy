package com.ids.ccda.sections

import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
class SocialHistorySection {
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
              templateId(root:"2.16.840.1.113883.10.20.22.2.17")
              code(code:"29762-2",
                   codeSystem:"2.16.840.1.113883.6.1",
                   codeSystemName:"LOINC",
                   displayName:"SOCIAL HISTORY"
              )
              title("Social History")
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
                        td(){
                            content(ID:"socialHistoryElement-${socialHistoryElement.uid}"){socialHistoryElement.type}//dynamic
                        }
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
            templateId(root:"2.16.840.1.113883.10.22.4.78")
            code(code:"ASSERTION", codeSystem:"2.16.840.1.113883.5.4" )
            statusCode(code:"completed")
            generateEffectiveTime( socialHistoryElement.dates) //dynamic
            value("xsi:type":"CD",
                   code:socialHistoryElement.snomed.code,    //dynamic
                   displayName: socialHistoryElement.snomed.displayName, //dynamic
                   codeSystem: "2.16.840.1.113883.6.96"
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
