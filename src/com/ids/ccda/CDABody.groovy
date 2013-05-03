package com.ids.ccda

import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID
import com.ids.ccda.sections.AllergiesSection
import com.ids.ccda.sections.ImmunizationsSection
import com.ids.ccda.sections.MedicationsSection
import com.ids.ccda.sections.ProblemsSection
import com.ids.ccda.sections.ProceduresSection
import com.ids.ccda.sections.ResultsSection
import com.ids.ccda.sections.SocialHistorySection
import com.ids.ccda.sections.VitalSignsSection
import groovy.xml.MarkupBuilder

class CDABody {
    def map
    DocUid docUid
    MarkupBuilder builder



    CDABody(map) {
        this.map = map
        docUid = new DocUid("${IDS_OID.IDS_CLINICAL_SUMMARY}.1.1234.${(new Date()).format(IDS_OID.FORMAT_DATE_OID)}")  //TODO: To be replaced
        map.docUid = docUid
    }

    public static void main(String[] args) {
        def map = [:]

        map.templateId = "5678"
        map.patient = [ id: "1234", firstName: "John", lastName: "Doe", middleName: "Q",
                dateOfBirth: "19690413", gender: "M", maritalStatus: "M",
                addressLine1: "123 Main St", addressLine2: "Apt 24", city: "Temple",
                state: "TX", zipCode: "76502"
        ]
        map.allergies = [
                 "${id()}": [  statusCode:"active", effectiveTimeLow: "20010214", effectiveTimeHigh:"", reactionCode: "59037007",
                        reactionName:"Nausea", drugCode:"12345", drugName:"A DRUG"],
                "${id()}":[  statusCode:"active", effectiveTimeLow: "20010214", effectiveTimeHigh:"", reactionCode: "59037007",
                        reactionName:"Nausea", drugCode:"12345", drugName:"A DRUG"],
                "${id()}":[  statusCode:"completed",
                        effectiveTimeLow: "", effectiveTimeHigh:"20100214", reactionCode: "416098002",
                        reactionName:"Difficulty Breathing", drugCode:"678910", drugName:"B DRUG"]

        ]

        map.medications = [
                "${id()}": [  code: "RxNorm1", name: "A Drug 1",
                        startDate: "20130114", stopDate: "20130214",
                  instructions: "1 drop, into both eyes, twice a day"],

                "${id()}":[  code: "RxNorm2", name: "A Drug 2",
                        startDate: "20121005", stopDate: "",
                        instructions: "1 drop, into left eye, once a day"],
        ]

        map.problems = [
                "${id()}":[  code:  "123456", name:  "Problem Name1",  statusCode: "55561003", statusName:"Active",
                        activeDates: [ startDate: "20090211", endDate: null],  observedDates: [ startDate: "20120114", endDate: null]
                ],
                "${id()}":[    code:  "765432", name:  "Problem Name2",  statusCode: "55561003", statusName:"Active",
                  activeDates: [ startDate: "20120114", endDate: null] ,  observedDates: [ startDate: "20120114", endDate: null]

                ]
        ]
        def performer =  [ firstName:  "John", lastName:  "House", npi: "123456789",
                telecom: "2541234567", addressLine1: "123 Main St",
                addressLine2: "Apt 456", city:  "Temple",  state: "TX", zipCode:  "76002" ]

        map.procedures = [
                "${id()}": [ code: "123456", name: "Procedure 1", date:  "20010401",
                  bodySiteCode: "123456", bodySiteName:" Name of body site 1",   performer: performer ] ,
                "${id()}": [ code: "789111", name: "Procedure 2", date:  "19920911",
                        bodySiteCode: "884422", bodySiteName:" Name of body site 2",   performer: performer ]
        ]

        map.socialHistoryElements = [
                "${id()}":[  name:  "Smoking",
                  dates: [startDate:  "19560701", endDate:  "19880704"],
                  snomed: [code:"8517006", displayName: "Former smoker"]
                ]
        ]

        map.results = [
                "${id()}": [snomed: [code:"SNOMED Code1", displayName: "SNOMED DisplayName1", status:"active"],
                 observations:[
                         [uuid:UUID.randomUUID(),
                          code: [code: "Observation Code 1",
                                 displayName: "Observation DisplayName1",
                                 codeSystem: "2.16.840.1.113883.6.1",
                                 codeSystemName:"LOINC"],
                          status: "active",
                          effectiveTime: "20130401124400",
                          value: [value: "12.3", unit: "10+3/ul"]
                         ]
                   ]
                ]
        ]

        map.vitalSigns = [
                "${id()}":[  date: "20081101",
                height: [measurement: "177", units: "cm"],
                weight: [measurement: "86", units: "kg"],
                systolic: [measurement: "132", units: "mmHg"],
                diastolic: [measurement: "86", units: "mmHg"],
                bmi: [measurement: "27.45", units: ""]
              ],
                "${id()}":[  date: "20120806",
                      height: [measurement: "177", units: "cm"],
                      weight: [measurement: "88", units: "kg"],
                      systolic: [measurement: "145", units: "mmHg"],
                      diastolic: [measurement: "88", units: "mmHg"],
                      bmi: [measurement: "28.09", units: ""]
              ]
        ]

        map.immunizations = [
                "${id()}":[  code: "88", name: "Influenza virus vaccine", date: "20051101"],
                "${id()}":[  code: "88", name: "Influenza virus vaccine", date: "20060910"],
                "${id()}":[  code: "09", name: "Tetanus-diphtheria adult", date: "20070104"],
                "${id()}":[  code: "33", name: "Pneumococcal polysaccharide", date: "20120806"]
        ]

        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        new CDABody(builder, map)
        println writer
    }

    CDABody( MarkupBuilder builder, map = [:]) {
        this.map = map
        this.builder = builder
        generate()
    }

    def generate(){
        builder.component(){
            structuredBody{  //sections are defined and limited here
              new AllergiesSection(builder,map).builder
              new MedicationsSection(builder,map).builder
              new ProblemsSection(builder,map).builder
              new ProceduresSection(builder, map).builder
              new SocialHistorySection(builder, map).builder
              new ResultsSection(builder,map).builder
              new VitalSignsSection(builder,map).builder
              new ImmunizationsSection(builder,map).builder

            }
        }
    }

    // Need globally unique OID, INTEGRITY_OID + EMR + DOC + TENANT + PATIENT + DATETIME
    private def clinicSummaryId( tenantId, patientId, Date dateGenerated = new Date(), String idsClinicalSummaryOid = IDS_OID.IDS_CLINICAL_SUMMARY){
        return "${idsClinicalSummaryOid}.${tenantId}.${patientId}.${dateGenerated.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED)}"
    }

    // "${IDS_OID.IDS_CLINICAL_SUMMARY}.1.1234.${(new Date()).format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED)}

    //TODO: To be removed once data is removed
   static  Random randomIntGenerator = new Random( 1452356 )
   static  def id(){
       return randomIntGenerator.nextInt(10000000).toString()
    }
}
