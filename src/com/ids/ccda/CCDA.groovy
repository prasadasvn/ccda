package com.ids.ccda

import com.ids.ccda.sections.AllergiesSection
import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/7/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
class CCDA {

    def map
    StringWriter writer
    MarkupBuilder builder


    public static void main(String[] args) {
        def map = [:]
        map.templateId = "5678"
        map.patient = [ id: "1234", mrn:"123", pmsId:"123",
                firstName: "Isabella", lastName: "Jones",  dateOfBirth: "19470501",
                addressLine1: "123 Main St", addressLine2: "Apt 24", city: "Temple", state: "TX", zipCode: "76502",
                homePhone: "(214) 123-456", workPhone: "(254) 456-7890", mobilePhone: "(214) 222-3333",
                email: "isabellajones@something.com",
                gender: [code: "F", displayName: "Female"], maritalStatus: [code:"M", displayName: "Married"],
                race: [code: "2106-3", displayName: "White"], ethnicity: [code: "2186-5", displayName: "Not Hispanic or Latino"]
        ]
        map.author = [:]
        map.custodian = [:]
        map.encounter = [startDate: "", endDate: "", attendingProvider:[npi:"12345678", firstName:"Tom" , lastName:"Crow"]]

        new CCDA(map)
    }

    CCDA(map) {
        this.map = map
        init()
        println writer
    }

    def init(){
       writer = new StringWriter()
       builder = new MarkupBuilder(writer)
        builder.ClinicalDocument(){
            new GeneralHeaderTemplate(builder, map).builder


        }
    }


}
