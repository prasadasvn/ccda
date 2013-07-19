package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder
@Mixin(Comment)
class PlanOfCareSectionTemplate  implements BodySectionTemplate{
    public static final TITLE = "Care Plan"
    public static final MAP_KEY = ["planEncounters", "planProcedures", "planObservations", "planInstructions"]
    public static final SECTION_CODE = [code:"18776-5", displayName:"TREATMENT PLAN"] + HL7_OID.LOINC

    def ENCOUNTER_ATTRS = ["visitCode", "visitName", "date", "instructions"]
    def OBSERVATION_ATTRS = [ "code", "name", "codeSystem", /*either SNOMED or LOINC"*/ "date"]
    def PROCEDURE_ATTRS = [ "code", "name", "codeSystem", /*either SNOMED or LOINC"*/ "date"]
    def INSTRUCTIONS_ATTRS = [ "instructions", "goal"]
    DocUid docUid
    MarkupBuilder builder
    Map map

    PlanOfCareSectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map ?: [:]
        generate()
    }

    def generate(){
      builder.mkp.comment(comment())
      builder.component(){
          section(){
              templateId(HL7_OID.PLAN_OF_CARE_TEMPLATE_ID)
              code(SECTION_CODE)
              title(TITLE)
              generateNormativeText()
              map.planEncounters.each{ encounterId, encounter -> generateEncounter(encounterId, encounter)}
              map.planProcedures.each{ procedureId, procedure -> generateProcedure(procedureId, procedure)}
              map.planObservations.each{ observationId, observation -> generateObservation(observationId, observation)}
              map.planInstructions.each{ instructionId, instruction -> generateInstruction(instructionId,instruction)}
          }
      }
    }

    def generateNormativeText(){
        builder.text(){
            table(border:"1", width:"100%"){
                thead(){
                    tr(){
                        th("Planned Activity")
                        th("Comments")
                        th("Planned Date")
                        th("Plan/Goal")
                    }
                }
                tbody(){
                  map.planEncounters.each{ encounterId, encounter -> generateEncounterNormativeText(encounterId, encounter)}
                  map.planProcedures.each{ procedureId, procedure -> generateProcedureNormativeText(procedureId, procedure)}
                  map.planObservations.each{ observationId, observation -> generateObservationNormativeText(observationId, observation)}
                  map.planInstructions.each{ instructionId, instruction -> generateInstructionNormativeText(instructionId,instruction)}
                }
             }
        }
    }

    def generateEncounterNormativeText(encounterId, encounter = [:]){
        def uid = docUid.planEncounterId(encounterId)
        builder.tr(){
            td{ content(ID:"encounter-${uid}",encounter?.visitName) }
            td(encounter?.instructions)
            td(encounter?.date)
            td("Plan")
        }
    }

    def generateObservationNormativeText(observationId, observation = [:]){
        println "observation: ${observation}"
        def uid = docUid.planObservationId(observationId)
        builder.tr(){
            td(colspan:"2"){ content(ID:"observation-${uid}", observation?.name)}
            td(observation.date)
            td("Plan")
        }
    }

    def generateProcedureNormativeText(procedureId, procedure = [:]){
        def uid = docUid.planProcedureId(procedureId)
        builder.tr(){
            td(colspan:"2"){ content(ID:"procedure-${uid}", procedure?.name) }
            td(procedure.date)
            td("Plan")
        }
    }

    def generateInstructionNormativeText(instructionId, instruction = [:]){
        def uid = docUid.planInstructionId(instructionId)
        builder.tr(){
            td( colspan:"3"){ content(ID:"instruction-${uid}", instruction?.instructions) }
            td( (instruction?.goal)?"Goal":"Plan" )
        }
    }




    def generateEncounter( encounterId, encounterMap = [:]){ //note encounter encompasses future appointments, and  referrals,
      builder.entry(){
      // PLAN OF CARE ACTIVITY ENCOUNTER TEMPLATE
          encounter(moodCode:"INT", classCode:"ENC"){
              templateId(HL7_OID.PLAN_OF_CARE_ACTIVITY_ENCOUNTER_TEMPLATE_ID)
              id(root:docUid.planEncounterId(encounterId))
              code([code:encounterMap.visitCode, name: encounterMap.visitName] + HL7_OID.CPT)  //dynamic, must be CPT code
              effectiveTime(){ center(value:encounterMap.date)}//dynamic
            if(encounterMap.instructions){
              entryRelationship(typeCode: "SUBJ", inversionInd: "true"){
                act(classCode:"ACT", moodCode:"INT"){
                    templateId(HL7_OID.INSTRUCTIONS_TEMPLATE_ID)
                    code(["xsi:type":"CE", code: "409073007", displayName: "instruction"] + HL7_OID.SNOMED)
                    text(encounterMap.instructions)  //dynamic
                    statusCode(code:"completed")
                }
              }
            }
          }
      }
    }

    def generateObservation( observationId, observationMap = [:]){
      builder.entry(typeCode:"DRIV"){
      // PLAN OF CARE ACTIVITY OBSERVATION TEMPLATE
        observation(classCode:"OBS", moodCode:"RQO"){
            templateId(HL7_OID.PLAN_OF_CARE_ACTIVITY_OBSERVATION_TEMPLATE_ID)
            id(docUid.planObservationId(observationId))
            code([code:observationMap.code, displayName: observationMap.name] + deriveCodingSystem(observationMap.codeSystem) )
            statusCode(code:"new")
            effectiveTime(){
                center(value:observationMap.date)
            }
        }
      }
    }

    def generateProcedure( procedureId, procedureMap = [:]){
      builder.entry(){
        //PLAN OF CARE ACTIVITY PROCEDURE TEMPLATE
        procedure(moodCode:"RQO", classCode:"PROC"){
            templateId(HL7_OID.PLAN_OF_CARE_ACTIVITY_PROCEDURE_TEMPLATE_ID)
            id(docUid.planProcedureId(procedureId))
            code([procedureMap.code, procedureMap.name] + deriveCodingSystem(procedureMap.codeSystem))
            statusCode(code:"new")
            effectiveTime(){
                center(value:procedureMap.date)
            }
        }
      }
    }

    def generateInstruction( instructionId, instruction = [:]){
        def uid = docUid.planInstructionId(instructionId)
        builder.entry(typeCode:"DRIV"){
            act(classCode:"ACT", moodCode: (instruction.goal)? "GOL" : "INT" ){
                templateId(HL7_OID.INSTRUCTIONS_TEMPLATE_ID)
                code(["xsi:type":"CE", code: "409073007", displayName: "Education"] + HL7_OID.SNOMED)
                text(){
                    //reference(value:"instructions-${uid}")
                    instruction.instructions
                }
                statusCode(code:"completed")
            }
        }
    }

    private def deriveCodingSystem(str){
        def codingSystem = null
        switch(str){
         case "CPT" : codingSystem = HL7_OID.CPT; break
         case "SNOMED" : codingSystem = HL7_OID.SNOMED; break
         case "LOINC" : codingSystem = HL7_OID.LOINC; break
         default: null
        }
        return codingSystem
    }



}
