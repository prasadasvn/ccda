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
    public static final SNOMED = "2.16.840.1.113883.6.96"
    static final INTERPRETATION_RESULTS = "2.16.840.1.113883.5.83"
    public static final NPI = "2.16.840.1.113883.4.6"
    public static final LOINC =[ codeSystem:"2.16.840.1.113883.6.1",   codeSystemName: "LOINC"]

    public static final US_REALM_TYPE_ID = [root:"2.16.840.1.113883.1.3", extension: "POCD_HD000040"]
    public static final US_REALM_HEADER_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.1.1"]
    public static final CCD_TEMPLATE_ID = [root:"2.16.840.1.113883.10.20.22.1.2"]
    public static final DOCUMENT_CONFIDENTIALITY = [codeSystem:"2.16.840.1.113883.5.25", code:"N"] //N-Normal, R-Restricted, V-Very Restricted, if ever needed







}
