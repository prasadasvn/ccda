package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.AllergiesSectionTemplate
import com.ids.ccda.templates.sections.body.PlanOfCareSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class PlanOfCareSectionTemplateTests {
    public static final TEST_MAP = [
            planEncounters: [
                    "12343235": [ visitCode: "99213", visitName: "Established Patient", date: (new Date() + 21).format("yyyyMMdd"),
                            instructions:"Follow up with Dr. Seven" ],
                    "34646773": [ visitCode: "99241", visitName: "Established Patient", date: (new Date() + 14).format("yyyyMMdd"),
                            instructions:"Follow up with Dr. Puffer for a pulmonology consultation" ]
            ],
            planObservations: [
                    "567895679": [  code: "6460-0", name: "Sputum Culture", codeSystem: "LOINC", date:"20120806"],
                    "6706796756": [ code: "168731009", name: "Chest X-Ray",  codeSystem: "SNOMED", date:"20120813"],
                    "586568574": [ code: "30313-1", name: "Chest HGB", codeSystem:"LOINC", date:"20120813"]
            ],
            planProcedures: [
                    "345745653":[code: "45378", name: "Colonoscopy", codeSystem:"CPT", date: "20120922"]
            ],
            planInstructions: [
                    "37745211": [ instructions: "Smoking cessation (SNOMED CT: 225423000)", goal:true],
                    "68790645": [ instructions: "Patient may continue to experience low grade fever and chills.", goal:false],
                    "75685345": [ instructions: "Return to clinic or call 911 if you experience chest pain, shortness of breath, high fevers, or intractable vomitting/diarrhea", goal:false]
            ]
    ]
    public static final MAP_KEY = ["planEncounters", "planObservations", "planProcedures", "planInstructions"]

    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, TEST_MAP)

    }

    @Test
    void testSomething() {
        template = new PlanOfCareSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
