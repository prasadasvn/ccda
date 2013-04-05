package com.ids.ccda

import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/7/13
 * Time: 12:49 PM
 * To change this template use File | Settings | File Templates.
 */
class GeneralHeaderTemplate {

    def map
    MarkupBuilder builder

    GeneralHeaderTemplate(builder, map = [:]) {
        this.builder = builder
        this.map = map
        generate()
    }



   def generate(){
     generateHeader()
     generateRecordTarget()
     generateAuthor()
   }

   protected def generateHeader(){
       def pat = [id: "blah"]
       builder.realmCode(code:"US")
       builder.typeId(root:"2.16.840.1.113883.1.3",
               extension: "POCD_HD000040")
       //indicates conformance with US Realm Clinical Document Header template
       builder.templateId(root:"2.16.840.1.113883.10.20.22.1.1")
       //conforms to CCD requirements
       builder.templateId(root:"2.16.840.1.113883.10.20.22.1.2")
       builder.id(root: OIDS.IDS_ROOT,
               assigningAuthorityName:"Integrity EMR",
               extension: clinicSummaryId(map.tenantId, pat.id),
               displayable: "true" )
       builder.code(code: "34133-9",  //summary specific
               codeSystem:"2.16.840.1.113883.6.1",
               codeSystemName: "LOINC",
               displayName: "Summarization of Episode Note"  //summary specific
       )
       builder.title( "A Title Would Go Here") //summary specific, practice specific
       builder.effectiveTime(generateEffectiveDate())  //time created
       builder.confidentialityCode( codeSystem:"2.16.840.1.113883.5.25",
               code:"N")   //N-Normal, R-Restricted, V-Very Restricted, if ever needed
       builder.languageCode(code:"en-US")
   }

   protected def generateAuthor(){
       def author = map.author
       builder.author(){
           time(value: new Date().format(OIDS.FORMAT_DATE_DTM_US_FIELDED))
           assignedAuthor(){
               id( root:OIDS.NPI,
                       extension:"NPI") //TODO: I believe this could be either a provider or organization-level NPI
               code( code:"TAXONOMY CODE",  //TODO:  This would either correspond with the a provider or organization's specialty
                       codeSystem:"2.16.840.1.113883.6.101",
                       displayName:"TAXONOMY DISPLAY")
               addr(use:"PUB" ){
                   streetAddressLine(author.addressLine1)
                   streetAddressLine(author.addressLine2)
                   city(author.city)
                   state(author.state)
                   postalCode(author.zipCode)
                   country("US")
               }
               telecom( value: "tel:${author.phone}", use: "WP"  )
               assignedPerson(){
                   name(){
                       family(author.lastName)
                       given(author.firstName)
                       given(author.middleName)
                       prefix(author.title)
                       suffix(author.degree)
                   }
               }
               assignedAuthoringDevice(){
                   manufacturerModelName("Integrity Digital Solutions, LLC")
                   softwareName("Integrity EMR")
               }
               //either assigned person or assigned authoring device, not both
               /*
               If assignedAuthor has an associated representedOrganization with no assignedPerson or assignedAuthoringDevice, then the
               value for ClinicalDocument/author/assignedAuthor/id/@NullFlavor SHALL be "NA" "Not applicable"
               */
           }

       }
   }

   protected def generateRecordTarget(){
       def pat = map.patient
       builder.recordTarget(){
           patientRole(){
               id(extension:"PATIENT_MRN", root: OIDS.IDS_PATIENT_MRN )// insert patient's MRN
               addr(use:"HP" ){
                   streetAddressLine(pat.addressLine1)
                   streetAddressLine(pat.addressLine2)
                   city(pat.city)
                   state(pat.state)
                   postalCode(pat.zipCode)
                   country("US")
               }
               telecom( use:"HP", value: "tel:${pat.homePhone}"  )  //TODO: will need to convert to () -  format
               telecom( use:"WP", value: "tel:${pat.workPhone}"  )  //TODO: will need to convert to () -  format
               telecom( use:"MC", value: "tel:${pat.mobilePhone}"  )   //TODO: will need to convert to () -  format
               telecom(           value: "mailto:${pat.emailAddress}"  )
               patient(){
                   name( use:"L"){
                       family(pat.lastName)
                       given(pat.firstName)
                       given(pat.middleName)
                   }
                   administrativeGenderCode( code:"M",    //TODO:will need adapter
                           codeSystem:"2.16.840.1.113883.5.1",
                           displayName: "Male")
                   birthTime(pat.dateOfBirth)
                   maritalStatusCode(code:"M",  //TODO:will need adapter
                           displayName:"Married",
                           codeSystem:"2.16.840.1.113883.5.2",
                           codeSystemName:"MaritalStatusCode")

                   raceCode( code: "2054-5", //TODO: will need adapter
                           displayName: "Black or African American",
                           codeSystem:"2.16.840.1.113883.6.238",
                           codeSystemName: "Race and Ethnicity - CDC")
                   /*http://phinvads.cdc.gov/vads/ViewCodeSystemConcept.action?oid=2.16.840.
                   1.113883.6.238&code=1000-9 */

                   ethnicGroupCode(  code:"2186-5", //TODO: will need adapter
                           displayame: "Not Hispanic or Latino",
                           codeSystem:"2.16.840.1.113883.6.238",
                           codeSystemName: "Race and Ethnicity - CDC"
                   )
               }
               //providerOrganization() //TODO:if we want this (not required), we'll need to add address and telephone fields to either clinic site or practice
           }
       }
   }

   def generateCustodian(){
     def custodian = map.custodian
     builder.custodian(){
       assignedCustodian(){
           representedCustodianOrganization(){
               id(root:"2.16.840.1.113883.4.6",
                  extension: custodian.npi //TODO: Organization NPI
               )
               name(custodian.practice_name)//TODO: Practice Name
               telecom( value: "tel:${custodian.phone_number}" , use: "WP")
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

   def generateDocumentationOf(){
     def serviceEvent = map.serviceEvent
     builder.documentationOf(){
         serviceEvent(classCode:"PCPR"){
             //code
             effectiveTime(){
                 low(value:serviceEvent.lowEffectiveTime?.format(OIDS.FORMAT_DATE_DTM_US_FIELDED))
                 high(value:new Date().format(OIDS.FORMAT_DATE_DTM_US_FIELDED))
             }
             serviceEvent.performers.each {   //should, not shall
               performer(typeCode:"PRF"){
                   //function code could be PP - PrimaryCarePhysician, RP - Referring, or CP Consulting
                   functionCode(code:"PP",
                                displayName:"Primary Performer",
                                codeSystem:"2.16.840.1.113883.12.443",
                                codeSystemName: "Provider Role"){
                       originalText("Primary Care Provider")
                   }
               }
             }
         }
     }
   }

   private def generateEffectiveDate(){
        new Date().format(OIDS.FORMAT_DATE_DTM_US_FIELDED)
    }

    // Need globally unique OID, INTEGRITY_OID + EMR + DOC + TENANT + PATIENT + DATETIME
    private def clinicSummaryId( tenantId, patientId, Date dateGenerated = new Date(), String idsClinicalSummaryOid = OIDS.IDS_CLINICAL_SUMMARY){
        return "${idsClinicalSummaryOid}.${tenantId}.${patientId}.${dateGenerated.format(OIDS.FORMAT_DATE_DTM_US_FIELDED)}"
    }

}



//TODO:Adapter somewhere to convert marital status to appropriate codes (pg 54, Table 6)
