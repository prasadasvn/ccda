package com.ids.ccda.sections

import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
class VitalSignsSection {
    def map
    MarkupBuilder builder
    def ATTRS = ["uid", "date", "height", "weight", "systolic", "diastolic", "bmi"  ] //measurement, units
    def vitalSigns = [:]
    def HEIGHT = [code: "8302-2", displayName: "Height"]
    def WEIGHT = [code: "3141-9", displayName: "Weight"]
    def SYSTOLIC = [code: "8480-6", displayName: "BP Systolic"]
    def DIASTOLIC = [code: "8462-4", displayName: "BP Diastolic"]
    def BMI = [code: "39156-5", displayName: "BMI (Body Mass Index)"]


    VitalSignsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.vitalSigns = map.vitalSigns
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(root:"2.16.840.1.113883.10.20.22.2.4.1")
              code(code:"8716-3",
                   codeSystem:"2.16.840.1.113883.6.1",
                   codeSystemName:"LOINC",
                   displayName:"VITAL SIGNS"
              )
              title("Vital Signs")
              generateNormativeText()
              vitalSigns.each { vitalSign ->
                  generateEntry(vitalSign)
              }

          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            table(border:"1", width:"100%"){
                thead(){
                    tr(){
                        th(align:"right"){"Date / Time"}
                        th("Height")
                        th("Weight")
                        th("BP Systolic")
                        th("BP Diastolic")
                        th("BMI")
                    }
                }
                vitalSigns.each{ vitalSign ->
                    tr(){
                        td(){ content(ID:"vitalSign-${vitalSign.uid}"){vitalSign.date} }
                        td(){ content(ID:"vitalSign-${vitalSign.uid}"){vitalSign.height} }
                        td(){ content(ID:"vitalSign-${vitalSign.uid}"){vitalSign.weight} }
                        td(){ content(ID:"vitalSign-${vitalSign.uid}"){vitalSign.systolic} }
                        td(){ content(ID:"vitalSign-${vitalSign.uid}"){vitalSign.diastolic} }
                        td(){ content(ID:"vitalSign-${vitalSign.uid}"){vitalSign.bmi} }
                    }
                }
            }
        }
    }

    def generateEntry( vitalSign = [:]){
      builder.entry( typeCode:"DRIV"){
        //VITAL SIGNS ORGANIZER TEMPLATE
          organizer(classCode:"CLUSTER", moodCode:"EVN"){
              templateId(root:"2.16.840.1.113883.10.20.22.4.26")
              id(root:UUID.randomUUID())
              code( code:"46680005",
                    codeSystem:"2.16.840.1.113883.6.96",
                    codeSystemName:" SNOMED-CT",
                    displayName: "Vital signs"
              )
              statusCode(code:"completed")
              effectiveTime(value:vitalSign.date)
              generateVitalSignObservation(vitalSign, HEIGHT, vitalSign.height )
              generateVitalSignObservation(vitalSign, WEIGHT, vitalSign.weight )
              generateVitalSignObservation(vitalSign, SYSTOLIC, vitalSign.systolic)
              generateVitalSignObservation(vitalSign, DIASTOLIC, vitalSign.diastolic )
              generateVitalSignObservation(vitalSign, BMI, vitalSign.diastolic )
          }

      }
    }

    def generateVitalSignObservation( vitalSign, vitalSignType, measure){
        builder.component(){
            //VITAL SIGNS OBSERVATION
            observation(classCode:"OBS", moodCode:"EVN")
            templateId(root:"2.16.840.1.113883.10.20.22.4.27")
            id(root:UUID.randomUUID())
            code( vitalSignsResultType(vitalSignType) )
            text(){
                reference(value:"#vitalSign-${vitalSign.uid}")
            }
            statusCode(code:"completed")
            effectiveTime(value:vitalSign.date)
            value( "xsi:type":"PQ",
                    value: measure.measurement,
                    unit: measure.units
            )
            interpretationCode(code:"N",
                    codeSystem:"2.16.840.1.113883.5.83"
            )
        }
    }

    def vitalSignsResultType(map = [:]){
        return map + [codeSytem: "2.16.840.1.113883.6.1", codeSystemName:"LOINC"]
    }
}
