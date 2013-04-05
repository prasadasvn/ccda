package com.ids.ccda

import com.ids.ccda.sections.AllergiesSection
import com.ids.ccda.sections.MedicationsSection
import com.ids.ccda.sections.ProblemsSection
import com.ids.ccda.sections.ProceduresSection
import com.ids.ccda.sections.ResultsSection
import com.ids.ccda.sections.SocialHistorySection
import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
class CDABody {
    def map
    MarkupBuilder builder
    public static void main(String[] args) {
        def map = [:]
        map.templateId = "5678"
        map.patient = [ id: "1234", firstName: "John", lastName: "Doe", middleName: "Q",
                dateOfBirth: "19690413", gender: "M", maritalStatus: "M",
                addressLine1: "123 Main St", addressLine2: "Apt 24", city: "Temple",
                state: "TX", zipCode: "76502"
        ]
        map.allergies = [
                [ uid: UUID.randomUUID(), observationUid: UUID.randomUUID(), statusCode:"active",
                        effectiveTimeLow: "20010214", effectiveTimeHigh:"", reactionCode: "59037007",
                        reactionName:"Nausea", drugCode:"12345", drugName:"A DRUG"],
                [ uid: UUID.randomUUID(), observationUid: UUID.randomUUID(), statusCode:"completed",
                        effectiveTimeLow: "", effectiveTimeHigh:"20100214", reactionCode: "416098002",
                        reactionName:"Difficulty Breathing", drugCode:"678910", drugName:"B DRUG"],

        ]

        map.medications = [
                [ uid: UUID.randomUUID(), consumableUid: UUID.randomUUID(), code: "RxNorm1", name: "A Drug 1",
                        startDate: "20130114", stopDate: "20130214",
                  instructions: "1 drop, into both eyes, twice a day"],

                [ uid: UUID.randomUUID(), consumableUid: UUID.randomUUID(), code: "RxNorm2", name: "A Drug 2",
                        startDate: "20121005", stopDate: "",
                        instructions: "1 drop, into left eye, once a day"],
        ]

        map.problems = [
                [ uid:  UUID.randomUUID(), observationUid: UUID.randomUUID(), statusUid: UUID.randomUUID(),
                  code:  "123456", name:  "Problem Name1",  statusCode: "55561003", statusName:"Active",
                        activeDates: [ startDate: "20090211", endDate: null],  observedDates: [ startDate: "20120114", endDate: null]
                ],
                [ uid:  UUID.randomUUID(), observationUid: UUID.randomUUID(), statusUid: UUID.randomUUID(),
                  code:  "765432", name:  "Problem Name2",  statusCode: "55561003", statusName:"Active",
                  activeDates: [ startDate: "20120114", endDate: null] ,  observedDates: [ startDate: "20120114", endDate: null]

                ]
        ]
        def performer =  [ firstName:  "John", lastName:  "House", npi: "123456789",
                telecom: "2541234567", addressLine1: "123 Main St",
                addressLine2: "Apt 456", city:  "Temple",  state: "TX", zipCode:  "76002" ]

        map.procedures = [
                [ uid: UUID.randomUUID(), code: "123456", name: "Procedure 1", date:  "20010401",
                  bodySiteCode: "123456", bodySiteName:" Name of body site 1",   performer: performer ] ,
                [ uid: UUID.randomUUID(), code: "789111", name: "Procedure 2", date:  "19920911",
                        bodySiteCode: "884422", bodySiteName:" Name of body site 2",   performer: performer ]
        ]

        map.socialHistoryElements = [
                [ uid:  UUID.randomUUID(), name:  "Smoking",
                  dates: [startDate:  "19560701", endDate:  "19880704"],
                  snomed: [code:"8517006", displayName: "Former smoker"]
                ]
        ]

        map.results = [
                [uid: UUID.randomUUID(), ]
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

            }
        }
    }
}
