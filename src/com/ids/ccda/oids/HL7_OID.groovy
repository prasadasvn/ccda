package com.ids.ccda.oids

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 4/29/13
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
class HL7_OID {
    public static final FORMAT_DATE_DTM_US_FIELDED = "yyyyMMddHHmmSSZ"
    public static final SNOMED = [codeSystem:"2.16.840.1.113883.6.96", codeSystemName:"SNOMED-CT"]
    static final INTERPRETATION_RESULTS = "2.16.840.1.113883.5.83"
    public static final NPI = "2.16.840.1.113883.4.6"
    public static final LOINC =[ codeSystem:"2.16.840.1.113883.6.1",   codeSystemName: "LOINC"]
    public static final RX_NORM = [codeSystem: "2.16.840.1.113883.6.88", codeSystemName:"RxNorm"]
    public static final CVX = [ codeSystem: "2.16.840.1.113883.6.59", codeSystemName: "CVX"]
    public static final ACT_CLASS = [codeSystem: "2.16.840.1.113883.5.6", codeSystemName:"HL7ActClass"]
    public static final ACT_CODE= [codeSystem: "2.16.840.1.113883.5.4", codeSystemName:"ActCode"]

    public static final GENDER_CODE = [codeSystem: "2.16.840.1.113883.5.1",codeSystemName:"AdministrativeGenderCode"]
    public static final MARITAL_CODE = [codeSystem:"2.16.840.1.113883.5.2", codeSystemName:"MaritalStatusCode"]
    public static final RACE_OR_ETHNICITY_CODE = [codeSystem:"2.16.840.1.113883.6.238", codeSystemName: "Race and Ethnicity - CDC"]
    /*http://phinvads.cdc.gov/vads/ViewCodeSystemConcept.action?oid=2.16.840.1.113883.6.238&code=1000-9 */
    public static final PERSONAL_RELATIONSHIP_ROLE = [codeSystem:"2.16.840.1.113883.1.11.19563",    codeSystemName:"Personal Relationship Role Type Value Set"]
    public static final PROVIDER_ROLE = [codeSystem:"2.16.840.1.113883.12.443",  codeSystemName: "Provider Role"]


    public static final US_REALM_TYPE_ID = [root:"2.16.840.1.113883.1.3", extension: "POCD_HD000040"]
    public static final US_REALM_HEADER_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.1.1"]
    public static final CCD_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.1.2"]
    public static final DOCUMENT_CONFIDENTIALITY = [codeSystem:"2.16.840.1.113883.5.25", code:"N"] //N-Normal, R-Restricted, V-Very Restricted, if ever needed

    public static final ALLERGIES_SECTION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.2.6.1"]
    public static final ALLERGY_PROBLEM_ACT_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.30"]
    public static final ALLERGY_INTOLERANCE_OBSERVATION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.7"]

    public static final IMMUNIZATION_SECTION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.2.2.1"]
    public static final IMMUNIZATION_ACTIVITY_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.52"]
    public static final IMMUNIZATION_MEDICATION_INFORMATION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.54"]

    public static final MEDICATIONS_SECTION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.2.1.1"]
    public static final MEDICATIONS_ACTIVITY_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.16"]
    public static final MEDICATION_INFORMATION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.23"]

    public static final INSTRUCTIONS_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.20"]

    public static final PROBLEMS_SECTION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.2.5.1"]
    public static final PROBLEM_CONCERN_ACT_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.3"]
    public static final PROBLEM_OBSERVATION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.4"]
    public static final PROBLEM_STATUS_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.6"]

    public static final PROCEDURES_SECTION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.2.7.1"]
    public static final PROCEDURE_ACTIVITY_PROCEDURE_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.4.14"]

    public static final RESULTS_SECTION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.2.3.1"]

    public static final SOCIAL_HISTORY_SECTION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.2.17"]
    public static final SMOKING_STATUS_OBSERVATION_TEMPLATE_ID = [root:"2.16.840.1.113883.10.22.4.78"]

}
