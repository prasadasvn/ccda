package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.mixins.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

@Mixin(Comment)
class ImmunizationsSectionTemplate   implements BodySectionTemplate{
    public static final TITLE = "Immunizations"
    public static final MAP_KEY = "immunizations"
    public static final SECTION_CODE = [code:"11369-6", displayName:"HISTORY OF IMMUNIZATIONS"] + HL7_OID.LOINC

    def ATTRS = [ "code", "name", "date"  ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def immunizations = [:]

    ImmunizationsSectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.immunizations = map.immunizations ?: [:]
        generate()
    }

    def generate(){
      builder.mkp.comment(comment())
      builder.component(){
          section(){
              templateId(HL7_OID.IMMUNIZATION_SECTION_TEMPLATE_ID)
              code( SECTION_CODE )
              title( TITLE )
              generateNormativeText()
              immunizations.each { id, immunization ->
                generateEntry(id, immunization)
              }
          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            content(ID:"immunSect")
            table(border:"1", width:"100%"){
                thead(){
                    tr(){
                        th("Vaccine")
                        th("Date")
                        th("Status")
                    }
                }
                immunizations.each{ id,immunization ->
                    def uid = docUid.secId(MAP_KEY,id)
                    tr(){
                        td{ content(ID:"immunization-${uid}",immunization.name) }
                        td(immunization.date)
                        td("Completed")
                    }
                }
            }
        }
    }

    def generateEntry(immunizationId, immunization = [:]){
      def uid = docUid.secId(MAP_KEY,immunizationId)
      builder.entry( typeCode:"DRIV"){
          //Immunization Activity Template
          substanceAdministration(classCode:"SBADM", moodCode:"EVN", negationInd:"false"){
            templateId(HL7_OID.IMMUNIZATION_ACTIVITY_TEMPLATE_ID)
            id(root:uid)
            //text(){ reference(value:"#$immunization-${uid}") }
            status(code:"completed")
            effectiveTime("xsi:type":"IVL_TS", value:immunization.date)
            //Immunization Medication Information
            consumable(){
                manufacturedProduct(classCode:"MANU"){
                    templateId(HL7_OID.IMMUNIZATION_MEDICATION_INFORMATION_TEMPLATE_ID)
                    manufacturedMaterial(){
                        code( immunizationCode([code:immunization.code, displayName: immunization.name] ) )
                        //originalText(immunization.name)
                    }
                }
            }

          }
      }
    }

    private def immunizationCode(map = [:]){
        return map + HL7_OID.CVX
    }
}
