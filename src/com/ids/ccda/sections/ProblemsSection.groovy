package com.ids.ccda.sections

import groovy.xml.MarkupBuilder

class ProblemsSection {
    def map
    MarkupBuilder builder
    def problems = [:]
    def ATTRS = ["uid", "code", "name", "statusCode", "statusName", "onsetDate", "resolutionDate" ]

    ProblemsSection(builder, map =[:]) {
        this.builder = builder
        this.map = map
        this.problems = this.map.problems
        generate()
    }

    def generate(){
      builder.component(){
          section(){
              templateId(root:"2.16.840.1.113883.10.20.22.2.5")
              templateId(root:"2.16.840.1.113883.10.20.22.2.5.1")
              code(code:"11450-4",
                   codeSystem:"2.16.840.1.113883.6.1",
                   codeSystemName:"LOINC",
                   displayName:"PROBLEM LIST"
              )
              title("Problems")
              generateNormativeText()
              problems.each { problem ->
                generateEntry(problem)
              }

          }
      }
    }

    def generateNormativeText(){
        builder.text(){
          content("ID":"problems")
          list(listType:"ordered"){
            problems.each { problem ->
              item(){
                  content("ID":"problem-${problem.uid}" ){problem.name}
                  content("ID":"status-${problem.statusUid}" ){"Status: ${problem.statusName}"}
              }
            }
          }
        }
    }

    def generateEntry( problem = [:]){
      builder.entry(){
          // PROBLEM CONCERN ACT TEMPLATE
        entry(typeCode:"DRIV"){
            act(classCode:"ACT", moodCode:"ENV"){
                templateId(root:"2.16.840.1.113883.10.20.22.4.3")
                id(root: problem.uid)  //dynamic
                code(code:"CONC",
                     codeSystem:"2.16.840.1.113883.5.6",
                     displayName:"Concern")
                statusCode(code: problem.status) //dynamic //from list of active, suspended, aborted, completed
                generateEffectiveTime(problem.activeDates)
                entryRelationship(typeCode:"REFR"){
                    observation(classCode:"OBS", moodCode:"ENV"){
                        templateId(root:"2.16.840.1.113883.10.20.22.4.6")
                        id(root:UUID.randomUUID())
                        code(code:"55607006", codeSystem: "2.16.840.1.113883.6.96", displayName:"Problem") //static, could be dynamic
                        text(){
                            reference(value:"#problem-${problem.uid}")
                        }
                        statusCode(code:"completed")
                        generateEffectiveTime(problem.observedDates)
                        //low shall be present
                        //high should be present; if problem resolved but resolutionDate not known, use null flavor UNK
                        value("xsi:type":"CD",
                              code:problem.code, //dynamic
                              codeSystem:"2.16.840.1.113883.6.96",
                              displayName: problem.name)  //dynamic
                        entryRelationship( typeCode:"REFR"){
                            observration(classCode:"OBS", moodCode:"EVN"){
                                templateId(root:"2.16.840.1.113883.10.20.22.4.6")
                                id(root:UUID.randomUUID())
                                code(code:"33999-4",
                                     codeSystem:"2.16.840.1.113883.6.1",
                                     displayName: "Status")
                                text(){
                                    reference(value:"#status-${problem.uid}") //might need to reference this differently
                                }
                                statusCode(code:"completed")
                                value("xsi:type":"CD",
                                      code: problem.statusCode,  //dynamic
                                      codeSystem:"2.16.840.1.113883.6.96",
                                      displayName:problem.statusName)

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
