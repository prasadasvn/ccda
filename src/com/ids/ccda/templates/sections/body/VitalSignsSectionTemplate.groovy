package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.mixins.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

@Mixin(Comment)
class VitalSignsSectionTemplate implements BodySectionTemplate {
    public static final TITLE = "Vital Signs"
    public static final MAP_KEY = "vitalSigns"
    public static final SECTION_CODE = [code:"8716-3", displayName:"VITAL SIGNS"] + HL7_OID.LOINC
    public static final VITAL_SIGNS_CODE = [code:"46680005", displayName: "Vital signs"] + HL7_OID.SNOMED
    public static final HEIGHT = [code: "8302-2", displayName: "Height"]  + HL7_OID.LOINC
    public static final WEIGHT = [code: "3141-9", displayName: "Weight"]   + HL7_OID.LOINC
    public static final SYSTOLIC = [code: "8480-6", displayName: "BP Systolic"]   + HL7_OID.LOINC
    public static final DIASTOLIC = [code: "8462-4", displayName: "BP Diastolic"]  + HL7_OID.LOINC
    public static final BMI = [code: "39156-5", displayName: "BMI (Body Mass Index)"]   + HL7_OID.LOINC

    def ATTRS = [ "date", "height", "weight", "systolic", "diastolic", "bmi"  ] //measurement, units
    DocUid docUid
    MarkupBuilder builder
    Map map
    def vitalSigns = [:]

    VitalSignsSectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.vitalSigns = map.vitalSigns ?: [:]
        generate()
    }

    def generate(){
      builder.mkp.comment(comment())
      builder.component(){
          section(){
              templateId(HL7_OID.VITAL_SIGNS_TEMPLATE_ID)
              code( SECTION_CODE )
              title( TITLE )
              generateNormativeText()
              vitalSigns.each { id, vitalSign ->
                  generateEntry(id, vitalSign)
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
                vitalSigns.each{ id, vitalSign ->
                    def uid = docUid.secId(MAP_KEY,id)
                    tr{
                        td{ content(ID:"vitalSign-${uid}",vitalSign.date) }
                        td{ content(ID:"vitalSign-${uid}","${vitalSign.heightMeasurement} ${vitalSign.heightUnits}") }
                        td{ content(ID:"vitalSign-${uid}","${vitalSign.weightMeasurement} ${vitalSign.weightUnits}") }
                        td{ content(ID:"vitalSign-${uid}","${vitalSign.systolicMeasurement} ${vitalSign.systolicUnits}") }
                        td{ content(ID:"vitalSign-${uid}","${vitalSign.diastolicMeasurement} ${vitalSign.diastolicUnits}") }
                        td{ content(ID:"vitalSign-${uid}", "${vitalSign.bmiMeasurement} ${vitalSign.bmiUnits}") }
                    }
                }
            }
        }
    }

    def generateEntry( vitalSignId, vitalSign = [:]){
      def uid = docUid.secId(MAP_KEY,vitalSignId)
      builder.entry( typeCode:"DRIV"){
        //VITAL SIGNS ORGANIZER TEMPLATE
          organizer(classCode:"CLUSTER", moodCode:"EVN"){
              templateId( HL7_OID.VITAL_SIGNS_ORGANIZER_TEMPLATE_ID)
              id(root:UUID.randomUUID())
              code( VITAL_SIGNS_CODE )
              statusCode(code:"completed")
              effectiveTime(value:vitalSign.date)
              generateVitalSignObservation( uid, vitalSign, HEIGHT, vitalSign.heightMeasurement, vitalSign.heightUnits )
              generateVitalSignObservation( uid, vitalSign, WEIGHT, vitalSign.weightMeasurement, vitalSign.weightUnits )
              generateVitalSignObservation( uid, vitalSign, SYSTOLIC, vitalSign.systolicMeasurement, vitalSign.systolicUnits)
              generateVitalSignObservation( uid, vitalSign, DIASTOLIC, vitalSign.diastolicMeasurement, vitalSign.diastolicUnits )
              generateVitalSignObservation( uid, vitalSign, BMI, vitalSign.bmiMeasurement, vitalSign.bmiUnits )
          }

      }
    }

    def generateVitalSignObservation( uid, vitalSign, vitalSignType, measurement, units){
        builder.component(){
            //VITAL SIGNS OBSERVATION
            observation(classCode:"OBS", moodCode:"EVN")
            templateId(HL7_OID.VITAL_SIGN_OBSERVATION_TEMPLATE_ID)
            id(root:uid)
            code( vitalSignType )
            //text(){ reference(value:"#vitalSign-${uid}")  }
            statusCode(code:"completed")
            effectiveTime(value:vitalSign.date)
            value( "xsi:type":"PQ",
                    value: measurement,
                    unit:  units
            )
            interpretationCode(code:"N", codeSystem:"2.16.840.1.113883.5.83" )
        }
    }


}
