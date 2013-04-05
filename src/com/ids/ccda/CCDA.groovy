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
        map.patient = [ id: "1234", firstName: "John", lastName: "Doe", middleName: "Q",
                dateOfBirth: "19690413", gender: "M", maritalStatus: "M",
                addressLine1: "123 Main St", addressLine2: "Apt 24", city: "Temple",
                state: "TX", zipCode: "76502"
        ]
        map.author = [:]
        map.custodian = [:]

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
