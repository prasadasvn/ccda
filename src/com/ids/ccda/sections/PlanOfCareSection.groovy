package com.ids.ccda.sections

import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder

class PlanOfCareSection {
    public static final TITLE = "Care Plan"
    public static final SECTION_CODE = [code:"18776-5", displayName:"TREATMENT PLAN"] + HL7_OID.LOINC
    public static final SECTION = "plans"
    def ATTRS = [  ]
    def ENCOUNTER_ATTRS = ["visitCode", "visitName", "date", "instructions"]
    def OBSERVATION_ATTRS = [ "codeWithSystem" /*code,displayName,codeSystem,codeSystemName*/, "date"]
    def PROCEDURE_ATTRS = [ "codeWithSystem" /*code,displayName,codeSystem,codeSystemName*/, "date"]
    def INSTRUCTIONS_ATTRS = [ "instructions", "goal"]

    def map
    DocUid docUid
    MarkupBuilder builder
    def plans = [ instructions: [:],encounters: [:], procedures: [:] ]

    PlanOfCareSection(builder, map =[:]) {
        this.builder = builder
        this.docUid = map.docUid
        this.map = map
        this.plans = this.map.plans
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(HL7_OID.PLAN_OF_CARE_TEMPLATE_ID)
              code(SECTION_CODE)
              title(TITLE)
              generateNormativeText()
              plans.encounters.each{ encounterId, encounter -> generateEncounter(encounterId, encounter)}
              plans.procedures.each{ procedureId, procedure -> generateProcedure(procedureId, procedure)}
              plans.observations.each{ observationId, observation -> generateObservation(observationId, observation)}
              plans.instructions.each{ instructionId, instruction -> generateInstruction(instructionId,instruction)}
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
                  plans.encounters.each{ encounterId, encounter -> generateEncounterNormativeText(encounterId, encounter)}
                  plans.procedures.each{ procedureId, procedure -> generateProcedureNormativeText(procedureId, procedure)}
                  plans.observations.each{ observationId, observation -> generateObservationNormativeText(observationId, observation)}
                  plans.instructions.each{ instructionId, instruction -> generateInstructionNormativeText(instructionId,instruction)}
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
            td(colspan:"2"){ content(ID:"observation-${uid}", observation?.codeWithSystem?.displayName)}
            td(observation.date)
            td("Plan")
        }
    }

    def generateProcedureNormativeText(procedureId, procedure = [:]){
        def uid = docUid.planProcedureId(procedureId)
        builder.tr(){
            td(colspan:"2"){ content(ID:"procedure-${uid}", procedure?.codeWithSystem?.displayName) }
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
            code(observationMap.codeWithSystem)
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
            code(procedureMap.codeWithSystem)
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



}
