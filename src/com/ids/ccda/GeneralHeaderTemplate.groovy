package com.ids.ccda

import com.ids.ccda.oids.HL7_OID
import com.ids.ccda.oids.IDS_OID
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
    def patient = [:]
    def author = [:]
    def custodian = [:]
    def participants = []
    def encounters = []

    MarkupBuilder builder
    // all dates need to formatted according to IDS_OID.FORMAT_DATE_DTM_US_FIELDED
    def MAP_ATTRS = [ "title", "effectiveDate"]
    def PAT_ATTRS = [ "id", "mrn", "pmsId",
                      "lastName", "firstName", "middleName",  "dateOfBirth",
                      "addressLine1", "addressLine2", "city", "state", "zipCode",
                      "homePhone", "workPhone", "mobilePhone", "email",
                      "gender","maritalStatus", "race", "ethnicity" //TODO: each with code and displayName attrs
                      ]

    def AUTHOR =  [ companyName: "Integrity Digital Solutions, LLC", softwareName: "Integrity EMR",
                    addressLine1: "6420 South General Bruce Drive", city: "Temple", state: "TX", zipCode: "76524",
                    phone: "(877) 905-4680",
                    code:[ code:"207W00000X", displayName:"OPHTHALMOLOGY", codeSystem:"2.16.840.1.113883.6.101" ]  ]

    def CUSTODIAN_ATTRS =  [  "practiceName", "npi","addressLine1", "addressLine2", "city", "state", "zipCode", "phone"]
    def PARTICIPANT_ATTRS = [ "relationshipCode" /* code, displayName*/, "firstName", "lastName"]
    def ENCOUNTER_ATTRS = [ "startDate", "endDate", "attendingProvider" /*npi, firstName, lastName  */]


    GeneralHeaderTemplate(builder, map = [:]) {
        this.builder = builder
        this.map = map
        this.patient = map.patient
        this.author = AUTHOR + map.author
        this.custodian = map.custodian
        this.participants = map.participants
        this.encounters = map.encounters
        generate()
    }



   def generate(){
     generateHeader()
     generateRecordTarget()
     generateAuthor()
     generateCustodian()
     generateParticipants()

   }

   protected def generateHeader(){
       builder.realmCode(code:"US")
       builder.typeId(HL7_OID.US_REALM_TYPE_ID)
       builder.templateId(HL7_OID.US_REALM_HEADER_TEMPLATE_ID)
       builder.templateId(HL7_OID.CCD_TEMPLATE_ID)
       builder.id(root: IDS_OID.IDS_ROOT,
               assigningAuthorityName:"Integrity EMR",
               extension: clinicSummaryId(map.tenantId, patient.id), //TODO: move to SledgeHammer
               displayable: "true" )
       builder.code([code: "34133-9",  displayName: "Summarization of Episode Note"] + HL7_OID.LOINC)
       builder.title( map.title) //summary specific, practice specific
       builder.effectiveTime(generateEffectiveDate())  //time created
       builder.confidentialityCode( HL7_OID.DOCUMENT_CONFIDENTIALITY)
       builder.languageCode(code:"en-US")
   }

   protected def generateAuthor(){
       def author = map.author
       builder.author(){  //INTEGRITY EMR IS THE AUTHOR
           time(value: new Date().format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
           assignedAuthor(){
               id( root: HL7_OID.NPI,     nullFlavor:"NA")
               code( author.code )
               addr(use:"PUB" ){
                   streetAddressLine(author.addressLine1)
                   streetAddressLine(author.addressLine2)
                   city(author.city)
                   state(author.state)
                   postalCode(author.zipCode)
                   country("US")
               }
               telecom( value: "tel:${author.phone}", use: "WP"  )
/*               assignedPerson(){
                   name(){
                       family(author.lastName)
                       given(author.firstName)
                       given(author.middleName)
                       prefix(author.title)
                       suffix(author.degree)
                   }
               }*/
               assignedAuthoringDevice(){
                   manufacturerModelName("Integrity Digital Solutions, LLC")
                   softwareName("Integrity EMR")
               }
           }
       }
   }


   protected def generateRecordTarget(){
       builder.recordTarget(){
           patientRole(){
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
                   administrativeGenderCode( code: patient.gender.code,
                           codeSystem:"2.16.840.1.113883.5.1",
                           displayName: patient.gender.diplayName)
                   birthTime(patient.dateOfBirth)
                   maritalStatusCode(code:patient.maritalStatus.code,
                           displayName:patient.maritalStatus.displayName,
                           codeSystem:"2.16.840.1.113883.5.2",
                           codeSystemName:"MaritalStatusCode")

                   raceCode( code: patient.race.code,
                           displayName: patient.race.displayName,
                           codeSystem:"2.16.840.1.113883.6.238",
                           codeSystemName: "Race and Ethnicity - CDC")
                   /*http://phinvads.cdc.gov/vads/ViewCodeSystemConcept.action?oid=2.16.840.
                   1.113883.6.238&code=1000-9 */

                   ethnicGroupCode(  code:patient.ethnicity.code,
                           displayame: patient.ethnicity.displayName,
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
               id(root:"2.16.840.1.113883.4.6", extension: custodian.npi)  //Organization NPI
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

   def generateParticipants(){
      participants.each{ participant -> generateParticipant(participant)}
   }

   def generateParticipant(participant = [:]) {
       builder.participant(typeCode:"IND"){
           associatedEntity(classCode:"PRS"){
               code(participant.relationshipCode +
                    [codeSystem:"2.16.840.1.113883.1.11.19563",
                     codeSystemName:"Personal Relationship Role Type Value Set"])
               associatedPerson(){
                   name(){
                       given(particpant.firstName)
                       family(particpant.lastName)
                   }
               }
           }
       }
   }



   def generateServiceEvents(){
       encounters.each { encounter -> generateServiceEvent(encounter)}
   }

   def generateServiceEvent( encounter = [:]){
     builder.documentationOf( typeCode:"DOC"){
         serviceEvent(classCode:"PCPR"){
             code( encounter.primaryDiagnosis + [codeSystem:"2.16.840.1.113883.6.96", codeSystemName:"SNOMED-CT"])
             effectiveTime(){
                 low(value:encounter.startDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
                 high(value:encounter.endDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
             }

               performer(typeCode:"PRF"){
                   //function code could be PP - PrimaryCarePhysician, RP - Referring, or CP Consulting
                   functionCode(code:"PP",
                                displayName:"Primary Performer",
                                codeSystem:"2.16.840.1.113883.12.443",
                                codeSystemName: "Provider Role"){
                       originalText("Primary Care Provider")
                   }
                   time(){
                       low(value:encounter.startDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
                       high(value:encounter.endDate?.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED))
                   }
                   assignedEntity(){
                       id( root: HL7_OID.NPI, extension: encounter.attendingProvider.npi)
                       assignedPerson(){
                           name(){
                               given(encounter.attendingProvider.firstName)
                               family(encounter.attendingProvider.lastName)
                           }
                       }
                   }

               }
         }
     }
   }


   private def generateEffectiveDate(){
        map.effectiveDate.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED)
    }

    // Need globally unique OID, INTEGRITY_OID + EMR + DOC + TENANT + PATIENT + DATETIME
    private def clinicSummaryId( tenantId, patientId, Date dateGenerated = new Date(), String idsClinicalSummaryOid = IDS_OID.IDS_CLINICAL_SUMMARY){
        return "${idsClinicalSummaryOid}.${tenantId}.${patientId}.${dateGenerated.format(HL7_OID.FORMAT_DATE_DTM_US_FIELDED)}"
    }

}



//TODO:Adapter somewhere to convert marital status to appropriate codes (pg 54, Table 6)
