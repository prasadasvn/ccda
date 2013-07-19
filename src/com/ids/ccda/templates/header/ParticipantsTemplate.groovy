package com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

class ParticipantsTemplate  implements HeaderTemplate {

    def ATTRS = ["relationshipCode", "relationshipDisplayName", "firstName", "lastName"]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def participants = []

    ParticipantsTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.participants= map.participants ?: [:]
        generate()
    }

    def generate(){
        participants.each{ participant -> generateParticipant(participant)}
    }

    def generateParticipant(participant = [:]) {
        builder.participant(typeCode:"IND"){
            associatedEntity(classCode:"PRS"){
                code([code: participant.relationshipCode, displayName: participant.relationshipDisplayName ] + HL7_OID.PERSONAL_RELATIONSHIP_ROLE    )
                associatedPerson(){
                    name(){
                        given(participant.firstName)
                        family(participant.lastName)
                    }
                }
            }
        }
    }


}
