package com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.mixins.Comment
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.oids.HL7_OID
import groovy.xml.MarkupBuilder
@Mixin(Comment)
class ProblemsSectionTemplate  implements BodySectionTemplate{
    public static final TITLE = "Problems"
    public static final MAP_KEY = "problems"
    public static final SECTION_CODE = [code:"11450-4", displayName:"PROBLEM LIST"] + HL7_OID.LOINC
    public static final CONCERN_CODE = [code:"CONC",displayName:"Concern"] + HL7_OID.ACT_CLASS
    public static final PROBLEM_CODE = [code:"55607006",  displayName:"Problem"] + HL7_OID.SNOMED  //codeSystem: "2.16.840.1.113883.6.96",
    public static final PROBLEM_STATUS_CODE = [code:"33999-4", displayName: "Status"] + HL7_OID.LOINC

    def ATTRS = [ "code", "name", "statusCode", "statusName", "onsetDate", "resolutionDate" ]
    DocUid docUid
    MarkupBuilder builder
    Map map
    def problems = [:]

    ProblemsSectionTemplate(Document doc) {
        this.builder = doc.builder
        this.docUid = doc.docUid
        this.map = doc.map
        this.problems = map.problems ?: [:]
        generate()
    }

    def generate(){
        builder.mkp.comment(comment())
        builder.component(){
          section(){
              templateId(HL7_OID.PROBLEMS_SECTION_TEMPLATE_ID)
              code( SECTION_CODE )
              title(TITLE)
              generateNormativeText()
              problems.each { id, problem ->
                generateEntry(id, problem)
              }
          }
      }
    }

    def generateNormativeText(){
        builder.text(){
          content("ID":"problems")
          list(listType:"ordered"){
            problems.each { id,problem ->
               def uid = docUid.secId(MAP_KEY,id)
                item{
                  content("ID":"problem-${uid}",problem.name)
                  content("ID":"status-${uid}","Status: ${problem.statusName}")
              }
            }
          }
        }
    }

    def generateEntry( problemId,problem = [:]){
        def uid = docUid.secId(MAP_KEY,problemId)
        builder.entry(){
          // PROBLEM CONCERN ACT TEMPLATE
        entry(typeCode:"DRIV"){
            act(classCode:"ACT", moodCode:"ENV"){
                templateId(HL7_OID.PROBLEM_CONCERN_ACT_TEMPLATE_ID)
                id(root: uid)  //dynamic
                code(CONCERN_CODE)
                statusCode(code: problem.status) //dynamic //from list of active, suspended, aborted, completed
                generateEffectiveTime(problem.activeDates)
                entryRelationship(typeCode:"REFR"){
                    observation(classCode:"OBS", moodCode:"ENV"){
                        templateId(HL7_OID.PROBLEM_OBSERVATION_TEMPLATE_ID)
                        id(root:docUid.probObsId(problemId))
                        code(PROBLEM_CODE) //static, could be dynamic
                        //text(){ reference(value:"#problem-${uid}") }
                        statusCode(code:"completed")
                        generateEffectiveTime(problem.observedDates)
                        //low shall be present
                        //high should be present; if problem resolved but resolutionDate not known, use null flavor UNK
                        value(["xsi:type":"CD", code:problem.code, displayName: problem.name] + HL7_OID.SNOMED) //dynamic
                        entryRelationship( typeCode:"REFR"){
                            observration(classCode:"OBS", moodCode:"EVN"){
                                templateId(HL7_OID.PROBLEM_STATUS_TEMPLATE_ID)
                                id(root:UUID.randomUUID())
                                code(PROBLEM_STATUS_CODE)
                                //text(){ reference(value:"#status-${uid}") }
                                statusCode(code:"completed")
                                value(["xsi:type":"CD", code: problem.statusCode,displayName:problem.statusName] + HL7_OID.SNOMED)
                                /*
                                  Must be one of the following SNOMED codes:
                                  413322009, Resolved
                                  55561003, Active
                                  73425007, Inactive
                                 */
                            }
                        }

                    }

                }


            }
        }
      }
    }


    private generateEffectiveTime( dates = [:]){
        builder.effectiveTime(){
            if(dates.startDate){
                low(value:dates.startDate)
            }
            if(dates.endDate){
                high(value:dates.endDate)
            }
        }
    }
}
