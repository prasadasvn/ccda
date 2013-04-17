package com.ids.ccda.sections

import groovy.xml.MarkupBuilder

class ImmunizationsSection {
    def map
    MarkupBuilder builder
    def immunizations = [:]
    def ATTRS = ["uid", "code", "name", "date"  ]
    def TEMPLATE_CODE = [code:"11369-6", displayName:"HISTORY OF IMMUNIZATIONS",
            codeSystem:"2.16.840.1.113883.6.1", codeSystemName:"LOINC"]
    def CVX_CODE = [ codeSystem: "2.16.840.1.113883.6.59", codeSystemName: "CVX"]

    ImmunizationsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.immunizations = this.map.immunizations
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(root:"2.16.840.1.113883.10.20.22.2.2.1")
              code( TEMPLATE_CODE )
              title("Immunications")
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
            templateId(root:"2.16.840.1.113883.10.20.22.4.52")
            id(root:UUID.randomUUID())
            text(){
                reference(value:"#$immunization-${immunization.uid}")
            }
            status(code:"completed")
            effectiveTime("xsi:type":"IVL_TS", value:immunization.date)
            //Immunization Medication Information
            consumable(){
                manufacturedProduct(classCode:"MANU"){
                    templateId(root:"2.16.840.1.113883.10.20.22.4.54")
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
        return map + CVX_CODE
    }
}
