package com.ids.ccda.sections

import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:37 AM
 * To change this template use File | Settings | File Templates.
 */
class VitalSignsSection {
    public static final TITLE = "Vital Signs"
    public static final SECTION_CODE = [code:"8716-3", displayName:"VITAL SIGNS"] + HL7_OID.LOINC
    public static final VITAL_SIGNS_CODE = [code:"46680005", displayName: "Vital signs"] + HL7_OID.SNOMED

    public static final HEIGHT = [code: "8302-2", displayName: "Height"]  + HL7_OID.LOINC
    public static final WEIGHT = [code: "3141-9", displayName: "Weight"]   + HL7_OID.LOINC
    public static final SYSTOLIC = [code: "8480-6", displayName: "BP Systolic"]   + HL7_OID.LOINC
    public static final DIASTOLIC = [code: "8462-4", displayName: "BP Diastolic"]  + HL7_OID.LOINC
    public static final BMI = [code: "39156-5", displayName: "BMI (Body Mass Index)"]   + HL7_OID.LOINC

    def map
    MarkupBuilder builder
    def ATTRS = ["uid", "date", "height", "weight", "systolic", "diastolic", "bmi"  ] //measurement, units
    def vitalSigns = [:]



    VitalSignsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.vitalSigns = map.vitalSigns
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(HL7_OID.VITAL_SIGNS_TEMPLATE_ID)
              code( SECTION_CODE )
              title( TITLE )
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
              templateId( HL7_OID.VITAL_SIGNS_ORGANIZER_TEMPLATE_ID)
              id(root:UUID.randomUUID())
              code( VITAL_SIGNS_CODE )
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
            templateId(HL7_OID.VITAL_SIGN_OBSERVATION_TEMPLATE_ID)
            id(root:UUID.randomUUID())
            code( vitalSignType )
            text(){ reference(value:"#vitalSign-${vitalSign.uid}")  }
            statusCode(code:"completed")
            effectiveTime(value:vitalSign.date)
            value( "xsi:type":"PQ",
                    value: measure.measurement,
                    unit: measure.units
            )
            interpretationCode(code:"N", codeSystem:"2.16.840.1.113883.5.83" )
        }
    }


}
