package com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID
import groovy.xml.MarkupBuilder

class RecordTargetTemplate  implements HeaderTemplate {
    public static final TEST_MAP = [id: "1234", firstName: "John", lastName: "Doe", middleName: "Q",  dateOfBirth: "19690413",
      addressLine1: "123 Main St", addressLine2: "Apt 24", city: "Temple",  state: "TX", zipCode: "76502",
      homePhone: "(214) 123-4567", workPhone: "(214) 789-0012", email: "johndoe@testme.com",
      genderCode: "M", genderDisplayName: "Male", maritalStatusCode: "M", maritalStatusDisplayName: "Married",
      raceCode: "2106-3", raceDisplayName: "White", ethnicityCode:"2186-5", ethnicityDisplayName:"Not Hispanic or Latino"
    ]
    def ATTRS = [ "id", "mrn", "pmsId",
            "lastName", "firstName", "middleName",  "dateOfBirth",
            "addressLine1", "addressLine2", "city", "state", "zipCode",
            "homePhone", "workPhone", "mobilePhone", "email",
            "genderCode", "genderDisplayName","maritalStatusCode", "maritalStatusDisplayName", "raceCode", "raceDisplayName", "ethnicityCode", "ethnicityDisplayName" //TODO: each with code and displayName attrs
    ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def patient = [:]

    RecordTargetTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.patient = map.patient ?: [:]
        generate()
    }

    def generate(){
        builder.recordTarget(){
            patientRole(){
                id(extension: patient.mrn, root: IDS_OID.IDS_PATIENT_MRN )
                id(extension: patient.mrn, root: IDS_OID.IDS_PATIENT_MRN )
                id(extension: patient.id, root: IDS_OID.IDS_PATIENT_ID )
                id(extension: patient.pmsId, root: IDS_OID.IDS_PATIENT_PMS_ID )
                addr(use:"HP" ){
                    streetAddressLine(patient.addressLine1)
                    streetAddressLine(patient.addressLine2)
                    city(patient.city)
                    state(patient.state)
                    postalCode(patient.zipCode)
                    country("US")
                }
                telecom( use:"HP", value: "tel:${patient.homePhone}"  )  //TODO: will need to convert to () -  format
                telecom( use:"WP", value: "tel:${patient.workPhone}"  )  //TODO: will need to convert to () -  format
                telecom( use:"MC", value: "tel:${patient.mobilePhone}"  )   //TODO: will need to convert to () -  format
                telecom(           value: "mailto:${patient.email}"  )
                patient(){
                    name( use:"L"){
                        family(patient.lastName)
                        given(patient.firstName)
                        given(patient.middleName)
                    }
                    administrativeGenderCode( [code: patient.genderCode, displayName: patient.genderDiplayName] + HL7_OID.GENDER_CODE)
                    birthTime(patient.dateOfBirth)
                    maritalStatusCode([code:patient.maritalStatusCode, displayName:patient.maritalStatusDisplayName ] +  HL7_OID.MARITAL_CODE)
                    raceCode( [code: patient.raceCode, displayName: patient.raceDisplayName] +  HL7_OID.RACE_OR_ETHNICITY_CODE )
                    ethnicGroupCode(  [code:patient.ethnicityCode, displayame: patient.ethnicityDisplayName] + HL7_OID.RACE_OR_ETHNICITY_CODE )
                }
                //providerOrganization() //TODO:if we want this (not required), we'll need to add address and telephone fields to either clinic site or practice
            }
        }
    }


}
