package com.ids.ccda.oids

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/11/13
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
class IDS_OID {
    //ORG.PRODUCT.DOCUMENT_TYPE.RECORD_ID
    //OID, INTEGRITY_OID + EMR + DOC + TENANT + PATIENT + DATETIME

    static final IDS_ROOT = "2.16.840.1.113883.3.1048";

    static final IDS_EMR = "${IDS_ROOT}.1";

    static final IDS_EMR_DOCUMENTS = "${IDS_EMR}.1";
    static final IDS_EMR_IDS = "${IDS_EMR}.11";

    static final IDS_CLINICAL_SUMMARY = "${IDS_EMR_DOCUMENTS}.1";

    static final IDS_PATIENT_ID = "${IDS_EMR_IDS}.1";
    static final IDS_PATIENT_MRN = "${IDS_EMR_IDS}.2";
    static final IDS_PATIENT_PMS_ID = "${IDS_EMR_IDS}.3";

    public static final FORMAT_DATE_OID = "yyyyMMddHHmmSS"
}
