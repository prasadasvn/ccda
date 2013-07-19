package com.ids.ccda.tests

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID

class TestCDA {
    Document document

    TestCDA( Document document) {
        this.document = document
        generate()
    }

    public static void main(String[] args) {

        def map = [:]
        map.templateId = "5678"
        map.patient = [ id: "1234", firstName: "John", lastName: "Doe", middleName: "Q",   //MU
                dateOfBirth: "19690413", gender: "M", maritalStatus: "M",
                addressLine1: "123 Main St", addressLine2: "Apt 24", city: "Temple",
                state: "TX", zipCode: "76502"
        ]

        def docUid = new DocUid("${IDS_OID.IDS_CLINICAL_SUMMARY}.1.1234.${(new Date()).format(IDS_OID.FORMAT_DATE_OID)}")
        def document = new Document( docUid, map)
        new TestCDA( document  )
        document.display()

    }



    def generate(){
       document.builder.component(){
           structuredBody{
               arrayOfSections().each {section ->
                  def clazz = this.class.classLoader.loadClass( "com.ids.ccda.templates.sections.body.${section}", true )
                  populateMap(clazz)
                  this.class.classLoader.loadClass( "com.ids.ccda.templates.sections.body.${section}", true )?.newInstance(document).builder
               }

           }
       }
    }

    def populateMap(Class clazz){
        if(clazz."MAP_KEY" instanceof String){
           document.map."${clazz."MAP_KEY"}" = clazz."TEST_MAP"
        } else if(clazz."MAP_KEY" instanceof ArrayList) {
          clazz."MAP_KEY".each { key ->
           document.map."${key}" =  clazz."TEST_MAP"."${key}"
          }
        } else {
            println "Some crazy stuff trying to populate map: ${clazz."MAP_KEY"}"
        }

    }

    def arrayOfSections(){
        def pwd = new File("").getAbsolutePath()
        def sections = new File("${pwd}/src/com/ids/ccda/templates/sections/body").listFiles().collect { file -> file.name.split("/")[-1].tokenize(".")[0] }
        sections.remove("BodySectionTemplate")
        return sections
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
