package com.ids.ccda.sections

import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

class ImmunizationsSection {
    public static final TITLE = "Immunizations"

    def map
    MarkupBuilder builder
    def immunizations = [:]

    def ATTRS = ["uid", "code", "name", "date"  ]
    def SECTION_CODE = [code:"11369-6", displayName:"HISTORY OF IMMUNIZATIONS"] + HL7_OID.LOINC

    ImmunizationsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.immunizations = this.map.immunizations
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(HL7_OID.IMMUNIZATION_SECTION_TEMPLATE_ID)
              code( SECTION_CODE )
              title( TITLE )
              generateNormativeText()
              immunizations.each { immunization ->
                generateEntry(immunization)
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
                immunizations.each{ immunization ->
                    tr(){
                        td(){
                            content(ID:"immunization-${immunization.uid}"){immunization.name}
                        }
                        td(immunization.date)
                        td("Completed")
                    }
                }
            }
        }
    }

    def generateEntry( immunization = [:]){
      builder.entry( typeCode:"DRIV"){
          //Immunization Activity Template
          substanceAdministration(classCode:"SBADM", moodCode:"EVN", negationInd:"false"){
            templateId(HL7_OID.SUBSTANCE_ADMINISTRATION_TEMPLATE_ID)
            id(root:UUID.randomUUID())
            text(){ reference(value:"#$immunization-${immunization.uid}") }
            status(code:"completed")
            effectiveTime("xsi:type":"IVL_TS", value:immunization.date)
            //Immunization Medication Information
            consumable(){
                manufacturedProduct(classCode:"MANU"){
                    templateId(HL7_OID.IMMUNIZATION_MEDICATION_INFORMATION_TEMPLATE_ID)
                    manufacturedMaterial(){
                        code( immunizationCode([code:immunization.code, displayName: immunization.name] ) )
                        originalText(immunization.name)
                    }
                }
            }

          }
      }
    }

    def immunizationCode(map = [:]){
        return map + HL7_OID.CVX
    }
}
