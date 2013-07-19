package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

@Mixin(Comment)
class SocialHistorySectionTemplate  implements BodySectionTemplate {
    public static final TITLE = "Social History"
    public static final MAP_KEY = "socialHistoryElements"
    public static final SECTION_CODE = [code:"29762-2", displayName:"SOCIAL HISTORY"] + HL7_OID.LOINC
    public static final SMOKING_STATUS_OBSERVATION_CODE = [code:"ASSERTION"] + HL7_OID.ACT_CODE

    def ATTRS = [ "name", "startDate","endDate",  "elementCode", "elementDisplayName"]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def socialHistoryElements = [:]

    SocialHistorySectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.socialHistoryElements = map.socialHistoryElements ?: [:]
        generate()
    }

    def generate(){
        builder.mkp.comment(comment())
        builder.component(){
          section(){
              templateId( HL7_OID.SOCIAL_HISTORY_SECTION_TEMPLATE_ID )
              code( SECTION_CODE )
              title( TITLE )
              generateNormativeText()
              socialHistoryElements.each { id, socialHistoryElement ->
                generateEntry(id, socialHistoryElement)
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
                socialHistoryElements.each{ id, socialHistoryElement ->
                    def uid = docUid.secId(MAP_KEY,id)
                    tr{
                        td{ content(ID:"socialHistoryElement-${uid}", socialHistoryElement.name) }//dynamic
                        td(socialHistoryElement.elementDisplayName) //dynamic
                        td(socialHistoryElement.startDate)  //dynamic
                        td(socialHistoryElement.endDate)  //dynamic
                    }
                }
            }
        }
    }

    def generateEntry( socialHistoryId, socialHistoryElement = [:]){
      def uid = docUid.secId(MAP_KEY,socialHistoryId)
      builder.entry( typeCode:"DRIV"){
          // Social history section template
          observation(classCode:"OBS", moodCode:"EVN"){
            //smoking status
            templateId(HL7_OID.SMOKING_STATUS_OBSERVATION_TEMPLATE_ID)
            code( SMOKING_STATUS_OBSERVATION_CODE )
            statusCode(code:"completed")
            generateEffectiveTime( socialHistoryElement.dates) //dynamic
            value(["xsi:type":"CD",
                   code:socialHistoryElement.elementCode,
                   displayName: socialHistoryElement.elementDisplayName ] + HL7_OID.SNOMED
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
