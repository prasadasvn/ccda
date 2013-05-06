package com.ids.ccda.documents.ccd.uid

import com.ids.ccda.oids.IDS_OID

class DocUid {
    String root
    static final UNKNOWN = "0000"
    static final DOCS = [
            section : [
                    allergies: "1" ,
                    immunizations: "2",
                    medications: "3",
                    problems: "4",
                    results: "5",
                    socialHistory: "6",
                    vitalSigns: "7"
            ]
    ]
    DocUid(root) {
        this.root = root
    }

    String toString(){
        root
    }

   def secId(section = UNKNOWN, id = UNKNOWN){
     ( DOCS.section."${section}" ) ?  "${root}.${DOCS.section."${section}"}.${id}" :  "${root}.${UNKNOWN}.${id}"
   }

   def medInfoId(id = UNKNOWN){
       "${root}.${DOCS.section.medications}.1.${id}"
   }

    def probObsId(id = UNKNOWN){
        "${root}.${DOCS.section.problems}.1.${id}"
    }

    public static void main(String[] args) {
       def docUid = new DocUid("${IDS_OID.IDS_CLINICAL_SUMMARY}.1.1234.${(new Date()).format(IDS_OID.FORMAT_DATE_OID)}")
       println docUid
       println docUid.secId("allergies", "45")
       println docUid.secId("blerg", "5")
       println docUid.secId("blerg")
       println docUid.secId("vitalSigns", "2")
    }

}
