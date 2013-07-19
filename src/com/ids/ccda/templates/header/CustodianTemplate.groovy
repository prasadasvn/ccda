package com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID
import groovy.xml.MarkupBuilder

class CustodianTemplate  implements HeaderTemplate {

    def ATTRS = [ "practiceName", "npi","addressLine1", "addressLine2", "city", "state", "zipCode", "phone" ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def custodian = [:]

    CustodianTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.custodian = map.custodian ?: [:]
        generate()
    }

    def generate(){
        builder.custodian(){
            assignedCustodian(){
                representedCustodianOrganization(){
                    id(root:HL7_OID.NPI, extension: custodian.npi)  //Organization NPI
                    name(custodian.practiceName) //Practice Name
                    telecom( value: "tel:${custodian.phone}" , use: "WP")
                    addr(use:"PUB" ){
                        streetAddressLine(custodian.addressLine1)
                        streetAddressLine(custodian.addressLine2)
                        city(custodian.city)
                        state(custodian.state)
                        postalCode(custodian.zipCode)
                        country("US")
                    }
                }
            }
        }
    }


}
