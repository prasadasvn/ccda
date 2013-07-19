package com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

class ServiceEventTemplate implements HeaderTemplate {

    def ATTRS = [ "startDate", "endDate", "primaryDiagnosisCode", "primaryDiagnosisDisplayName", "attendingProviderNpi", "attendingProviderLastName", "attendingProviderFirstName"  ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def encounters = []

    ServiceEventTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.encounters = map.encounters ?: [:]
        generate()
    }

    def generate(){
        encounters.each { encounter -> generateServiceEvent(encounter)}
    }

    def generateServiceEvent( encounter = [:]){
        builder.documentationOf( typeCode:"DOC"){
            serviceEvent(classCode:"PCPR"){
                code( [encounter.primaryDiagnosisCode, encounter.primaryDiagnosisDisplayName ]+ HL7_OID.SNOMED )
                effectiveTime(){
                    low(value:encounter.startDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
                    high(value:encounter.endDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
                }

                performer(typeCode:"PRF"){
                    //function code could be PP - PrimaryCarePhysician, RP - Referring, or CP Consulting
                    functionCode([code:"PP",  displayName:"Primary Performer"] + HL7_OID.PROVIDER_ROLE  ){
                        originalText("Primary Care Provider")
                    }
                    time(){
                        low(value:encounter.startDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
                        high(value:encounter.endDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
                    }
                    assignedEntity(){
                        id( root: HL7_OID.NPI, extension: encounter.attendingProviderNpi)
                        assignedPerson(){
                            name(){
                                given(encounter.attendingProviderFirstName)
                                family(encounter.attendingProviderLastName)
                            }
                        }
                    }

                }
            }
        }
    }

}
