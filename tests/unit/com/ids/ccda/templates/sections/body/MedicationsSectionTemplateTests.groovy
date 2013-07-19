package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.ImmunizationsSectionTemplate
import com.ids.ccda.templates.sections.body.MedicationsSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class MedicationsSectionTemplateTests {
    public static final MAP_KEY = "medications"
    public static final TEST_MAP =[  //MU
            "132513251": [  code: "197517", name: "Clarinthromycin 500 MG Oral Tablet",  routeCode: "C38288", routeName: "ORAL",
                    startDate: (new Date() - 365).format("yyyyMMdd") , stopDate: "",
                    dosageQuantity: "500", dosageUnit: "MG",
                    instructions: "by mouth twice daily for 7 days"],
            "1631326": [  code: "866924", name: "Metoprolol Tartrate 25 MG Oral Tablet",  routeCode: "C38288", routeName: "ORAL",
                    startDate: (new Date() - 710).format("yyyyMMdd") , stopDate: "",
                    dosageQuantity: "25", dosageUnit: "MG",
                    instructions: "by mouth once daily"],
            "236235234": [  code: "573521", name: "Albuterol",  routeCode: "38216", routeName: "RESPIRATORY (INHALATION)",
                    startDate: new Date().format("yyyyMMdd"), stopDate: "",
                    dosageQuantity: "0.09" ,dosageUnit: "MG/ACTUAT",
                    instructions: "2 puffs once every 6 hours PRN wheezing"]
    ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [medications:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new MedicationsSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
