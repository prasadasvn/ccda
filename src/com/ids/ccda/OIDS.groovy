package com.ids.ccda

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/11/13
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
class OIDS {
    static final IDS_ROOT = "2.16.840.1.113883.3.1048";

    static final IDS_EMR = "${IDS_ROOT}.1";

    static final IDS_EMR_DOCUMENTS = "${IDS_EMR}.1";
    static final IDS_EMR_IDS = "${IDS_EMR}.11";

    static final IDS_CLINICAL_SUMMARY = "${IDS_EMR_DOCUMENTS}.1";

    static final IDS_PATIENT_MRN = "${IDS_EMR_IDS}.1";

    static final FORMAT_DATE_DTM_US_FIELDED = "yyyyMMddHHmmSSZ"

    static final NPI = "2.16.840.1.113883.4.6"

    static final SNOMED = "2.16.840.1.113883.6.96"

    static final INTERPRETATION_RESULTS = "2.16.840.1.113883.5.83"

}
