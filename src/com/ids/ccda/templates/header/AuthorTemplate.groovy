package com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID
import groovy.xml.MarkupBuilder

class AuthorTemplate  implements HeaderTemplate { //This seems to be a completely static template

    public static final AUTHOR =  [ companyName: "Integrity Digital Solutions, LLC", softwareName: "Integrity EMR",
            addressLine1: "6420 South General Bruce Drive", city: "Temple", state: "TX", zipCode: "76524",
            phone: "(877) 905-4680",
            code:"207W00000X", codeDisplayName:"OPHTHALMOLOGY", codeSystem:"2.16.840.1.113883.6.101"   ]
    def ATTRS = [ ]
    DocUid docUid
    MarkupBuilder builder
    Map map

    AuthorTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = [:]
        this.map.author = AUTHOR // + doc?.map?.author
        generate()
    }

    def generate(){
        def author = map.author
        builder.author(){  //INTEGRITY EMR IS THE AUTHOR
            time(value: new Date().format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
            assignedAuthor(){
                id( root: HL7_OID.NPI,     nullFlavor:"NA")
                code( [code: author.code, displayName: author.codeDisplayName, codeSystem: author.codeSystem] )
                addr(use:"PUB" ){
                    streetAddressLine(author.addressLine1)
                    streetAddressLine(author.addressLine2)
                    city(author.city)
                    state(author.state)
                    postalCode(author.zipCode)
                    country("US")
                }
                telecom( value: "tel:${author.phone}", use: "WP"  )
                assignedAuthoringDevice(){
                    manufacturerModelName("Integrity Digital Solutions, LLC")
                    softwareName("Integrity EMR")
                }
            }
        }
    }

    public static void test(String[] args){
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        new AuthorTemplate(builder, AuthorTemplate.TEST_MAP)
        println writer
    }


}
